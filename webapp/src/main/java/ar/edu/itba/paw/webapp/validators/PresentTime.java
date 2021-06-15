package ar.edu.itba.paw.webapp.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PresentTimeValidator.class})
public @interface PresentTime {

    String message() default "Invalid date/time selection";
    String timeMessage() default "Selected time has already passed";
    String dateMessageBefore() default "Selected date has already passed";
    String dateMessageAfter() default "Selected date is too far in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    String time();

    String date();

    int daysLimit();
}
