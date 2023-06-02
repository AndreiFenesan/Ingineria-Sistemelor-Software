package com.example.bugsystem.controllers.errors;

public class ErrorMessage {
    private String description;

    public ErrorMessage(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
