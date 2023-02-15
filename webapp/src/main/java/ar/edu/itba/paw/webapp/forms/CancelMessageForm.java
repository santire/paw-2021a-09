package ar.edu.itba.paw.webapp.forms;

import org.hibernate.validator.constraints.NotEmpty;

public class CancelMessageForm {
    @NotEmpty
    String message;

    public CancelMessageForm() {
    }

    public CancelMessageForm(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
