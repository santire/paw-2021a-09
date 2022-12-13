package ar.edu.itba.paw.webapp.dto;

public class LoginDto {
    private String email;
    private String password;

    private LoginDto() {
        /* Required by JSON object mapper */
    }

    private LoginDto(String email , String password) {
        this.email = email;

        // TODO: Ojo con esto, revisar si esta bien
        this.password = password;
    }

    public static LoginDto from(String email, String password) {
        return new LoginDto(email,password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
