package ar.edu.itba.paw.webapp.forms;

import javax.validation.constraints.Size;

import ar.edu.itba.paw.webapp.validators.FieldMatch;

@FieldMatch(first = "password", second = "repeatPassword")
public class PasswordForm {

    @Size(min = 8, max = 100)
    private String password;

    @Size(min = 8, max = 100)
    private String repeatPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

}
