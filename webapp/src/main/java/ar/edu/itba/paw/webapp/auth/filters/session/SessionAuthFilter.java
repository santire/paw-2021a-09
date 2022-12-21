package ar.edu.itba.paw.webapp.auth.filters.session;

import ar.edu.itba.paw.model.exceptions.NoJWTFoundException;
import ar.edu.itba.paw.model.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.auth.token.JWTUsernamePasswordAuthToken;
import ar.edu.itba.paw.webapp.auth.token.JWTUtility;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;

public class SessionAuthFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionAuthFilter.class);

    private static final String AUTH_BEARER_TYPE = "Bearer ";
    private static final String DEFAULT_FILTER   = "/**";

    private static final RequestMatcher logOutEndpointMatcher
            = new AntPathRequestMatcher("/api/logout", HttpMethod.POST);

    private static final String RESTAURANT_OWNER = "ROLE_RESTAURANTOWNER";
    private static final String USER = "ROLE_USER";

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private RequestMatcher needAuthEndpointsMatcher;

    @Autowired
    private RequestMatcher optionalAuthEndpointsMatcher;

//    @Autowired
//    private RequestMatcher adminEndpointsMatcher;


    public SessionAuthFilter() {
        super(DEFAULT_FILTER);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException, ServletException {
        Authentication auth;
        Optional<JWTUsernamePasswordAuthToken> token = getRequestToken(httpServletRequest);
        if (!token.isPresent()) {
            if (optionalAuthEndpointsMatcher.matches(httpServletRequest)) {
                LOGGER.info("Anonymous access is given");
                auth = new AnonymousAuthenticationToken("ANONYMOUS",
                        "ANONYMOUS",
                        Collections.singletonList(new SimpleGrantedAuthority("NONE")));
            } else {
                throw new NoJWTFoundException("There was no JWT found");
            }
        }
        else {
            LOGGER.info("Provide session with JWT access");
            LOGGER.info("Provided BEARER TOKEN is: {}", token.get());
            auth = getAuthenticationManager().authenticate(token.get());
//            if(adminEndpointsMatcher.matches(httpServletRequest)){
//                LOGGER.info("Provide session with JWT access - RESTAURANT OWNER");
//                if(isRestaurantOwner(auth))
//                    return auth;
//            }
            if(needAuthEndpointsMatcher.matches(httpServletRequest)){
                LOGGER.info("Provide session with JWT access - AUTH NEEDED");
                isAuthenticated(auth);
            }
        }
        return auth;
    }
    private boolean isAuthenticated(Authentication auth){
        if(!auth.getAuthorities().contains(new SimpleGrantedAuthority(RESTAURANT_OWNER))
                && !auth.getAuthorities().contains(new SimpleGrantedAuthority(USER))){
            throw new UnauthorizedException("User not authenticated");
        }
        return true;
    }
    private boolean isRestaurantOwner(Authentication auth){
        if(!auth.getAuthorities().contains(new SimpleGrantedAuthority(RESTAURANT_OWNER))){
            throw new UnauthorizedException("It is not a restaurant owner");
        }
        return true;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        if (logOutEndpointMatcher.matches(request)) {
            LOGGER.trace("THE USER WILL BE LOGGED OUT");
            String tokenString = getRequestToken(request)
                    .orElseThrow(()->new NoJWTFoundException("There was no JWT found"))
                    .getToken();
            Claims tokenClaims = getRequestClaims(request);
            LocalDateTime expiry = tokenClaims.getExpiration().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        chain.doFilter(request, response);
    }

    private Optional<JWTUsernamePasswordAuthToken> getRequestToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        JWTUsernamePasswordAuthToken ret = null;
        if (header != null && header.startsWith(AUTH_BEARER_TYPE)) {
            String authToken = header.substring(AUTH_BEARER_TYPE.length());
            ret = new JWTUsernamePasswordAuthToken(authToken);
        }
        return Optional.ofNullable(ret);
    }

    private Claims getRequestClaims(HttpServletRequest request) {
        Optional<JWTUsernamePasswordAuthToken> token = getRequestToken(request);
        Claims ret = null;
        if (token.isPresent()) {
            ret = jwtUtility.validateTokenString(token.get().getToken());
        }
        return ret;
    }
}
