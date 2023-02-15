package ar.edu.itba.paw.webapp.controller.mappers;
import ar.edu.itba.paw.model.exceptions.ReservationNotFoundException;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestaurantNotFoundExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<RestaurantNotFoundException> {

    @Override
    public Response toResponse(RestaurantNotFoundException e) {
        final HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "Restaurant does not exist";
        ResponseEntity re = getExceptionResponseEntity(e, status, message);
        return this.errorResponse(status.value(), re, e);
    }
}
