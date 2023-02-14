package ar.edu.itba.paw.model.exceptions;

public class UsernameInUseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String username;

    public UsernameInUseException(String msString, String username) {
        super(msString);
        this.username = username;
    }

    public String getUsername() { return username; }
}
