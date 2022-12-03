package ar.edu.itba.paw.webapp.auth.filters.login;

import ar.edu.itba.paw.model.exceptions.AlreadyLoggedException;
import ar.edu.itba.paw.model.exceptions.InvalidLoginException;
import ar.edu.itba.paw.webapp.dto.LoginDto;
import ar.edu.itba.paw.webapp.utils.JSONUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginAuthFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthFilter.class);

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (isLogged()) {
            throw new AlreadyLoggedException(getUserName() + "The user has been already logged in");
        }
        LoginDto loginDto;
        try {
            loginDto = obtainLoginDto(request);
            request.setAttribute("loginRequest", loginDto);
        } catch (IOException e) {
            throw new InvalidLoginException("An error occurred processing the login request", e);
        }
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                        loginDto.getPassword());
        this.setDetails(request, token);
        return this.getAuthenticationManager().authenticate(token);
    }

    private LoginDto obtainLoginDto(HttpServletRequest request) throws IOException {
        String json = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        return JSONUtils.jsonToObject(json, LoginDto.class);
    }

    public boolean isLogged() {
        Authentication authentication = getAuth();
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public String getUserName() {
        final Authentication authentication = getAuth();
        if (authentication == null || !isLogged()) {
            return null;
        }
        return authentication.getName();
    }

    private Authentication getAuth() {
        Authentication authentication = null;
        SecurityContext secCont = SecurityContextHolder.getContext();
        if (secCont != null) {
            authentication = secCont.getAuthentication();
        }
        return authentication;
    }

}
