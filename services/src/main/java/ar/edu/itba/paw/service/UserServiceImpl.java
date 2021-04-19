package ar.edu.itba.paw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.UserDao;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private UserDao userDao;

  @Autowired
  private EmailService emailService;

  @Override
  public Optional<User> findById(long id) {
    return this.userDao.findById(id);
  }

  @Transactional
  @Override
  public User register(String username, String password, String firstName, String lastName, String email,
      String phone) {

    User user = userDao.register(username,encoder.encode(password), firstName, lastName, email, phone);
    emailService.sendRegistrationEmail(email);

    return  user;
  }

  @Override
  public User register(String email) {
    User user = userDao.register("dummy","dummy","dummy","dummy", email,"dummy");
    //emailService.sendRegistrationEmail(email);
    return user;
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userDao.findByEmail(email);
  }


}
