package ar.edu.itba.paw.webapp.controller.mappers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collections;


@Provider
public class AccessDeniedExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    @Override
    public Response toResponse(AccessDeniedException e) {
        final HttpStatus status = HttpStatus.FORBIDDEN;
        final String localizedMessage = e.getLocalizedMessage();
        String message = (localizedMessage != null && !localizedMessage.isEmpty() ? localizedMessage : status.getReasonPhrase());
        ResponseEntity re = getExceptionResponseEntity(e, status, message);
        return this.errorResponse(status.value(), re, e);
    }
}
