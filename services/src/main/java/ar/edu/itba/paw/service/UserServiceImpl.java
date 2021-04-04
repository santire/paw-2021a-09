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

  @Autowired
  private EmailService emailService;

  @Override
  public Optional<User> findById(long id) {
    return this.userDao.findById(id);
  }

  @Override
  public User register(String username, String password, String first_name, String last_name, String email, String phone) {
    emailService.sendRegistrationEmail(email);
    return userDao.register(username,password,first_name,last_name, email, phone);
  }


}
