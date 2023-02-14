package ar.edu.itba.paw.webapp.forms;


import ar.edu.itba.paw.webapp.validators.EmailNotInUse;
import ar.edu.itba.paw.webapp.validators.FieldMatch;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FieldMatch(first = "password", second = "repeatPassword")
public class RegisterUserForm {

    @Size(min = 3, max = 30)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    @NotNull
    @NotEmpty
    private String username;

    @Size(min = 8, max = 100)
    @NotNull
    @NotEmpty
    private String password;

    @Size(min = 8, max = 100)
    @NotNull
    @NotEmpty
    private String repeatPassword;


    @Pattern(regexp = "[a-zA-ZñÑáéíóúÁÉÍÓÚ]+[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]*")
    @Size(min = 2, max = 100)
    @NotNull
    @NotEmpty
    private String firstName;

    @Pattern(regexp = "[a-zA-ZñÑáéíóúÁÉÍÓÚ]+[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]*")
    @Size(min = 2, max = 100)
    @NotNull
    @NotEmpty
    private String lastName;

    @Size(min = 6, max = 100)
    // @Pattern(regexp =
    // "/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/")

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    @EmailNotInUse
    @NotNull
    @NotEmpty
    private String email;

    private boolean emailInUse;

    @Size(min = 6, max = 15)
    @Pattern(regexp = "[0-9]+")
    @NotNull
    @NotEmpty
    private String phone;

    public RegisterUserForm() {
    }

    public boolean isEmailInUse() {
        return emailInUse;
    }

    public void setEmailInUse(boolean emailInUse) {
        this.emailInUse = emailInUse;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return this.repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
