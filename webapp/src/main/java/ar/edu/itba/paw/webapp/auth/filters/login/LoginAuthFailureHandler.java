package ar.edu.itba.paw.webapp.auth.filters.login;

import ar.edu.itba.paw.model.exceptions.AlreadyLoggedException;
import ar.edu.itba.paw.model.exceptions.InvalidLoginException;
import ar.edu.itba.paw.webapp.dto.LoginDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginAuthFailureHandler implements AuthenticationFailureHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthFailureHandler.class);
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException e) {
        if (e instanceof AlreadyLoggedException) {
            LOGGER.info("the user is already logged to the site, no authentication is needed");
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        }
        else if (e instanceof InvalidLoginException) {
            LOGGER.info("the login was not valid");
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        else if (e instanceof BadCredentialsException) {
            LOGGER.info("bad credentials");
            LoginDto loginDto = (LoginDto) httpServletRequest.getAttribute("loginRequest");
            if (loginDto.getUsername() == null || loginDto.getPassword() == null) {
                LOGGER.info("the credentials introduced by the user are not correct. there is an error either on password or on username");
                httpServletResponse.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            }
            else {
                LOGGER.info("either the json for login was not valid or there was no jwt token.");
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        }
    }
}
