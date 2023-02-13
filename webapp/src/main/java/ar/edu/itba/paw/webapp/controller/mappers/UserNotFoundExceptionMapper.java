package ar.edu.itba.paw.webapp.controller.mappers;

import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UserNotFoundExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<UserNotFoundException> {

    @Override
    public Response toResponse(UserNotFoundException e) {
        final HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "User does not exist";
        ResponseEntity re = getExceptionResponseEntity(e, status, message);
        return this.errorResponse(status.value(), re, e);
    }
}
