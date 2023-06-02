package com.example.bugsystem.controllers;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
public class HelloController {
    @GetMapping("/sayHello")
    @PreAuthorize("hasAuthority('PROGRAMMER') OR hasAuthority('TESTER')")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello secured TESTER");
    }

    @GetMapping("/sayHello/prog")
    @PreAuthorize("hasRole('PROGRAMMER')")
    public ResponseEntity<String> sayHelloPrg() {
        return ResponseEntity.ok("Hello secured PROGRAMMER");
    }
}
