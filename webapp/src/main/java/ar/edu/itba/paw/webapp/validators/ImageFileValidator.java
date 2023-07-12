package ar.edu.itba.paw.webapp.validators;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import ar.edu.itba.paw.model.exceptions.InvalidImageException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageFileValidator implements ConstraintValidator<ValidImage, FormDataBodyPart> {
    public int FILE_MAX_SIZE_KB = 400;

    @Override
    public void initialize(ValidImage constraintAnnotation) {

    }

    @Override
    public boolean isValid(FormDataBodyPart multipartFile, ConstraintValidatorContext context) {
        if (multipartFile != null) {
            String contentType = multipartFile.getMediaType().toString();
            if(!isSupportedContentType(contentType)) {
                throw new InvalidImageException("Invalid image type. Only PNG or JPG images are allowed.");
            }
            try {
                if(!isSupportedSize(multipartFile)){
                    throw new InvalidImageException("Invalid image size. It must be lower than " + FILE_MAX_SIZE_KB + " KB");
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        } else {
            throw new RuntimeException();
        }
        return true;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }

    private boolean isSupportedSize(FormDataBodyPart multipartFile) throws IOException {        
        InputStream inputStream = multipartFile.getEntityAs(InputStream.class);
        // Read the content of the input stream and calculate the size in bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] bodyBytes = outputStream.toByteArray();
        int bodySize = bodyBytes.length / 1024;
        System.out.println("THE SIZE OF THIS FILE IS => " + bodySize);
        
        return bodySize <= FILE_MAX_SIZE_KB;
    }
}
