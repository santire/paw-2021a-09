package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.RefreshToken;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.TokenDao;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class TokenServiceImpl implements JwtService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);
    private static final long ACCESS_TOKEN_DURATION = 30 * 60L; // 30min
    //    private static final long ACCESS_TOKEN_DURATION = 15L; // 15s
    private static final long REFRESH_TOKEN_DURATION = 15 * 24 * 60 * 60L; // 15 days
    //    private static final long REFRESH_TOKEN_DURATION =  2* 60L; // 2min

    //    @Value("${jwt.secret-key}")
    private String secret = "secret";

    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Map<String, String> generateTokens(UserDetails userDetails) {
        User user = userService
                .findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
        return createTokens(user);
    }


    @Override
    @Transactional
    public Map<String, String> refreshTokens(String token) {
        Map<String, String> result = null;

        // Check that token is signed and not expired
        LOGGER.info("Reading refresh token: {}", token);
        Claims claims = getClaims(token);
        if (claims != null) {
            // Obtain corresponding refresh token from db
            LOGGER.info("Obtaining token with jti: {}", claims.getId());
            RefreshToken rToken = tokenDao.findRefreshToken(claims.getId()).orElse(null);
            if (rToken != null && !rToken.getExpiresAt().isBefore(LocalDateTime.now())) {// Get user and invalidate used token
                User user = rToken.getUser();
                LOGGER.info("Removing refresh token: {}", rToken.getId());
                tokenDao.invalidateRefreshToken(rToken);
                result = createTokens(user);
            }
        }

        return result;
    }

    private Map<String, String> createTokens(User user) {
        final String refreshJti = UUID.randomUUID().toString();
        LOGGER.info("creating tokens for user {}", user.getEmail());

        LOGGER.debug("Signing with: {}", secret);
        LOGGER.debug("Signing with (base64): {}", Base64.getEncoder().encode(secret.getBytes()));

        RefreshToken newTokenObj = new RefreshToken(
                refreshJti,
                LocalDateTime.now(),
                getExpiryDateForToken(REFRESH_TOKEN_DURATION)
        );

        RefreshToken refreshToken = tokenDao.addRefreshToken(user, newTokenObj);
        Map<String, String> tokens = null;
        if (refreshToken != null) {
            String accessJwt = generateAccessJwtValue(user.getEmail());
            String refreshJwt = generateRefreshJwtValue(refreshToken);
            tokens = new HashMap<>();
            tokens.put("access_token", accessJwt);
            tokens.put("refresh_token", refreshJwt);
        }

        return tokens;
    }

    private String generateAccessJwtValue(String email) {
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiration = getExpiryDateForToken(ACCESS_TOKEN_DURATION);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(issuedAt.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encode(secret.getBytes()))
                .compact();
    }

    private String generateRefreshJwtValue(RefreshToken refreshToken) {
        LocalDateTime issuedAt = refreshToken.getCreatedAt();
        LocalDateTime expiration = refreshToken.getExpiresAt();
        return Jwts.builder()
                .setSubject(refreshToken.getUser().getEmail())
                .setId(refreshToken.getJti())
                .setIssuedAt(Date.from(issuedAt.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encode(secret.getBytes()))
                .compact();
    }


    private LocalDateTime getExpiryDateForToken(Long accessDurationSeconds) {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.plusSeconds(accessDurationSeconds);
    }

    @Override
    public Claims getClaims(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encode(secret.getBytes()))
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException ex) {
            LOGGER.error("Token has expired");
        } catch (SignatureException ex) {
            LOGGER.error("Invalid token signature");
        } catch (Exception ex) {
            LOGGER.error("Error validating token: {}", ex.getMessage());
        }
        return claims;
    }


}
