package ar.edu.itba.paw.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.model.User;

@Repository
public class UserDaoInMemoryImpl implements UserDao {

  // Mock of a db. Just for show :)
  private Map<String, User> users = new ConcurrentHashMap<>();

  public UserDaoInMemoryImpl() {
    User user = new User("1", "Leo", "1234");
    User user2 = new User("2", "Alvaro", "12345");
    User user3 = new User("3", "Pepe", "123456");

    users.put("1", user);
    users.put("2", user2);
    users.put("3", user3);
  }

  @Override
  public User get(String id) {
    return this.users.get(id);
  }

  @Override
  public List<User> list() {
    return new ArrayList<>(this.users.values());
  }

  @Override
  public User save(User user) {
    return this.users.put(user.getId(), user);
  }

}
