package ar.edu.itba.paw.persistence;

import java.util.List;

import ar.edu.itba.paw.model.User;

public interface UserDao {

  User findById(long id);
  User register(String username);
}
