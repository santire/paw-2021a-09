package ar.edu.itba.paw.service;

import java.util.Optional;

import ar.edu.itba.paw.model.User;

public interface UserService {

  public Optional<User> findById(long id);

  public User register(final String username, final String password, final String firstName,final String lastName, final String email, final String phone);

  public User register(final String email);

  public Optional<User> findByEmail(String email);


}
