package ar.edu.itba.paw.webapp.forms;

import javax.validation.constraints.Pattern;

import ar.edu.itba.paw.webapp.validators.EmailInUse;

public class EmailForm {

    @Pattern(regexp ="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    @EmailInUse
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
