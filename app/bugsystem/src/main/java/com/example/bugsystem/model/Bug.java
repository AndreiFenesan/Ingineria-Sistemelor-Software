package com.example.bugsystem.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Bug {
    @Id
    @GeneratedValue
    private Integer id;
    private String description;
    private String name;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Date dateOfSolving;
    private Date postDate;
    @ManyToOne
    @JoinColumn(name = "programmer_id")
    private Programmer bugSolver;
    @ManyToOne
    @JoinColumn(name = "tester_id")
    private Tester bugPoster;

    public Bug(String description, String name, Priority priority, Status status, Date dateOfSolving, Date postDate, Tester bugPoster) {
        this.description = description;
        this.name = name;
        this.priority = priority;
        this.status = status;
        this.dateOfSolving = dateOfSolving;
        this.postDate = postDate;
        this.bugSolver = null;
        this.bugPoster = bugPoster;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Programmer getBugSolver() {
        return bugSolver;
    }

    public void setBugSolver(Programmer bugSolver) {
        this.bugSolver = bugSolver;
    }

    public Bug() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDateOfSolving() {
        return dateOfSolving;
    }

    public void setDateOfSolving(Date dateOfSolving) {
        this.dateOfSolving = dateOfSolving;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Tester getBugPoster() {
        return bugPoster;
    }

    public void setBugPoster(Tester bugPoster) {
        this.bugPoster = bugPoster;
    }
}
