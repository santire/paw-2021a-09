package ar.edu.itba.paw.webapp.controller.mappers;

import ar.edu.itba.paw.model.exceptions.RatingNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RatingNotFoundExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<RatingNotFoundException> {

    @Override
    public Response toResponse(RatingNotFoundException e) {
        final HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "Rating does not exist";
        ResponseEntity re = getExceptionResponseEntity(e, status, message);
        return this.errorResponse(status.value(), re, e);
    }
}
