package ar.edu.itba.paw.webapp.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
@Component
public class ApiEntryPoint implements AuthenticationEntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException authException) throws IOException, ServletException {
        LOGGER.error("Unauthorized error: {}", authException.getMessage());
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write("{\"message\": \"Unauthorized\",\"status\": \"401\"}");
    }

}
