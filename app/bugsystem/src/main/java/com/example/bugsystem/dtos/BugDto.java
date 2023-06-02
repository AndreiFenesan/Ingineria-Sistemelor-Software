package com.example.bugsystem.dtos;

import com.example.bugsystem.model.Bug;
import com.example.bugsystem.model.Priority;
import com.example.bugsystem.model.Status;

import java.util.Date;

public class BugDto {
    private Integer id;
    private String description;
    private String name;
    private Priority priority;
    private Status status;
    private Date dateOfSolving;
    private Date postDate;
    private String bugPosterUsername;

    public BugDto(Integer id, String description, String name, Priority priority, Status status, Date dateOfSolving, Date postDate) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.priority = priority;
        this.status = status;
        this.dateOfSolving = dateOfSolving;
        this.postDate = postDate;
    }

    public BugDto(Bug bug) {
        this.id = bug.getId();
        this.dateOfSolving = bug.getDateOfSolving();
        this.postDate = bug.getPostDate();
        this.name = bug.getName();
        this.status = bug.getStatus();
        this.priority = bug.getPriority();
        this.description = bug.getDescription();
        this.bugPosterUsername = bug.getBugPoster().getUsername();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getBugPosterUsername() {
        return bugPosterUsername;
    }

    public void setBugPosterUsername(String bugPosterUsername) {
        this.bugPosterUsername = bugPosterUsername;
    }
}
