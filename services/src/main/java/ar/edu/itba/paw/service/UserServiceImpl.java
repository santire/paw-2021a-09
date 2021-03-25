package ar.edu.itba.paw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.UserDao;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserDao userDao;

  @Override
  public User findById(long id) {
    return this.userDao.findById(id);
  }

  @Override
  public User register(final String username) {
    return userDao.register(username);
  }
}
