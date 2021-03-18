package ar.edu.itba.paw.persistence;

import java.util.List;

import ar.edu.itba.paw.model.User;

public interface UserDao {

  User get(String id);

  List<User> list();

  User save(User user);

}
