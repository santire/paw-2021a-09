package ar.edu.itba.paw.webapp.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidTimeValidator.class})
public @interface ValidTime {
    String message() default "Not a valid time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int minHour();
    int maxHour();
    int stepMinutes();

}
