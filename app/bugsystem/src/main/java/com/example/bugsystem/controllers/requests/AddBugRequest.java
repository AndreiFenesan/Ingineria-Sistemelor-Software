package com.example.bugsystem.controllers.requests;

import com.example.bugsystem.model.Priority;

public class AddBugRequest {
    private String name;
    private String description;
    private Priority priority;

    public AddBugRequest(String name, String description, Priority priority) {
        this.name = name;
        this.description = description;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
