package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validators.FieldMatch;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldMatch(first = "password", second = "repeatPassword")
public class PasswordResetForm {
    @Size(min = 8, max = 100)
    @NotNull
    @NotEmpty
    private String password;
    @Size(min = 8, max = 100)
    @NotNull
    @NotEmpty
    private String repeatPassword;

    public PasswordResetForm() {
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getRepeatPassword() {
        return this.repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
