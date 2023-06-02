package com.example.bugsystem.model;

import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

@MappedSuperclass
public abstract class Employee implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    private Date birthDate;

    public Employee(String firstname, String lastname, String username, String email, String password, Date birthDate) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
    }

    public Employee(Integer id, String firstname, String lastname, String username, String email, String password, Date birthDate) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
    }

    public Employee() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstName) {
        this.firstname = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
