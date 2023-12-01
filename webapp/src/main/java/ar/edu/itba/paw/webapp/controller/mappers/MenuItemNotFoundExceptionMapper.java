package ar.edu.itba.paw.webapp.controller.mappers;

import ar.edu.itba.paw.model.exceptions.MenuItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MenuItemNotFoundExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<MenuItemNotFoundException> {

    @Override
    public Response toResponse(MenuItemNotFoundException e) {
        final HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "Menu item does not exist";
        ResponseEntity re = getExceptionResponseEntity(e, status, message);
        return this.errorResponse(status.value(), re, e);
    }
}
