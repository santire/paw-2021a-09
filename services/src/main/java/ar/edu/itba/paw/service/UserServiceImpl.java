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
import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import ar.edu.itba.paw.model.exceptions.TokenDoesNotExistException;
import ar.edu.itba.paw.model.exceptions.TokenExpiredException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.context.MessageSource;


import java.util.Locale;

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
      String phone) throws EmailInUseException, TokenCreationException {
      Locale locale = LocaleContextHolder.getLocale();

      User user = userDao.register(username,encoder.encode(password), firstName, lastName, email, phone);
      String url = "http://pawserver.it.itba.edu.ar/paw-2021a-09/activate?token=";

      String token = UUID.randomUUID().toString();
      LocalDateTime createdAt = LocalDateTime.now();

      userDao.assignTokenToUser(token, createdAt, user.getId());
        String plainText = messageSource.getMessage("mail.register.plain",new Object[]{user.getFirstName()},locale)+"\n"+url+token+"\n";
        String emailContent="<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>Gourmetable</title><style>body {margin:0; padding:0; -webkit-text-size-adjust:none; -ms-text-size-adjust:none;} img{line-height:100%; outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;} a img{border: none;} #backgroundTable {margin:0; padding:0; width:100% !important; } a, a:link{color:#2A5DB0; text-decoration: underline;} table td {border-collapse:collapse;} span {color: inherit; border-bottom: none;} span:hover { background-color: transparent; }</style><style>.scalable-image img{max-width:100% !important;height:auto !important}.button a{transition:background-color .25s, border-color .25s}.button a:hover{background-color:#e1e1e1 !important;border-color:#0976a5 !important}@media only screen and (max-width: 400px){.preheader{font-size:12px !important;text-align:center !important}.header--white{text-align:center}.header--white .header__logo{display:block;margin:0 auto;width:118px !important;height:auto !important}.header--left .header__logo{display:block;width:118px !important;height:auto !important}}@media screen and (-webkit-device-pixel-ratio), screen and (-moz-device-pixel-ratio){.sub-story__image,.sub-story__content{display:block !important}.sub-story__image{float:left !important;width:200px}.sub-story__content{margin-top:30px !important;margin-left:200px !important}}@media only screen and (max-width: 550px){.sub-story__inner{padding-left:30px !important}.sub-story__image,.sub-story__content{margin:0 auto !important;float:none !important;text-align:center}.sub-story .button{padding-left:0 !important}}@media only screen and (max-width: 400px){.featured-story--top table,.featured-story--top td{text-align:left}.featured-story--top__heading td,.sub-story__heading td{font-size:18px !important}.featured-story--bottom:nth-child(2) .featured-story--bottom__inner{padding-top:10px !important}.featured-story--bottom__inner{padding-top:20px !important}.featured-story--bottom__heading td{font-size:28px !important;line-height:32px !important}.featured-story__copy td,.sub-story__copy td{font-size:14px !important;line-height:20px !important}.sub-story table,.sub-story td{text-align:center}.sub-story__hero img{width:100px !important;margin:0 auto}}@media only screen and (max-width: 400px){.footer td{font-size:12px !important;line-height:16px !important}}@media screen and (max-width:600px) {table[class=\"columns\"] {margin: 0 auto !important;float:none !important;padding:10px 0 !important;}td[class=\"left\"] {padding: 0px 0 !important;</style></head><body style=\"background: #e1e1e1;font-family:Arial, Helvetica, sans-serif; font-size:1em;\"><style type=\"text/css\">div.preheader { display: none !important; } </style><table id=\"backgroundTable\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background:#e1e1e1;\"><tr><td class=\"body\" align=\"center\" valign=\"top\" style=\"background:#e1e1e1;\" width=\"100%\"><table cellpadding=\"0\" cellspacing=\"0\"><tr><td width=\"640\"></td></tr><tr><td class=\"main\" width=\"640\" align=\"center\" style=\"padding: 0 10px;\"><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table cellspacing=\"0\" cellpadding=\"0\"><tr><td width=\"640\" align=\"left\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"header header--left\" style=\"padding: 20px 10px;\" align=\"center\"><a href=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/\" ><img class=\"header__logo\" src=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/resources/images/logo.png\" alt=\"Gourmetable\" style=\"display: block; border: 0;\" width=\"128\" height=\"128\"></a></td></tr></table></td></tr></table></td></tr></table><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table class=\"featured-story featured-story--top\" cellspacing=\"0\" cellpadding=\"0\"> <tr>  <td style=\"padding-bottom: 20px;\"> <table cellspacing=\"0\" cellpadding=\"0\">  <tr>   <td class=\"featured-story__inner\" style=\"background: #fff;\">  <table cellspacing=\"0\" cellpadding=\"0\">   <tr>   </tr>   <tr>    <td class=\"featured-story__content-inner\" style=\"padding: 32px 30px 45px;\">   <table cellspacing=\"0\" cellpadding=\"0\">    <tr>     <td class=\"featured-story__heading featured-story--top__heading\" style=\"background: #fff;\" width=\"640\" align=\"left\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 22px; color: #464646;\" width=\"400\" align=\"left\">     <a style=\"text-decoration: none; color: #464646;\">"
        +"</a>      </td>     </tr>    </table>     </td>    </tr>    <tr>     <td class=\"featured-story__copy\" style=\"background: #fff;\" width=\"640\" align=\"center\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; line-height: 22px; color: #555555; padding-top: 16px;\" align=\"left\">"
        +messageSource.getMessage("mail.register.body",new Object[]{user.getFirstName()},locale)
        +"</td>     </tr>    </table>     </td>    </tr>    "
        +"<td class=\"button\" style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; padding-top: 26px;\" width=\"640\" align=\"left\">    <a href=\""
        +url+token+"\""
        +"style=\"background: #0c99d5; color: #fff; text-decoration: none; border: 14px solid #0c99d5; border-left-width: 50px; border-right-width: 50px; text-transform: uppercase; display: inline-block;\">"
        +messageSource.getMessage("mail.register.button",null,locale)
        +"<tr></tr>   </table>    </td>   </tr>  </table>   </td>  </tr> </table>  </td> </tr></table></td></tr></table></td>        </tr>        <tr></custom></body></html>"
        ;
        Email myemail = new Email();
        myemail.setMailTo(user.getEmail());
        myemail.setMailSubject(messageSource.getMessage("mail.register.subject",null,locale));
        myemail.setMailContent(emailContent);
        emailService.sendEmail(myemail,plainText);
    
    return  user;
  }

  @Transactional
  @Override
  public void requestPasswordReset(String email) throws TokenCreationException {
    // User user = userDao.findByEmail(email).orElseThrow(UserNotFoundException::new);
    // String url = "http://pawserver.it.itba.edu.ar/paw-2021a-09/reset-password?token=";
    // String token = UUID.randomUUID().toString();
    // LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
    // Locale locale = LocaleContextHolder.getLocale();

    // userDao.assignPasswordTokenToUser(token, createdAt, user.getId());
    // String plainText = messageSource.getMessage("mail.forgot.plain",new Object[]{user.getFirstName()},locale)+"\n"+url+token+"\n";
    // String emailContent="<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>Gourmetable</title><style>body {margin:0; padding:0; -webkit-text-size-adjust:none; -ms-text-size-adjust:none;} img{line-height:100%; outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;} a img{border: none;} #backgroundTable {margin:0; padding:0; width:100% !important; } a, a:link{color:#2A5DB0; text-decoration: underline;} table td {border-collapse:collapse;} span {color: inherit; border-bottom: none;} span:hover { background-color: transparent; }</style><style>.scalable-image img{max-width:100% !important;height:auto !important}.button a{transition:background-color .25s, border-color .25s}.button a:hover{background-color:#e1e1e1 !important;border-color:#0976a5 !important}@media only screen and (max-width: 400px){.preheader{font-size:12px !important;text-align:center !important}.header--white{text-align:center}.header--white .header__logo{display:block;margin:0 auto;width:118px !important;height:auto !important}.header--left .header__logo{display:block;width:118px !important;height:auto !important}}@media screen and (-webkit-device-pixel-ratio), screen and (-moz-device-pixel-ratio){.sub-story__image,.sub-story__content{display:block !important}.sub-story__image{float:left !important;width:200px}.sub-story__content{margin-top:30px !important;margin-left:200px !important}}@media only screen and (max-width: 550px){.sub-story__inner{padding-left:30px !important}.sub-story__image,.sub-story__content{margin:0 auto !important;float:none !important;text-align:center}.sub-story .button{padding-left:0 !important}}@media only screen and (max-width: 400px){.featured-story--top table,.featured-story--top td{text-align:left}.featured-story--top__heading td,.sub-story__heading td{font-size:18px !important}.featured-story--bottom:nth-child(2) .featured-story--bottom__inner{padding-top:10px !important}.featured-story--bottom__inner{padding-top:20px !important}.featured-story--bottom__heading td{font-size:28px !important;line-height:32px !important}.featured-story__copy td,.sub-story__copy td{font-size:14px !important;line-height:20px !important}.sub-story table,.sub-story td{text-align:center}.sub-story__hero img{width:100px !important;margin:0 auto}}@media only screen and (max-width: 400px){.footer td{font-size:12px !important;line-height:16px !important}}@media screen and (max-width:600px) {table[class=\"columns\"] {margin: 0 auto !important;float:none !important;padding:10px 0 !important;}td[class=\"left\"] {padding: 0px 0 !important;</style></head><body style=\"background: #e1e1e1;font-family:Arial, Helvetica, sans-serif; font-size:1em;\"><style type=\"text/css\">div.preheader { display: none !important; } </style><table id=\"backgroundTable\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background:#e1e1e1;\"><tr><td class=\"body\" align=\"center\" valign=\"top\" style=\"background:#e1e1e1;\" width=\"100%\"><table cellpadding=\"0\" cellspacing=\"0\"><tr><td width=\"640\"></td></tr><tr><td class=\"main\" width=\"640\" align=\"center\" style=\"padding: 0 10px;\"><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table cellspacing=\"0\" cellpadding=\"0\"><tr><td width=\"640\" align=\"left\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"header header--left\" style=\"padding: 20px 10px;\" align=\"center\"><a href=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/\" ><img class=\"header__logo\" src=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/resources/images/logo.png\" alt=\"Gourmetable\" style=\"display: block; border: 0;\" width=\"128\" height=\"128\"></a></td></tr></table></td></tr></table></td></tr></table><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table class=\"featured-story featured-story--top\" cellspacing=\"0\" cellpadding=\"0\"> <tr>  <td style=\"padding-bottom: 20px;\"> <table cellspacing=\"0\" cellpadding=\"0\">  <tr>   <td class=\"featured-story__inner\" style=\"background: #fff;\">  <table cellspacing=\"0\" cellpadding=\"0\">   <tr>   </tr>   <tr>    <td class=\"featured-story__content-inner\" style=\"padding: 32px 30px 45px;\">   <table cellspacing=\"0\" cellpadding=\"0\">    <tr>     <td class=\"featured-story__heading featured-story--top__heading\" style=\"background: #fff;\" width=\"640\" align=\"left\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 22px; color: #464646;\" width=\"400\" align=\"left\">     <a style=\"text-decoration: none; color: #464646;\">"
    // +messageSource.getMessage("mail.forgot.title",null,locale)
    // +"</a>      </td>     </tr>    </table>     </td>    </tr>    <tr>     <td class=\"featured-story__copy\" style=\"background: #fff;\" width=\"640\" align=\"center\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; line-height: 22px; color: #555555; padding-top: 16px;\" align=\"left\">"
    // +messageSource.getMessage("mail.forgot.body",new Object[]{user.getFirstName()},locale)
    // +"</td>     </tr>    </table>     </td>    </tr>    "
    // +"<td class=\"button\" style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; padding-top: 26px;\" width=\"640\" align=\"left\">    <a href=\""
    // +url+token+"\""
    // +"style=\"background: #0c99d5; color: #fff; text-decoration: none; border: 14px solid #0c99d5; border-left-width: 50px; border-right-width: 50px; text-transform: uppercase; display: inline-block;\">"
    // +messageSource.getMessage("mail.forgot.button",null,locale)
    // +"<tr></tr>   </table>    </td>   </tr>  </table>   </td>  </tr> </table>  </td> </tr></table></td></tr></table></td>        </tr>        <tr></custom></body></html>"
    // ;
    // Email myemail = new Email();
    // myemail.setMailTo(user.getEmail());
    // myemail.setMailSubject(messageSource.getMessage("mail.forgot.subject",null,locale));
    // myemail.setMailContent(emailContent);
    // emailService.sendEmail(myemail,plainText);
    
    LOGGER.error("Not yet implemented");
  }

  @Override
  public User activateUserByToken(String token) throws TokenExpiredException {


    Optional<VerificationToken> maybeToken = userDao.getToken(token);
    LOGGER.debug("GOT TOKEN {}", maybeToken.get().getToken());
    LOGGER.debug("WITH UID {}", maybeToken.get().getUser().getId());
    User user = getUserFromMaybeToken(maybeToken);

    LOGGER.debug("Activating user {}", user.getId());
    user = userDao.activateUserById(user.getId()).orElseThrow(() -> new RuntimeException("Couldn't activate user"));
    // userDao.deleteToken(token);
    return user;
  }

  @Override
  public User updatePasswordByToken(String token, String password) throws TokenExpiredException {

    throw new RuntimeException("Method not yet implemented");
    // Optional<VerificationToken> maybeToken = userDao.getPasswordToken(token);
    // User user = getUserFromMaybeToken(maybeToken);

    // updateUser(user.getId(), 
                // user.getName(),
                // encoder.encode(password),
                // user.getFirstName(),
                // user.getLastName(),
                // user.getEmail(),
                // user.getPhone());

    // userDao.deleteAssociatedPasswordTokens(token);
    // return user;
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
    // userDao.updateUser(id, username, password, firstName, lastName, email, phone);
    User user = userDao.findById(id).orElseThrow(UserNotFoundException::new);
    user.setUsername(username);
    user.setPassword(password);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);
    user.setPhone(phone);
  }

  private User getUserFromMaybeToken(Optional<VerificationToken> maybeToken) throws TokenExpiredException {
    VerificationToken verificationToken = maybeToken.orElseThrow(TokenDoesNotExistException::new);
    LocalDateTime expiryDate = verificationToken.getCreatedAt().plusDays(1);

    if(LocalDateTime.now().isAfter(expiryDate)) {
      LOGGER.warn("verificationToken {} is expired", verificationToken.getToken());
      throw new TokenExpiredException();
    }

    return verificationToken.getUser();

    // Optional<User> maybeUser = userDao.findById(verificationToken.getUser().getId());

    // return maybeUser.orElseThrow(()-> new RuntimeException("Invalid user id"));
  }
}
