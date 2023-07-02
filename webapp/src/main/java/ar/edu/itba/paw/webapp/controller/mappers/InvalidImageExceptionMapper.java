package ar.edu.itba.paw.webapp.controller.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ar.edu.itba.paw.model.exceptions.InvalidImageException;

@Provider
public class InvalidImageExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<InvalidImageException> {
    @Override
    public Response toResponse(InvalidImageException e) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = e.getMessage(); // Get the exception message
        ResponseEntity re = getExceptionResponseEntity(e, status, message);
        return this.errorResponse(status.value(), re, e);
    }
}
