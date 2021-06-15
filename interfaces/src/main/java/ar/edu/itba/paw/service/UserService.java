package ar.edu.itba.paw.service;

import java.util.Optional;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import ar.edu.itba.paw.model.exceptions.TokenExpiredException;

public interface UserService {

  // CREATE
  public User register(final String username,
      final String password,
      final String firstName,
      final String lastName,
      final String email, 
      final String phone,
      String baseUrl) throws EmailInUseException, TokenCreationException;

  // READ
  public Optional<User> findById(long id);
  public Optional<User> findByEmail(String email);
  public boolean isRestaurantOwner(long userId);
  public boolean isTheRestaurantOwner(long userId,long restaurantId);

  // UPDATE
  public User activateUserByToken(String token) throws TokenExpiredException;
  public User updatePasswordByToken(String token, String password) throws TokenExpiredException;
  public void updateUser(long id, String username, String password, String firstName, String lastName, String email, String phone);
  public void requestPasswordReset(String email, String baseUrl) throws TokenCreationException;

}
