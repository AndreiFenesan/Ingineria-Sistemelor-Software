package com.example.bugsystem.controllers;

import com.example.bugsystem.controllers.errors.ErrorMessage;
import com.example.bugsystem.controllers.requests.AuthenticationRequest;
import com.example.bugsystem.controllers.requests.RegisterRequest;
import com.example.bugsystem.servicies.EntitiesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
    private final EntitiesService entitiesService;

    public AuthenticationController(EntitiesService entitiesService) {
        this.entitiesService = entitiesService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(entitiesService.register(registerRequest));
    }

    @PostMapping("/session")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse response = entitiesService.authenticateUser(authenticationRequest);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> usernameNotFound(Exception e) {
        ErrorMessage errorMessage = new ErrorMessage("Invalid credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errorMessage);
    }

}
