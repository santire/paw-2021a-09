package ar.edu.itba.paw.webapp.controller.mappers;

import ar.edu.itba.paw.model.exceptions.AlreadyLikedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class AlreadyLikedExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<AlreadyLikedException> {

    @Override
    public Response toResponse(AlreadyLikedException e) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseEntity re = getExceptionResponseEntity(e, status, "Can't like an already liked restaurant");
        return this.errorResponse(status.value(), re, e);
    }
}