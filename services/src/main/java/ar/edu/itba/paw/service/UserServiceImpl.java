package ar.edu.itba.paw.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ar.edu.itba.paw.exceptions.UserRegistrationException;
import ar.edu.itba.paw.model.Restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.persistence.UserDao;

@Service
public class UserServiceImpl implements UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private UserDao userDao;

  @Autowired
  private EmailService emailService;

  @Autowired
  private RestaurantService restaurantService;

  @Override
  public Optional<User> findById(long id) {
    return this.userDao.findById(id);
  }

  @Transactional
  @Override
  public User register(String username, String password, String firstName, String lastName, String email,
      String phone) {

    Optional<User> maybeUser = userDao.register(username,encoder.encode(password), firstName, lastName, email, phone);
    if (maybeUser.isPresent()) {
      User user = maybeUser.get();

      String url = "http://pawserver.it.itba.edu.ar/paw-2021a-09/activate?token=";

      String token = UUID.randomUUID().toString();
      Timestamp createdAt = Timestamp.from(Instant.now());

      userDao.assignTokenToUser(token, createdAt, user.getId());

      emailService.sendRegistrationEmail(email, url+token);
    }

    return  maybeUser.orElseThrow(UserRegistrationException::new);
  }

  @Override
  public User activateUserByToken(String token) {
    // TODO: Add custom exceptions

    Optional<VerificationToken> maybeToken = userDao.getToken(token);

    VerificationToken verificationToken = maybeToken.orElseThrow(() -> new RuntimeException("Token doesn't exist"));

    Instant expiryDate = Instant.from(verificationToken.getCreatedAt().toInstant()).plus(24, ChronoUnit.HOURS);

    if(Instant.now().isAfter(expiryDate)) {
      LOGGER.warn("verificationToken {} is expired", token);
      throw new RuntimeException("Token is expired");
    }

    Optional<User> maybeUser = userDao.findById(verificationToken.getUserId());
    User user = maybeUser.orElseThrow(() -> {
      // If the data source is configured properly there shouldn't be an invalid user id
      // assigned to a verification token. If this happens something's wrong...
      LOGGER.error("id {} associated to token {} is not valid.");
      throw new RuntimeException("Invalid user id");
    });
    
    user = userDao.activateUserById(user.getId()).orElseThrow(() -> new RuntimeException("Couldn't activate user"));
    userDao.deleteToken(token);
    return user;
  }

  @Override
  public User register(String email) {
    // User user = userDao.register("dummy","dummy","dummy","dummy", email,"dummy");
    //emailService.sendRegistrationEmail(email);
    // return user;
    return null;
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userDao.findByEmail(email);
  }

  @Override
  public boolean isTheRestaurantOwner(long userId, long restaurantId) {
    return userDao.isTheRestaurantOwner(userId, restaurantId);
  }

  @Override
  public boolean isRestaurantOwner(long userId) {
     return userDao.isRestaurantOwner(userId);
  }

  @Override
  public void updatePassword(long id, String password) {
      userDao.updatePassword(id, encoder.encode(password));
  }
  @Override
  public void updateUsername(long id, String username) {
      userDao.updateUsername(id, username);
  }
  @Override
  public void updateFistName(long id, String first_name) {
    userDao.updateFistName(id, first_name);
  }
  @Override
  public void updateLastName(long id, String last_name) {
    userDao.updateLastName(id, last_name);
  }
  @Override
  public void updatePhone(long id, String phone) {
    userDao.updatePhone(id, phone);
  }

  @Override
  public void updateEmail(long id, String email) {
      userDao.updateEmail(id, email);
  }

  @Override
  public void updateUser(long id, String username, String password, String first_name, String last_name, String email, String phone) {
    userDao.updateUser(id, username, password, first_name, last_name, email, phone);
  }

}
