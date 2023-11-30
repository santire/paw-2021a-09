package ar.edu.itba.paw.webapp.controller.mappers;

import ar.edu.itba.paw.model.exceptions.AlreadyRatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AlreadyRatedExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<AlreadyRatedException> {

    @Override
    public Response toResponse(AlreadyRatedException e) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseEntity re = getExceptionResponseEntity(e, status, "Only one rating per restaurant is allowed. Update or delete previous rating.");
        return this.errorResponse(status.value(), re, e);
    }
}