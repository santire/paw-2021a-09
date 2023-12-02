package ar.edu.itba.paw.webapp.auth.filters;

import ar.edu.itba.paw.webapp.utils.JwtTokenUtil;
import io.jsonwebtoken.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class BasicAuthenticationWithJwtHeaderFilter extends BasicAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicAuthenticationWithJwtHeaderFilter.class);
    private final JwtTokenUtil jwtUtility;
    private final UserDetailsService userDetailsService;


    @Autowired
    public BasicAuthenticationWithJwtHeaderFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint entryPoint, JwtTokenUtil jwtUtility, UserDetailsService userDetailsService) {
        super(authenticationManager, entryPoint);
        this.jwtUtility = jwtUtility;
        this.userDetailsService = userDetailsService;

    }


    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, Authentication authentication) throws IOException {
        LOGGER.info("The user with email {} has been successfully authenticated", authentication.getName());
        String jwt = jwtUtility.generateToken(userDetailsService.loadUserByUsername(authentication.getName()));
        response.addHeader("X-Auth-Token", "Bearer " + jwt);
    }
}
