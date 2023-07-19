package ar.edu.itba.paw.webapp.controller.mappers;

import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import ar.edu.itba.paw.model.exceptions.TokenDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TokenDoesNotExistExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<TokenDoesNotExistException> {

    @Override
    public Response toResponse(TokenDoesNotExistException e) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseEntity re = getExceptionResponseEntity(e, status, "Token does not exist");
        return this.errorResponse(status.value(), re, e);
    }
}