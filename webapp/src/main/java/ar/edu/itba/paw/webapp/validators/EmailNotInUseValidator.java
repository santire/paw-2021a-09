package ar.edu.itba.paw.webapp.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.itba.paw.service.UserService;

public class EmailNotInUseValidator implements ConstraintValidator<EmailNotInUse, String> {

    @Autowired
    UserService userService;

    @Override
    public void initialize(EmailNotInUse constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return true;
        }
        return !userService.findByEmail(email).isPresent();
    }

}
