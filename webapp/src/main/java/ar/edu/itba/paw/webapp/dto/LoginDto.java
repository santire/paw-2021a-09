package ar.edu.itba.paw.webapp.dto;

public class LoginDto {
    private String username;
    private String password;

    private LoginDto() {
        /* Required by JSON object mapper */
    }

    private LoginDto(String username , String password) {
        this.username = username;

        // TODO: Ojo con esto, revisar si esta bien
        this.password = password;
    }

    public static LoginDto from(String username, String password) {
        return new LoginDto(username,password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
