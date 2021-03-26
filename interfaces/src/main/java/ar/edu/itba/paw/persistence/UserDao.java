package ar.edu.itba.paw.persistence;

import java.util.Optional;

import ar.edu.itba.paw.model.User;

public interface UserDao {

  public Optional<User> findById(long id);

  public User register(String username);
}
