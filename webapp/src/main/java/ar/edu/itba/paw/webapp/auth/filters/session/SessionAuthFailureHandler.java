package ar.edu.itba.paw.webapp.auth.filters.session;

import ar.edu.itba.paw.webapp.auth.filters.login.LoginAuthSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

public class SessionAuthFailureHandler implements AuthenticationFailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionAuthFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException e) {
        LOGGER.info("There was an error during the auth process. AUTHENTICATION FAILURE");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        LOGGER.info("STATUS was set to UNAUTHORIZED");
        httpServletResponse.setContentType(MediaType.TEXT_HTML);
        httpServletResponse.setContentLength(0);
        LOGGER.info("RESPONSE HAS NO CONTENT");
    }
}
