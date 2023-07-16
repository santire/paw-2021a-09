package ar.edu.itba.paw.webapp.controller.mappers;

import ar.edu.itba.paw.model.exceptions.NotLikedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotLikedExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<NotLikedException> {

    @Override
    public Response toResponse(NotLikedException e) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseEntity re = getExceptionResponseEntity(e, status, "Can't dislike a unliked restaurant");
        return this.errorResponse(status.value(), re, e);
    }
}