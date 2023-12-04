package ar.edu.itba.paw.webapp.auth.filters;

import ar.edu.itba.paw.service.JwtService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final UserDetailsService userDetailsService;
    private final JwtService tokenService;

    public JwtRequestFilter(UserDetailsService userDetailsService, JwtService tokenService) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String accessToken = extractAccessToken(request);
        final Claims claims = tokenService.getClaims(accessToken);
        if (claims != null) {
            // Access token is valid, continue processing the request
            authenticateUser(request, claims.getSubject());
        } else {
            // Access token is not valid
            // Check if there is a refresh token
            final String refreshToken = extractRefreshToken(request);

            if (StringUtils.hasText(refreshToken)) {
                // Attempt to refresh the access token
                final Map<String, String> tokens = tokenService.refreshTokens(refreshToken);

                if (tokens != null) {
                    final String newAccessToken = tokens.get("access_token");
                    final String newRefreshToken = tokens.get("refresh_token");

                    final Claims refreshClaims = tokenService.getClaims(newRefreshToken);
                    if (refreshClaims != null) {
                        // Refresh successful, set the new access token in the response
                        response.addHeader("X-Auth-Token", "Bearer " + newAccessToken);
                        response.addHeader("X-Refresh-Token", "Bearer " + newRefreshToken);
                        authenticateUser(request, refreshClaims.getSubject());
                    }

                }
            }
        }

        filterChain.doFilter(request, response);
    }


    private void authenticateUser(HttpServletRequest request, String username) {
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                LOGGER.info("The user with email {} has been successfully authenticated", userDetails.getUsername());
            } catch (UsernameNotFoundException e) {
                LOGGER.error("Username not found: {}", e.getMessage());
            }
        }
    }


    private String extractAccessToken(HttpServletRequest request) {
        return extractToken(request, "Authorization");
    }

    private String extractRefreshToken(HttpServletRequest request) {
        return extractToken(request, "X-Refresh-Token");
    }

    private String extractToken(HttpServletRequest request, String headerName) {
        String header = request.getHeader(headerName);
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }
}
