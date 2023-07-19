package ar.edu.itba.paw.service;

import java.util.Optional;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import ar.edu.itba.paw.model.exceptions.TokenExpiredException;
import ar.edu.itba.paw.model.exceptions.UsernameInUseException;

public interface UserService {

  // CREATE
  User register(final String username,
                       final String password,
                       final String firstName,
                       final String lastName,
                       final String email,
                       final String phone,
                       String baseUrl) throws EmailInUseException, TokenCreationException, UsernameInUseException;

  // READ
  Optional<User> findById(long id);
  Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);
  boolean isRestaurantOwner(long userId);
  boolean isTheRestaurantOwner(long userId,long restaurantId);

  // UPDATE
  User activateUserByToken(String token) throws TokenExpiredException;
  User updatePasswordByToken(String token, String password) throws TokenExpiredException;
  void updateUser(long id, String password, String firstName, String lastName, String phone);
  void requestPasswordReset(String email, String baseUrl) throws TokenCreationException;

}
