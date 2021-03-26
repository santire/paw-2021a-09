package ar.edu.itba.paw.service;

import java.util.Optional;

import ar.edu.itba.paw.model.User;

public interface UserService {

  public Optional<User> findById(long id);

  public User register(String username);
}
