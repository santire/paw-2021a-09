package ar.edu.itba.paw.persistence;

import java.time.LocalDateTime;
import java.util.Optional;

import ar.edu.itba.paw.model.PasswordToken;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;

public interface UserDao {

  // CREATE
  public User register(final String username, final String password, final String firstName,final String lastName, final String email, final String phone) throws EmailInUseException;
  public void assignTokenToUser(String token, LocalDateTime createdAt, long userId) throws TokenCreationException;
  public void assignPasswordTokenToUser(String token, LocalDateTime createdAt, long userId) throws TokenCreationException;

  // READ
  public Optional<User> findById(long id);
  public Optional<User> findByEmail(String email);
  public Optional<VerificationToken> getToken(String token);
  public Optional<PasswordToken> getPasswordToken(String token);

  // DESTROY
  public void deleteToken(String token);
  public void deleteAssociatedPasswordTokens(User user);
  public void purgeAllExpiredTokensSince(LocalDateTime expiredCreatedAt);

}
