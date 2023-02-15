package ar.edu.itba.paw.webapp.controller.mappers;

import ar.edu.itba.paw.model.exceptions.ReservationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ReservationNotFoundExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<ReservationNotFoundException> {

    @Override
    public Response toResponse(ReservationNotFoundException e) {
        final HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "Reservation does not exist";
        ResponseEntity re = getExceptionResponseEntity(e, status, message);
        return this.errorResponse(status.value(), re, e);
    }
}
