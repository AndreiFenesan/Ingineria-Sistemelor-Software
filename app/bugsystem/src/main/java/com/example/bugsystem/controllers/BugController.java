package com.example.bugsystem.controllers;
import com.example.bugsystem.controllers.errors.ErrorMessage;
import com.example.bugsystem.controllers.requests.AddBugRequest;
import com.example.bugsystem.dtos.BugDto;
import com.example.bugsystem.model.Role;
import com.example.bugsystem.model.Status;
import com.example.bugsystem.servicies.EntitiesService;
import com.example.bugsystem.servicies.exception.ServiceException;
import com.example.bugsystem.validators.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bugs")
public class BugController {
    private final EntitiesService entitiesService;

    public BugController(EntitiesService entitiesService) {
        this.entitiesService = entitiesService;
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('TESTER')")
    public ResponseEntity<BugDto> addBug(@RequestBody AddBugRequest addBugRequest) {
        String testerUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        BugDto savedBug = entitiesService.addBug(addBugRequest.getName(), addBugRequest.getDescription(), addBugRequest.getPriority(), testerUsername);
        return ResponseEntity.ok(savedBug);

    }

    @GetMapping
    @PreAuthorize("hasAuthority('TESTER') OR hasAuthority('PROGRAMMER')")
    public ResponseEntity<List<BugDto>> findAllBugs(
            @RequestParam(value = "status", required = false) Status status
    ) {
        List<BugDto> requestedBugs = findBugsBasedOnStatusAndUserRequest(status);
        if (requestedBugs == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok()
                .body(requestedBugs);
    }

    @PutMapping("/unsolved/{bugId}")
    @PreAuthorize("hasAuthority('PROGRAMMER')")
    public ResponseEntity<BugDto> assignBugToProgrammer(@PathVariable int bugId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        BugDto savedBug = entitiesService.assignBugToProgrammer(username, bugId);
        return ResponseEntity.ok().body(savedBug);
    }
    @PreAuthorize("hasAuthority('PROGRAMMER')")
    @PutMapping("in-progress/{bugId}")
    public ResponseEntity<BugDto> markBugAsSolved(@PathVariable int bugId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(bugId);
        System.out.println(username);
        BugDto solvedBug = entitiesService.markBugAsSolved(username, bugId);
        return ResponseEntity.ok(solvedBug);
    }

    @PreAuthorize("hasAuthority('TESTER')")
    @DeleteMapping("/{bugId}")
    public void deleteBug(@PathVariable int bugId) {
        System.out.println("Here");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        entitiesService.deletePostedBug(bugId, username);
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorMessage> validationErrorHandler(ValidationException validationException) {
        ErrorMessage errorMessage = new ErrorMessage(validationException.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(errorMessage);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFound(EntityNotFoundException entityNotFoundException) {
        ErrorMessage errorMessage = new ErrorMessage(entityNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);

    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorMessage> serviceErrorHandler(ServiceException serviceException) {
        ErrorMessage errorMessage = new ErrorMessage(serviceException.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorMessage);
    }

    private List<BugDto> findBugsBasedOnStatusAndUserRequest(Status status) {
        if (status != null) {
            return entitiesService.findAllBugsWithStatus(status);
        }

        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        if (authenticatedUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.PROGRAMMER.toString()))) {
            return entitiesService.getAssignedBugsOfAProgrammer(authenticatedUser.getName());
        }

        return entitiesService.findAllBugs();
    }
}
