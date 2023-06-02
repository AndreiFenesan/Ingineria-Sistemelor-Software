package com.example.bugsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
public class Tester extends Employee {
    @Enumerated(EnumType.STRING)
    private Role role;

    public Tester(Integer id, String firstname, String lastname, String username, String email, String password, Date birthDate, Role role) {
        super(id, firstname, lastname, username, email, password, birthDate);
        this.role = role;
    }

    public Tester(String firstname, String lastname, String username, String email, String password, Date birthDate, Role role) {
        super(firstname, lastname, username, email, password, birthDate);
        this.role = role;
    }

    public Tester() {
        super();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
