package ar.edu.itba.paw.service;

import java.util.List;

import ar.edu.itba.paw.model.User;

public interface UserService {

  User findByUser(String id);

  List<User> list();

}
