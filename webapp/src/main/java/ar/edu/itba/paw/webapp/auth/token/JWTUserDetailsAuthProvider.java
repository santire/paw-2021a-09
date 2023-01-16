package ar.edu.itba.paw.webapp.auth.token;

import ar.edu.itba.paw.model.exceptions.MalformedTokenException;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JWTUserDetailsAuthProvider extends AbstractUserDetailsAuthenticationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTUserDetailsAuthProvider.class);

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private PawUserDetailsService pawUserDetailsService;

    @Override
    public boolean supports(Class<?> authentication) {
        return (JWTUsernamePasswordAuthToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails,
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
    ) throws AuthenticationException {
        if (!(usernamePasswordAuthenticationToken instanceof JWTUsernamePasswordAuthToken)) {
            throw new MalformedTokenException("Authentication was rejected because there is not a JWTUsernamePasswordAuthToken. AUTH REJECTION");
        }
    }

    @Override
    protected UserDetails retrieveUser(String s,
                                       UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException {
        JWTUsernamePasswordAuthToken jwtUsernamePasswordAuthToken =
                ((JWTUsernamePasswordAuthToken) usernamePasswordAuthenticationToken);
        String jwtToken = jwtUsernamePasswordAuthToken.getToken();

        Claims tokenClaims = jwtUtility.validateTokenString(jwtToken);
        String username = tokenClaims == null ? null : tokenClaims.getSubject();
        if (username == null) {
            throw new MalformedTokenException("The TOKEN username is not present");
        }
        LOGGER.info("Username from token is: " + username);
        return pawUserDetailsService.loadUserByUsername(username);
    }
}
