package ar.edu.itba.paw.webapp.controller.mappers;

import ar.edu.itba.paw.model.exceptions.UsernameInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UsernameInUseExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<UsernameInUseException> {

    @Override
    public Response toResponse(UsernameInUseException e) {
        final HttpStatus status = HttpStatus.CONFLICT;
        ResponseEntity re = getExceptionResponseEntity(e, status, e.getMessage());
        return this.errorResponse(status.value(), re, e);
    }
}
