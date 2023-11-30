package ar.edu.itba.paw.webapp.controller.mappers;


import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

@Provider
@Priority(1)
public class JsonMappingExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<JsonMappingException> {
    @Override
    public Response toResponse(JsonMappingException e) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        final List<String> mappingErrors = e.getPath().stream()
                .map(violation -> String.format("%s: Invalid value", violation.getFieldName()))
                .collect(Collectors.toList());
        ResponseEntity re = getExceptionResponseEntity(e, status, mappingErrors);
        return this.errorResponse(status.value(), re, e);
    }
}
