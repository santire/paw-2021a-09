package ar.edu.itba.paw.webapp.forms;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class LoginForm {

    @Email
    private String email;

    @Size(min = 8, max = 100)
    private String password;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
