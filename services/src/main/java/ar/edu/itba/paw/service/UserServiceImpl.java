package ar.edu.itba.paw.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ar.edu.itba.paw.model.Restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import ar.edu.itba.paw.model.exceptions.TokenDoesNotExistException;
import ar.edu.itba.paw.model.exceptions.TokenExpiredException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
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

  @Override
  public Optional<User> findById(long id) {
    return this.userDao.findById(id);
  }

  @Transactional
  @Override
  public User register(String username, String password, String firstName, String lastName, String email,
      String phone) throws EmailInUseException, TokenCreationException {

      User user = userDao.register(username,encoder.encode(password), firstName, lastName, email, phone);
      String url = "http://pawserver.it.itba.edu.ar/paw-2021a-09/activate?token=";

      String token = UUID.randomUUID().toString();
      LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());

      userDao.assignTokenToUser(token, createdAt, user.getId());

      emailService.sendRegistrationEmail(email, url+token);

    return  user;
  }

  @Transactional
  @Override
  public void requestPasswordReset(String email) throws TokenCreationException {
    User user = userDao.findByEmail(email).orElseThrow(UserNotFoundException::new);
    String url = "http://pawserver.it.itba.edu.ar/paw-2021a-09/reset-password?token=";
    String token = UUID.randomUUID().toString();
    LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());

    userDao.assignPasswordTokenToUser(token, createdAt, user.getId());

    emailService.sendResetPasswordEmail(email, url+token);
      
  }

  @Override
  public User activateUserByToken(String token) throws TokenExpiredException {


    Optional<VerificationToken> maybeToken = userDao.getToken(token);
    User user = getUserFromMaybeToken(maybeToken);

    user = userDao.activateUserById(user.getId()).orElseThrow(() -> new RuntimeException("Couldn't activate user"));
    userDao.deleteToken(token);
    return user;
  }

  @Override
  public User updatePasswordByToken(String token, String password) throws TokenExpiredException {

    Optional<VerificationToken> maybeToken = userDao.getPasswordToken(token);
    User user = getUserFromMaybeToken(maybeToken);

    user = userDao.activateUserById(user.getId()).orElseThrow(() -> new RuntimeException("Couldn't activate user"));
    updateUser(user.getId(), 
                user.getName(),
                encoder.encode(password),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone());

    userDao.deleteAssociatedPasswordTokens(token);
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
  public void updateUser(long id, String username, String password, String firstName, String lastName, String email, String phone) {
    userDao.updateUser(id, username, password, firstName, lastName, email, phone);
  }

  private User getUserFromMaybeToken(Optional<VerificationToken> maybeToken) throws TokenExpiredException {
    VerificationToken verificationToken = maybeToken.orElseThrow(TokenDoesNotExistException::new);
    Instant expiryDate = Instant.from(verificationToken.getCreatedAt().toInstant()).plus(24, ChronoUnit.HOURS);

    if(Instant.now().isAfter(expiryDate)) {
      LOGGER.warn("verificationToken {} is expired", verificationToken.getToken());
      throw new TokenExpiredException();
    }

    /*Optional<User> maybeUser = userDao.findById(verificationToken.getUserId());
    return maybeUser.orElseThrow(() -> {
      // If the data source is configured properly there shouldn't be an invalid user id
      // assigned to a verification token. If this happens something's wrong...
      LOGGER.error("id {} associated to token {} is not valid.");
      throw new RuntimeException("Invalid user id");
    });

     */
    Optional<User> maybeUser = userDao.findById(verificationToken.getUserId());
    User user = maybeUser.orElseThrow(() -> new RuntimeException("Invalid user id"));
    return user;
  }
}
