package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.PasswordToken;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.model.exceptions.*;
import ar.edu.itba.paw.persistence.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.service.utils.UriBuilder;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    @Transactional
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Override
    @Transactional
    public Optional<User> findByEmail(String email) {
        if (email == null) return Optional.empty();
        return userDao.findByEmail(email);
    }

    @Override
    @Transactional(rollbackFor = {UsernameInUseException.class, EmailInUseException.class, TokenCreationException.class})
    public User register(String username, String password, String firstName, String lastName, String email,
                         String phone,
                         String baseRequestUrl,
                         URI baseUri)
            throws UsernameInUseException, EmailInUseException, TokenCreationException {

        User user = userDao.register(username, encoder.encode(password), firstName, lastName, email, phone);

        final URI patchUri = makePatchUri(user, baseUri.toString());

        String token = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        String url = UriBuilder.fromUri(baseRequestUrl)
                .path("user")
                .path("activate")
                .queryParam("token", token)
                .queryParam("patchUrl", patchUri.toString())
                .build()
                .toString();

        userDao.assignTokenToUser(token, createdAt, user.getId());
        emailService.sendRegistrationEmail(user.getEmail(), user.getFirstName(), url);

        return user;
    }

    @Transactional
    @Override
    public void requestPasswordReset(String email, String baseRequestUrl, URI baseUri) throws TokenCreationException {
        User user = userDao.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if(!user.isActive()) {
            throw new UserNotFoundException();
        }
        LOGGER.debug("Requesting reset for {}", user.getEmail());
        final URI patchUri = makePatchUri(user, baseUri.toString());

        String token = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        String url = UriBuilder.fromUri(baseRequestUrl)
                .path("user")
                .path("reset")
                .queryParam("token", token)
                .queryParam("patchUrl", patchUri.toString())
                .build()
                .toString();

        userDao.assignPasswordTokenToUser(token, createdAt, user.getId());
        LOGGER.debug("Setting reset email url to: {}", url);
        emailService.sendPasswordResetEmail(user.getEmail(), user.getFirstName(), url);
    }

    private URI makePatchUri(User user, String baseUsersUrl) {
        return UriBuilder.
                fromUri(baseUsersUrl)
                .path(user.getId().toString())
                .build();
    }

    @Override
    @Transactional
    public User activateUserByToken(String token) throws TokenExpiredException {


        Optional<VerificationToken> maybeToken = userDao.getToken(token);
        LOGGER.debug("GOT TOKEN {}", maybeToken.isPresent() ? maybeToken.get().getToken() : null);
        LOGGER.debug("WITH UID {}", maybeToken.isPresent() ? maybeToken.get().getUser().getId() : null);
        VerificationToken verificationToken = maybeToken.orElseThrow(TokenDoesNotExistException::new);
        LocalDateTime expiryDate = verificationToken.getCreatedAt().plusDays(1);

        if (LocalDateTime.now().isAfter(expiryDate)) {
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

        if (LocalDateTime.now().isAfter(expiryDate)) {
            LOGGER.warn("verificationToken {} is expired", passwordToken.getToken());
            throw new TokenExpiredException();
        }

        User user = passwordToken.getUser();
        if (password != null && !password.isEmpty()) user.setPassword(encoder.encode(password));
        userDao.deleteAssociatedPasswordTokens(user);

        return user;
    }


    @Override
    @Transactional
    public User updateUser(long id, String password, String firstName, String lastName, String phone) {
        User user = userDao.findById(id).orElseThrow(UserNotFoundException::new);
        if (password != null && !password.isEmpty()) user.setPassword(encoder.encode(password));
        if (firstName != null && !firstName.isEmpty()) user.setFirstName(firstName);
        if (lastName != null && !lastName.isEmpty()) user.setLastName(lastName);
        if (phone != null && !phone.isEmpty()) user.setPhone(phone);

        return user;
    }

}
