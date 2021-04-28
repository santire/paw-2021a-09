package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.util.Optional;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;

public interface UserDao {

  // CREATE
  public Optional<User> register(final String username, final String password, final String firstName,final String lastName, final String email, final String phone);
  public void assignTokenToUser(String token, Timestamp createdAt, long userId);

  // READ
  public Optional<User> findById(long id);
  public Optional<User> findByEmail(String email);
  public Optional<VerificationToken> getToken(String token);

  // UPDATE
  public Optional<User> activateUserById(long userId);
  public void updateUser(long id, String username, String password, String first_name, String last_name, String email, String phone);

  public void updatePassword(long id, String password);
  public void updateUsername(long id, String username);
  public void updateFistName(long id, String first_name);
  public void updateLastName(long id, String last_name);
  public void updatePhone(long id, String phone);
  public void updateEmail(long id, String email);


  // DESTROY
  public void deleteToken(String token);

}
