package com.example.bugsystem.servicies;

import com.example.bugsystem.controllers.requests.AuthenticationRequest;
import com.example.bugsystem.controllers.AuthenticationResponse;
import com.example.bugsystem.controllers.requests.RegisterRequest;
import com.example.bugsystem.dtos.BugDto;
import com.example.bugsystem.model.*;
import com.example.bugsystem.repositories.IBugRepository;
import com.example.bugsystem.repositories.IProgrammerRepository;
import com.example.bugsystem.repositories.ITesterRepository;
import com.example.bugsystem.servicies.exception.ServiceException;
import com.example.bugsystem.validators.IValidator;
import com.example.bugsystem.validators.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EntitiesService {
    private final ITesterRepository testerRepository;
    private final IProgrammerRepository programmerRepository;
    private final IValidator<Bug> bugValidator;
    private final PasswordEncoder passwordEncoder;
    private final IBugRepository bugRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public EntitiesService(ITesterRepository testerRepository, IProgrammerRepository programmerRepository, IBugRepository bugRepository,
                           PasswordEncoder passwordEncoder, JwtService jwtService,
                           AuthenticationManager authenticationManager, IValidator<Bug> bugValidator) {
        this.testerRepository = testerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.programmerRepository = programmerRepository;
        this.bugRepository = bugRepository;
        this.bugValidator = bugValidator;
    }

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if (registerRequest.getRole().equals(Role.TESTER)) {
            Tester tester = new Tester(
                    registerRequest.getFirstname(),
                    registerRequest.getLastname(),
                    registerRequest.getUsername(),
                    registerRequest.getEmail(),
                    passwordEncoder.encode(registerRequest.getPassword()),
                    new Date(),
                    Role.TESTER
            );
            testerRepository.save(tester);
            var jwt = jwtService.generateJwt(new HashMap<>(), tester);
            return new AuthenticationResponse(jwt, Role.TESTER);
        } else {
            Programmer programmer = new Programmer(
                    registerRequest.getFirstname(),
                    registerRequest.getLastname(),
                    registerRequest.getUsername(),
                    registerRequest.getEmail(),
                    passwordEncoder.encode(registerRequest.getPassword()),
                    new Date(),
                    Role.PROGRAMMER
            );
            programmerRepository.save(programmer);
            var jwt = jwtService.generateJwt(new HashMap<>(), programmer);
            return new AuthenticationResponse(jwt, Role.PROGRAMMER);
        }

    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        ));

        Optional<Tester> tester = testerRepository.findTesterByUsername(authenticationRequest.getUsername());
        if (tester.isPresent()) {
            String jwt = jwtService.generateJwt(new HashMap<>(), tester.get());
            return new AuthenticationResponse(jwt, Role.TESTER);
        }
        Optional<Programmer> programmer = programmerRepository.findProgrammerByUsername(authenticationRequest.getUsername());
        if (programmer.isPresent()) {
            String jwt = jwtService.generateJwt(new HashMap<>(), programmer.get());
            return new AuthenticationResponse(jwt, Role.PROGRAMMER);
        }

        throw new UsernameNotFoundException("Username not found");
    }

    public BugDto addBug(String name, String description, Priority priority, String testerUsername) throws ValidationException {
        Tester bugPoster = getTesterIfExists(testerUsername);
        Bug bug = new Bug(
                description,
                name,
                priority,
                Status.UNSOLVED,
                null,
                new Date(),
                bugPoster
        );
        bugValidator.validate(bug);
        Bug savedBug = bugRepository.save(bug);
        return new BugDto(savedBug);
    }

    public List<BugDto> findAllBugs() {
        List<Bug> allBugs = this.bugRepository.findAll();
        return allBugs.stream().map(BugDto::new).collect(Collectors.toList());
    }

    public BugDto assignBugToProgrammer(String programmerUsername, int bugId) {
        Programmer programmer = getProgrammerIfExists(programmerUsername);

        Optional<Bug> optionalBug = bugRepository.findById(bugId);
        if (optionalBug.isEmpty()) {
            throw new EntityNotFoundException("There is no bug with this id");
        }
        Bug bug = optionalBug.get();
        if (bug.getBugSolver() != null) {
            throw new ServiceException("This bug was already assign to a programmer");
        }
        if (bug.getStatus() != Status.UNSOLVED) {
            throw new ServiceException("The bug must have unsolved status");
        }


        bug.setStatus(Status.IN_PROGRESS);
        bug.setBugSolver(programmer);
        Bug savedBug = bugRepository.save(bug);
        return new BugDto(savedBug);
    }

    public BugDto markBugAsSolved(String programmerUsername, int bugId) {
        Programmer programmer = getProgrammerIfExists(programmerUsername);

        Optional<Bug> optionalBug = bugRepository.findById(bugId);
        if (optionalBug.isEmpty()) {
            throw new EntityNotFoundException("There is no bug with this id");
        }
        Bug bug = optionalBug.get();


        if (!bug.getBugSolver().equals(programmer)) {
            throw new ServiceException("Cannot mark as solved a bug that is not yours");
        }
        bug.setStatus(Status.SOLVED);
        bug.setDateOfSolving(new Date());
        Bug solvedBug = bugRepository.save(bug);
        return new BugDto(solvedBug);
    }

    @Transactional
    public void deletePostedBug(int bugId, String testerUsername) {
        Tester tester = getTesterIfExists(testerUsername);
        int deletedBugs = bugRepository.deleteBugByIdAndBugPoster(bugId, tester);
        if (deletedBugs == 0) {
            throw new EntityNotFoundException("No bug with this id or the bug was not added by you");
        }
    }

    public List<BugDto> findAllBugsWithStatus(Status status) {
        return bugRepository.findBugsByStatusIs(status).
                stream().
                map(BugDto::new).
                collect(Collectors.toList());
    }

    public List<BugDto> getAssignedBugsOfAProgrammer(String programmerUsername) throws EntityNotFoundException {
        Programmer programmer = getProgrammerIfExists(programmerUsername);
        return bugRepository.findBugsByStatusIsAndBugSolverIs(Status.IN_PROGRESS, programmer)
                .stream().map(BugDto::new).toList();
    }

    private Programmer getProgrammerIfExists(String programmerUsername) throws EntityNotFoundException {
        Optional<Programmer> optionalProgrammer = programmerRepository.findProgrammerByUsername(programmerUsername);
        return optionalProgrammer.
                orElseThrow(() -> new EntityNotFoundException("There is no programmer with this username"));
    }

    private Tester getTesterIfExists(String testerUsername) throws EntityNotFoundException {
        Optional<Tester> optionalTester = testerRepository.findTesterByUsername(testerUsername);
        return optionalTester.
                orElseThrow(() -> new EntityNotFoundException("There is no tester with this username"));
    }

}
