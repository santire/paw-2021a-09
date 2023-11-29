package ar.edu.itba.paw.webapp.controller.mappers;

import org.glassfish.jersey.server.ParamException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class QueryParamExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<ParamException.QueryParamException> {

    @Override
    public Response toResponse(ParamException.QueryParamException e) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;

        ResponseEntity re = getExceptionResponseEntity(e, status, String.format("Query param %s has an invalid value", e.getParameterName()));
        return this.errorResponse(status.value(), re, e);
    }
}