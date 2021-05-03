package ar.edu.itba.paw.service;

import java.util.Optional;

import ar.edu.itba.paw.model.User;

public interface UserService {

  public Optional<User> findById(long id);

  public User register(final String username, final String password, final String firstName,final String lastName, final String email, final String phone);

  public User register(final String email);

  public Optional<User> findByEmail(String email);

  public boolean isRestaurantOwner(long userId);

  public boolean isTheRestaurantOwner(long userId,long restaurantId);

  public User activateUserByToken(String token);

  public void updateUser(long id, String username, String password, String firstName, String lastName, String email, String phone);

}
