package ar.edu.itba.paw.webapp.auth.token;

import ar.edu.itba.paw.model.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;

@Component
public class JWTUtility {
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtility.class);

    private final String secret;
    private final long maxValidTime;
    private final SecureRandom secureRandom;

    @Autowired
    public JWTUtility(Environment environment) {
        this.secret = "my-secret" ;//environment.getRequiredProperty(environment.getRequiredProperty("state") + ".token.secret");
        this.maxValidTime = 864_000_000;//Long.MAX_VALUE;//environment.getRequiredProperty(environment.getRequiredProperty("state") + ".token.time", Integer.class);
        this.secureRandom = new SecureRandom();
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        Date timeNow = new Date();
        return Jwts.builder()
                .setClaims(claims)
                // TODO: Change to real role ( user.getRole() )
                .claim("Role", "USER")
                .setHeaderParam("salt",randomizer())
                .signWith(SignatureAlgorithm.HS512, secret)
                .setIssuedAt(timeNow)
                .setExpiration(new Date(timeNow.getTime() + maxValidTime))
                .compact();
    }

    public Claims validateTokenString(String tokenString) {
        Claims claims;
        try {
            Jwt token = Jwts.parser().setSigningKey(secret).parse(tokenString);
            claims = (Claims) token.getBody();
            Header header = token.getHeader();
            Date timeNow = new Date();

            if(claims.getIssuedAt() == null || claims.getIssuedAt().after(timeNow)) {
                throw new JwtException("the issue date is not valid");
            }
            if(claims.getExpiration() == null || claims.getExpiration().before(timeNow)) {
                throw new JwtException("the expiration date is not valid");
            }
            if(header.get("salt") == null) {
                throw new JwtException("the jwt has no salt");
            }

        } catch (ClassCastException | JwtException e) {
            LOGGER.warn("an error occurred while validating the jwt");
            claims = null;
        }
        return claims;
    }

    private long randomizer(){
        int N = 5;
        long r = 1;
        long sum = 0;
        for(int i = 0 ; i < N ; i++) {
            sum += secureRandom.nextLong();
        }
        return sum/5;
    }

}
