package ar.edu.itba.paw.webapp.controller.mappers;

import ar.edu.itba.paw.model.exceptions.LikeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LikeNotFoundExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<LikeNotFoundException> {

    @Override
    public Response toResponse(LikeNotFoundException e) {
        final HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "Like does not exist";
        ResponseEntity re = getExceptionResponseEntity(e, status, message);
        return this.errorResponse(status.value(), re, e);
    }
}
