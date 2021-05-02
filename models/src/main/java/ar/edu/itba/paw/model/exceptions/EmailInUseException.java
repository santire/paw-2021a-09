package ar.edu.itba.paw.model.exceptions;

public class EmailInUseException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String email;

    public EmailInUseException(String msString, String email) {
        super(msString);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
