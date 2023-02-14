package ar.edu.itba.paw.webapp.controller.mappers;

import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TokenCreationExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<TokenCreationException> {

    @Override
    public Response toResponse(TokenCreationException e) {
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseEntity re = getExceptionResponseEntity(e, status, "Error creating token");
        return this.errorResponse(status.value(), re, e);
    }
}