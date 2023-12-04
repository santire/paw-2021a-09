package ar.edu.itba.paw.webapp.auth.filters;

import ar.edu.itba.paw.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class BasicAuthenticationWithJwtHeaderFilter extends BasicAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicAuthenticationWithJwtHeaderFilter.class);
    private final UserDetailsService userDetailsService;

    private final JwtService tokenService;


    @Autowired
    public BasicAuthenticationWithJwtHeaderFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint entryPoint, UserDetailsService userDetailsService, JwtService tokenService) {
        super(authenticationManager, entryPoint);
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;

    }


    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, Authentication authentication) throws IOException {
        LOGGER.info("The user with email {} has been successfully authenticated", authentication.getName());
       Map<String, String> tokens = tokenService.generateTokens(userDetailsService.loadUserByUsername(authentication.getName()));
        response.addHeader("X-Auth-Token", "Bearer " + tokens.get("access_token"));
        response.addHeader("X-Refresh-Token", "Bearer " + tokens.get("refresh_token"));
    }
}
