package ar.edu.itba.paw.webapp.controller.mappers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper  extends AbstractExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException e) {
        final HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "Resource does not exist";
        ResponseEntity re = getExceptionResponseEntity(e, status, message);
        return this.errorResponse(status.value(), re, e);
    }
}
