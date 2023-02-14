package ar.edu.itba.paw.persistence;

import java.time.LocalDateTime;
import java.util.Optional;

import ar.edu.itba.paw.model.PasswordToken;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;


public interface UserDao {

  // CREATE
  User register(final String username, final String password, final String firstName,final String lastName, final String email, final String phone);
  void assignTokenToUser(String token, LocalDateTime createdAt, long userId);
  void assignPasswordTokenToUser(String token, LocalDateTime createdAt, long userId);

  // READ
  Optional<User> findById(long id);
  Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);
  Optional<VerificationToken> getToken(String token);
  Optional<PasswordToken> getPasswordToken(String token);

  // DESTROY
  void deleteToken(String token);
  void deleteAssociatedPasswordTokens(User user);
  void purgeAllExpiredTokensSince(LocalDateTime expiredCreatedAt);

}
