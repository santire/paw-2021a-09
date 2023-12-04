package ar.edu.itba.paw.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    Map<String, String> generateTokens(UserDetails user);
    Claims getClaims(String accessToken);
    Map<String, String> refreshTokens(String refreshToken);
}
