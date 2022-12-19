package ar.edu.itba.paw.webapp.dto;

public class TokenResponseDto {
    UserDto user;
    String token;
    //TODO: add ROLES
    // List<String> roles;

    public TokenResponseDto(UserDto user, String token) {
        this.user = user;
        this.token = token;
    }

    public TokenResponseDto() {
        // for Jersey
    }

    public UserDto getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}