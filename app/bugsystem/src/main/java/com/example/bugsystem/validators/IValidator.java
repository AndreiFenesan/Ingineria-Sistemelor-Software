package com.example.bugsystem.validators;

public interface IValidator<T> {
    public void validate(T entity) throws ValidationException;
}
