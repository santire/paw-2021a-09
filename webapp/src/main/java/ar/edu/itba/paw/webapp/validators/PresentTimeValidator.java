package ar.edu.itba.paw.webapp.validators;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

public class PresentTimeValidator implements ConstraintValidator<PresentTime, Object> {

    private String timeFieldName;
    private String dateFieldName;
    private int daysLimit;
    private String timeMessage;
    private String dateMessageAfter;
    private String dateMessageBefore;

    @Override
    public void initialize(PresentTime constraintAnnotation) {
        timeFieldName = constraintAnnotation.time();
        dateFieldName = constraintAnnotation.date();
        daysLimit = constraintAnnotation.daysLimit();
        timeMessage = constraintAnnotation.timeMessage();
        dateMessageAfter = constraintAnnotation.dateMessageAfter();
        dateMessageBefore = constraintAnnotation.dateMessageBefore();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        final LocalTime time = (LocalTime) new BeanWrapperImpl(value).getPropertyValue(timeFieldName);
        final LocalDate date = (LocalDate) new BeanWrapperImpl(value).getPropertyValue(dateFieldName);

        final LocalTime currentTime = LocalTime.now();
        final LocalDate currentDate = LocalDate.now();

        if (currentDate.isAfter(date)) {
            buildViolation(context, dateMessageBefore, dateFieldName);
            return false;
        }

        if (currentDate.plusDays(daysLimit).isBefore(date)) {
            buildViolation(context, dateMessageAfter, dateFieldName);
            return false;
        }

        if (currentDate.equals(date) && currentTime.isAfter(time)) {
            buildViolation(context, timeMessage, timeFieldName);
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
