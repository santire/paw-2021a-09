package ar.edu.itba.paw.persistence;

import java.util.Optional;

import ar.edu.itba.paw.model.User;

public interface UserDao {

  public Optional<User> findById(long id);

  public User register(final String username, final String password, final String fullname, final String email, final String phone);
}
