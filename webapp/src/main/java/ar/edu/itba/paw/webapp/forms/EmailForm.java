package ar.edu.itba.paw.webapp.forms;

import javax.validation.constraints.Pattern;

public class EmailForm {

    @Pattern(regexp ="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
