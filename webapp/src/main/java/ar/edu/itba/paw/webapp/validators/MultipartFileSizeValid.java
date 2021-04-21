package ar.edu.itba.paw.webapp.validators;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultipartFileSizeValidator.class)
public @interface MultipartFileSizeValid {


    String message() default "DEFAULT_ERROR_MESSAGE";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
