package ar.edu.itba.paw.model.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {

    public UnauthorizedException(String s) {
        super(s);
    }
}

