package ar.edu.itba.paw.service;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.UserDao;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private UserDao userDao;

  @Autowired
  private EmailService emailService;

  @Autowired
  private RestaurantService restaurantService;

  @Override
  public Optional<User> findById(long id) {
    return this.userDao.findById(id);
  }

  @Transactional
  @Override
  public User register(String username, String password, String firstName, String lastName, String email,
      String phone) {

    User user = userDao.register(username,encoder.encode(password), firstName, lastName, email, phone);
    emailService.sendRegistrationEmail(email);

    return  user;
  }

  @Override
  public User register(String email) {
    User user = userDao.register("dummy","dummy","dummy","dummy", email,"dummy");
    //emailService.sendRegistrationEmail(email);
    return user;
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userDao.findByEmail(email);
  }

  @Override
  public boolean isTheRestaurantOwner(long userId, long restaurantId) {
    List<Restaurant> restaurants = restaurantService.getRestaurantsFromOwner(userId);
    for (Restaurant r: restaurants) {
      if(r.getId() == restaurantId){
        return true;
      }
    }
    return false;
  }

  @Override
  public void updatePassword(long id, String password) {
      userDao.updatePassword(id, encoder.encode(password));
  }
  @Override
  public void updateUsername(long id, String username) {
      userDao.updateUsername(id, username);
  }
  @Override
  public void updateFistName(long id, String first_name) {
    userDao.updateFistName(id, first_name);
  }
  @Override
  public void updateLastName(long id, String last_name) {
    userDao.updateLastName(id, last_name);
  }
  @Override
  public void updatePhone(long id, String phone) {
    userDao.updatePhone(id, phone);
  }

  @Override
  public void updateEmail(long id, String email) {
      userDao.updateEmail(id, email);
  }

  @Override
  public void updateUser(long id, String username, String password, String first_name, String last_name, String email, String phone) {
    userDao.updateUser(id, username, password, first_name, last_name, email, phone);
  }


}
