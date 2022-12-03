package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.model.exceptions.InvalidVerificationTokenException;

import java.util.Optional;

public interface TokenService {


    /**
     *
     * @param token
     * @param id
     */
    public void create(String token, long id) throws UserNotFoundException;

    /**
     *
     * @param token
     * @return
     */
    public boolean isValid(String token);

    /**
     *
     * @param token
     */
    public void verify(String token) throws InvalidVerificationTokenException;

    /**
     *
     * @param token
     * @return
     */
    public Optional<User> getUser(String token);


    public void checkOut(String token);
}