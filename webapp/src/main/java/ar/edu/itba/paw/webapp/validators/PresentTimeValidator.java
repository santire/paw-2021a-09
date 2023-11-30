package ar.edu.itba.paw.webapp.validators;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

public class PresentTimeValidator implements ConstraintValidator<PresentTime, Object> {

    private String dateFieldName;
    private int daysLimit;
    private String dateMessageAfter;
    private String dateMessageBefore;

    @Override
    public void initialize(PresentTime constraintAnnotation) {
        dateFieldName = constraintAnnotation.date();
        daysLimit = constraintAnnotation.daysLimit();
        dateMessageAfter = constraintAnnotation.dateMessageAfter();
        dateMessageBefore = constraintAnnotation.dateMessageBefore();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        final LocalDateTime date = (LocalDateTime) new BeanWrapperImpl(value).getPropertyValue(dateFieldName);

        if(date == null) {
            buildViolation(context, "Missing date", dateFieldName);
            return false;
        }

        final LocalDateTime currentDate = LocalDateTime.now();

        if (currentDate.isAfter(date)) {
            buildViolation(context, dateMessageBefore, dateFieldName);
            return false;
        }

        if (currentDate.plusDays(daysLimit).isBefore(date)) {
            buildViolation(context, dateMessageAfter, dateFieldName);
            return false;
        }

        return true;
    }

    private void buildViolation(ConstraintValidatorContext context, String message, String node) {
            context.buildConstraintViolationWithTemplate(message)
                .addNode(node)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }

}
