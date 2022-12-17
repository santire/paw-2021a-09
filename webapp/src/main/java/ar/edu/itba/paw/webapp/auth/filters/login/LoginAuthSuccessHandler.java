package ar.edu.itba.paw.webapp.auth.filters.login;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.auth.token.JWTUtility;
import ar.edu.itba.paw.webapp.dto.LoginDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginAuthSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthSuccessHandler.class);

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        LOGGER.info("The user with email {} has been successfully authenticated", authentication.getName());

        User user = userService.findByEmail(authentication.getName()).get();
        String jwt = jwtUtility.createToken(user);

        httpServletResponse.addHeader("Authorization", jwt);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        LOGGER.info("Token is: {}", jwt);

        UserDto userDto = UserDto.fromUser(user);
        TokenResponse responseObject = new TokenResponse(userDto, jwt);

//        new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), LoginDto.from(username,jwt));
        new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), responseObject);

    }

    private static class TokenResponse {
        UserDto user;
        String token;
        //TODO: add ROLES
        // List<String> roles;

        public TokenResponse(UserDto user, String token) {
            this.user = user;
            this.token = token;
        }

        public UserDto getUser() {
            return user;
        }

        public String getToken() {
            return token;
        }
    }
}
