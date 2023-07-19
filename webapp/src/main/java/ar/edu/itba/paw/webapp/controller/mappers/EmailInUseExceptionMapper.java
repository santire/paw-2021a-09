package ar.edu.itba.paw.webapp.controller.mappers;

import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EmailInUseExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<EmailInUseException> {

    @Override
    public Response toResponse(EmailInUseException e) {
        final HttpStatus status = HttpStatus.CONFLICT;
        ResponseEntity re = getExceptionResponseEntity(e, status, e.getMessage());
        return this.errorResponse(status.value(), re, e);
    }
}
