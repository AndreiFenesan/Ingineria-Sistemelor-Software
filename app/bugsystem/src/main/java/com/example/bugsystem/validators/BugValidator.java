package com.example.bugsystem.validators;

import com.example.bugsystem.model.Bug;
import com.example.bugsystem.model.Priority;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class BugValidator implements IValidator<Bug> {

    private boolean isNullOrEmpty(String string) {
        if (string == null) {
            return true;
        }
        return string.isEmpty();
    }

    private boolean isValidPriority(Priority priority) {
        if (priority == null) {
            return false;
        }
        Optional<Priority> optionalPriority =
                Arrays.stream(Priority.values()).filter((p) -> p.equals(priority)).findFirst();
        return optionalPriority.isPresent();
    }

    @Override
    public void validate(Bug entity) throws ValidationException {
        if (entity == null) {
            throw new ValidationException("Incorrect entity");
        }
        if (isNullOrEmpty(entity.getName())) {
            throw new ValidationException("Name is empty");
        }
        if (isNullOrEmpty(entity.getDescription())) {
            throw new ValidationException("Description is empty");
        }
        if (!isValidPriority(entity.getPriority())) {
            throw new ValidationException("Invalid priority");
        }
    }
}
