package ar.edu.itba.paw.webapp.controller.mappers;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(1)
public class JsonParseExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<JsonParseException> {
    @Override
    public Response toResponse(JsonParseException e) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseEntity re = getExceptionResponseEntity(e, status, e.getMessage());
        return this.errorResponse(status.value(), re, e);
    }
}
