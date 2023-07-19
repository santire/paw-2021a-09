package ar.edu.itba.paw.webapp.controller.mappers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collections;

@Provider
public class GeneralExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        ResponseStatus responseStatus =
                e.getClass().getAnnotation(ResponseStatus.class);
        final HttpStatus status = responseStatus != null ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
        final String localizedMessage = e.getLocalizedMessage();
        String message = (localizedMessage != null && !localizedMessage.isEmpty() ? localizedMessage : status.getReasonPhrase());

        // ResponseEntity class's Member Integer code, String message, Object data. For response format.
        ResponseEntity re = getExceptionResponseEntity(e, status, message);

        return this.errorResponse(status.value(), re, e);
    }
}
