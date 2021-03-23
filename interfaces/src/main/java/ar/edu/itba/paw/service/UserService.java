package ar.edu.itba.paw.service;

import java.util.List;

import ar.edu.itba.paw.model.User;

public interface UserService {

  User findById(long id);
  User register(String username);
}
