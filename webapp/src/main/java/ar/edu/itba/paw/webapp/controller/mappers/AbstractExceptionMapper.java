package ar.edu.itba.paw.webapp.controller.mappers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractExceptionMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExceptionMapper.class);
    public static final String ACCESS_DENIED = "Access denied!";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String LIST_JOIN_DELIMITER = ",";
    private static final String ERRORS = "errors";
    private static final String ERROR = "errors";
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String TYPE = "type";

    protected Response errorResponse(int status, ResponseEntity responseEntity) {
        return customizeResponse(status, responseEntity);
    }

    protected Response errorResponse(int status, ResponseEntity responseEntity, Throwable t) {
//        LOGGER.error("Error:", t); // logging stack trace.

        return customizeResponse(status, responseEntity);
    }

    protected ResponseEntity<Object> getExceptionResponseEntity(final Exception exception,
                                                              final HttpStatus status,
                                                              final List<String> errors) {
        final Map<String, Object> body = generalResponseEntityBody(exception, status);
        body.put(ERRORS, errors);

        final String errorsMessage = !errors.isEmpty() ?
                errors.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(LIST_JOIN_DELIMITER))
                : status.getReasonPhrase();
        LOGGER.error("ERRORS: {}", errorsMessage);
        return new ResponseEntity<>(body, status);
    }
    protected ResponseEntity<Object> getExceptionResponseEntity(final Exception exception,
                                                                final HttpStatus status,
                                                                final String error) {
        final Map<String, Object> body = generalResponseEntityBody(exception, status);
        body.put(ERROR, error);

        final String errorsMessage = !error.isEmpty() ?
                error: status.getReasonPhrase();
        LOGGER.error("ERROR: {}", errorsMessage);
        return new ResponseEntity<>(body, status);
    }

    private Map<String, Object> generalResponseEntityBody(final Exception exception, final HttpStatus status) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put(STATUS, status.value());
        body.put(TYPE, exception.getClass().getSimpleName());
        body.put(MESSAGE, getMessageForStatus(status));
        return body;
    }

    private String getMessageForStatus(HttpStatus status) {
        switch (status) {
            case UNAUTHORIZED:
                return ACCESS_DENIED;
            case BAD_REQUEST:
                return INVALID_REQUEST;
            default:
                return status.getReasonPhrase();
        }
    }

    private Response customizeResponse(int status, ResponseEntity responseEntity) {
        return Response
                .status(status)
                .entity(responseEntity.getBody())
                .build();
    }
}
