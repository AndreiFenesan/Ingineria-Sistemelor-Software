package com.example.bugsystem.controllers;

import com.example.bugsystem.model.Role;

public class AuthenticationResponse {
    private String token;
    private Role role;

    public AuthenticationResponse(String token, Role role) {
        this.token = token;
        this.role = role;
    }

    public AuthenticationResponse() {
    }

    public String getToken() {
        return token;
    }

    public Role getRole() {
        return role;
    }
}
