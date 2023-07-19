package ar.edu.itba.paw.webapp.controller.mappers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        final List<String> validationErrors = e.getConstraintViolations().stream().
                map(violation -> {
                            String[] violationArgs = violation.getPropertyPath().toString().split("\\.");
                            int violationLength = violationArgs.length;
                            return violationArgs[violationLength > 0 ? violationLength - 1 : 0] + ": " + violation.getMessage();
                        }
                )
                .collect(Collectors.toList());
        ResponseEntity re = getExceptionResponseEntity(e, status, validationErrors);
        return this.errorResponse(status.value(), re, e);
    }
}

