package ar.edu.itba.paw.webapp.controller.mappers;

import ar.edu.itba.paw.model.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TokenExpiredExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<TokenExpiredException> {

    @Override
    public Response toResponse(TokenExpiredException e) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseEntity re = getExceptionResponseEntity(e, status, "Token is expired");
        return this.errorResponse(status.value(), re, e);
    }
}