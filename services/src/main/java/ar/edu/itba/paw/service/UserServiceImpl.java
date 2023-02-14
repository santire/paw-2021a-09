package ar.edu.itba.paw.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;


import ar.edu.itba.paw.model.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.i18n.LocaleContextHolder;


import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Email;
import ar.edu.itba.paw.model.EmailTemplate;
import ar.edu.itba.paw.model.PasswordToken;
import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.context.MessageSource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@Component
public class UserServiceImpl implements UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private UserDao userDao;

  @Autowired
  private EmailService emailService;

  @Autowired
  private MessageSource messageSource;

  @Override
  public Optional<User> findById(long id) {
    return userDao.findById(id);
  }

  @Transactional(rollbackFor = {UsernameInUseException.class, EmailInUseException.class, TokenCreationException.class})
  @Override
  public User register(String username, String password, String firstName, String lastName, String email,
                       String phone, String baseUrl)
          throws UsernameInUseException, EmailInUseException, TokenCreationException {
    User user = userDao.register(username,encoder.encode(password), firstName, lastName, email, phone);
    if (user == null) return null;
    String url = baseUrl + "/register?type=activate&token=";

    String token = UUID.randomUUID().toString();
    LocalDateTime createdAt = LocalDateTime.now();

    userDao.assignTokenToUser(token, createdAt, user.getId());
    emailService.sendRegistrationEmail(user.getEmail(), user.getFirstName(), url+token);

    return  user;
  }

  @Transactional
  @Override
  public void requestPasswordReset(String email, String baseUrl) throws TokenCreationException {
    LOGGER.debug("Requesting reset for {}", email);
    User user = userDao.findByEmail(email).orElseThrow(UserNotFoundException::new);
    String url = baseUrl + "/reset?type=reset&token=";
    String token = UUID.randomUUID().toString();
    LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());

    userDao.assignPasswordTokenToUser(token, createdAt, user.getId());
    emailService.sendPasswordResetEmail(user.getEmail(), user.getFirstName(), url+token);
  }

  @Override
  @Transactional
  public User activateUserByToken(String token) throws TokenExpiredException {


    Optional<VerificationToken> maybeToken = userDao.getToken(token);
    LOGGER.debug("GOT TOKEN {}", maybeToken.isPresent() ? maybeToken.get().getToken() : null);
    LOGGER.debug("WITH UID {}", maybeToken.isPresent() ? maybeToken.get().getUser().getId(): null);
    VerificationToken verificationToken = maybeToken.orElseThrow(TokenDoesNotExistException::new);
    LocalDateTime expiryDate = verificationToken.getCreatedAt().plusDays(1);

    if(LocalDateTime.now().isAfter(expiryDate)) {
      LOGGER.warn("verificationToken {} is expired", verificationToken.getToken());
      throw new TokenExpiredException();
    }

    User user = verificationToken.getUser();

    LOGGER.debug("Activating user {}", user.getId());
    user.setActive(true);
    userDao.deleteToken(token);
    return user;
  }

  @Override
  @Transactional
  public User updatePasswordByToken(String token, String password) throws TokenExpiredException, TokenDoesNotExistException {

    Optional<PasswordToken> maybeToken = userDao.getPasswordToken(token);
    PasswordToken passwordToken = maybeToken.orElseThrow(TokenDoesNotExistException::new);
    LOGGER.debug("GOT TOKEN {}", passwordToken.getToken());
    LOGGER.debug("WITH UID {}", passwordToken.getUser().getId());

    LocalDateTime expiryDate = passwordToken.getCreatedAt().plusDays(1);

    if(LocalDateTime.now().isAfter(expiryDate)) {
      LOGGER.warn("verificationToken {} is expired", passwordToken.getToken());
      throw new TokenExpiredException();
    }

    User user = passwordToken.getUser();

    updateUser(user.getId(),
            encoder.encode(password),
            user.getFirstName(),
            user.getLastName(),
            user.getPhone());

    userDao.deleteAssociatedPasswordTokens(user);
    return user;
  }

  @Override
  public Optional<User> findByUsername(String username) { return userDao.findByUsername(username); }

  @Override
  public Optional<User> findByEmail(String email) {
    return userDao.findByEmail(email);
  }

  @Override
  public boolean isTheRestaurantOwner(long userId, long restaurantId) {
    User user = userDao.findById(userId).orElseThrow(UserNotFoundException::new);
    return user
            .getOwnedRestaurants()
            .stream()
            .filter((r) -> r.getId().equals(restaurantId))
            .findFirst()
            .isPresent();
  }

  @Override
  public boolean isRestaurantOwner(long userId) {
    User user = userDao.findById(userId).orElseThrow(UserNotFoundException::new);
    return user.getOwnedRestaurants().size() > 0;
  }


  @Override
  @Transactional
  public void updateUser(long id, String password, String firstName, String lastName, String phone) {
    User user = userDao.findById(id).orElseThrow(UserNotFoundException::new);
    if(password != null && !password.isEmpty()) user.setPassword(password);
    if(firstName != null && !firstName.isEmpty()) user.setFirstName(firstName);
    if(lastName != null && !lastName.isEmpty()) user.setLastName(lastName);
    if(phone != null && !phone.isEmpty()) user.setPhone(phone);
  }

}
