package ar.edu.itba.paw.service;

import java.util.Optional;

import ar.edu.itba.paw.model.User;

public interface UserService {

  public Optional<User> findById(long id);

  public User register(final String username, final String password, final String firstName,final String lastName, final String email, final String phone);

  public User register(final String email);

  public Optional<User> findByEmail(String email);

  public boolean isTheRestaurantOwner(long userId,long restaurantId);

  public User activateUserByToken(String token);

  public void updatePassword(long id, String password);
  public void updateUsername(long id, String username);
  public void updateFistName(long id, String first_name);
  public void updateLastName(long id, String last_name);
  public void updatePhone(long id, String phone);
  public void updateEmail(long id, String email);
  public void updateUser(long id, String username, String password, String first_name, String last_name, String email, String phone);

}
