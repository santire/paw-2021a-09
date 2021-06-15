package ar.edu.itba.paw.webapp.validators;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class ValidTimeValidator implements ConstraintValidator<ValidTime, LocalTime> {

    private int minHour;
    private int maxHour;
    private int stepMinutes;

    @Override
    public void initialize(ValidTime constraintAnnotation) {
        minHour = constraintAnnotation.minHour();
        maxHour = constraintAnnotation.maxHour();
        stepMinutes = constraintAnnotation.stepMinutes();

    }

    @Override
    public boolean isValid(LocalTime time, ConstraintValidatorContext context) {
        List<LocalTime> availableHours = new ArrayList<>();
        for (int i = minHour; i <= maxHour; i++) {
            for (int j=0; j<60; j+=stepMinutes) {
                availableHours.add(LocalTime.parse(String.format("%02d", i) + ":" + String.format("%02d", j)));
            }
        }
        return availableHours.contains(time);
    }

}
