package ar.edu.itba.paw.webapp.controller.mappers;

import ar.edu.itba.paw.model.exceptions.EmptyBodyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EmptyBodyExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<EmptyBodyException> {

    @Override
    public Response toResponse(EmptyBodyException e) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "Request body is missing";
        ResponseEntity re = getExceptionResponseEntity(e, status, message);
        return this.errorResponse(status.value(), re, e);
    }
}
