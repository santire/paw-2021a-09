package ar.edu.itba.paw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.UserDao;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserDao userDao;

  @Override
  public Optional<User> findById(long id) {
    return this.userDao.findById(id);
  }

  @Override
  public User register(final String username, final String password, final String fullname, final String email, final String phone) {
    return userDao.register(username,password,fullname, email, phone);
  }


}
