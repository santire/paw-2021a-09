package ar.edu.itba.paw.webapp.controller.mappers;

import org.glassfish.jersey.server.ParamException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PathParamExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<ParamException.PathParamException> {

    @Override
    public Response toResponse(ParamException.PathParamException e) {
        final HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "Resource not found does not exist";
        ResponseEntity re = getExceptionResponseEntity(e, status, message);
        return this.errorResponse(status.value(), re, e);
    }
}
