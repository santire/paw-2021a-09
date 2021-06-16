package ar.edu.itba.paw.webapp.validators;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultipartFileSizeValidator implements ConstraintValidator<MultipartFileSizeValid, MultipartFile> {

    private static final String ERROR_MESSAGE = "File too Large.";

    private static final int MAX_SIZE = 2000000;

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(ERROR_MESSAGE)
            .addConstraintViolation();
        return multipartFile.getSize() < MAX_SIZE;
    }

    @Override
    public void initialize(MultipartFileSizeValid arg0) {
    }

}
