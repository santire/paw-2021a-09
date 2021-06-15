package ar.edu.itba.paw.webapp.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.itba.paw.service.UserService;

public class EmailInUseValidator implements ConstraintValidator<EmailInUse, String> {

    @Autowired
    UserService userService;

    @Override
    public void initialize(EmailInUse constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return userService.findByEmail(email).isPresent();
    }

}
