package ar.edu.itba.paw.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.i18n.LocaleContextHolder;


import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Email;
import ar.edu.itba.paw.model.EmailTemplate;
import ar.edu.itba.paw.model.PasswordToken;
import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import ar.edu.itba.paw.model.exceptions.TokenDoesNotExistException;
import ar.edu.itba.paw.model.exceptions.TokenExpiredException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.context.MessageSource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
  private MessageSource messageSource;

  @Override
  public Optional<User> findById(long id) {
    return userDao.findById(id);
  }

  @Transactional
  @Override
  public User register(String username, String password, String firstName, String lastName, String email,
      String phone, String baseUrl) throws EmailInUseException, TokenCreationException {
      Locale locale = LocaleContextHolder.getLocale();

      User user = userDao.register(username,encoder.encode(password), firstName, lastName, email, phone);
      String url = baseUrl + "/activate?token=";

      String token = UUID.randomUUID().toString();
      LocalDateTime createdAt = LocalDateTime.now();

      userDao.assignTokenToUser(token, createdAt, user.getId());
        String plainText = messageSource.getMessage("mail.register.plain",new Object[]{user.getFirstName()},locale)+"\n"+url+token+"\n";
        Email myemail = new Email();
        myemail.setMailTo(user.getEmail());
        myemail.setMailSubject(messageSource.getMessage("mail.register.subject",null,locale));
        Map<String, Object> args = new HashMap<>();
        args.put("titleMessage", "");
        args.put("bodyMessage",messageSource.getMessage("mail.register.body",new Object[]{user.getFirstName()},locale));
        args.put("buttonMessage",messageSource.getMessage("mail.register.button",null,locale));
        args.put("link", url+token);

        emailService.sendEmail(myemail,plainText, args, EmailTemplate.BUTTON);
    
    return  user;
  }

  @Transactional
  @Override
  public void requestPasswordReset(String email, String baseUrl) throws TokenCreationException {
    User user = userDao.findByEmail(email).orElseThrow(UserNotFoundException::new);
    String url = baseUrl + "/reset-password?token=";
    String token = UUID.randomUUID().toString();
    LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
    Locale locale = LocaleContextHolder.getLocale();

    userDao.assignPasswordTokenToUser(token, createdAt, user.getId());
    String plainText = messageSource.getMessage("mail.forgot.plain",new Object[]{user.getFirstName()},locale)+"\n"+url+token+"\n";
    Email myemail = new Email();
    myemail.setMailTo(user.getEmail());
    myemail.setMailSubject(messageSource.getMessage("mail.forgot.subject",null,locale));
    Map<String, Object> args = new HashMap<>();
    args.put("titleMessage", "");
    args.put("bodyMessage",messageSource.getMessage("mail.forgot.body",new Object[]{user.getFirstName()},locale));
    args.put("buttonMessage",messageSource.getMessage("mail.forgot.button",null,locale));
    args.put("link", url+token);

    emailService.sendEmail(myemail,plainText, args, EmailTemplate.BUTTON);
  }

  @Override
  @Transactional
  public User activateUserByToken(String token) throws TokenExpiredException {


    Optional<VerificationToken> maybeToken = userDao.getToken(token);
    LOGGER.debug("GOT TOKEN {}", maybeToken.get().getToken());
    LOGGER.debug("WITH UID {}", maybeToken.get().getUser().getId());
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
  public User updatePasswordByToken(String token, String password) throws TokenExpiredException {

    Optional<PasswordToken> maybeToken = userDao.getPasswordToken(token);
    LOGGER.debug("GOT TOKEN {}", maybeToken.get().getToken());
    LOGGER.debug("WITH UID {}", maybeToken.get().getUser().getId());
    PasswordToken passwordToken = maybeToken.orElseThrow(TokenDoesNotExistException::new);
    LocalDateTime expiryDate = passwordToken.getCreatedAt().plusDays(1);

    if(LocalDateTime.now().isAfter(expiryDate)) {
      LOGGER.warn("verificationToken {} is expired", passwordToken.getToken());
      throw new TokenExpiredException();
    }

    User user = passwordToken.getUser();

    updateUser(user.getId(), 
                user.getName(),
                encoder.encode(password),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone());

    userDao.deleteAssociatedPasswordTokens(user);
    return user;
  }

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
  public void updateUser(long id, String username, String password, String firstName, String lastName, String email, String phone) {
    User user = userDao.findById(id).orElseThrow(UserNotFoundException::new);
    user.setUsername(username);
    user.setPassword(password);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);
    user.setPhone(phone);
  }

}
