package ar.edu.itba.paw.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
  @SequenceGenerator(sequenceName = "users_user_id_seq", name = "users_user_id_seq", allocationSize = 1)
  private Long id;

  @Column(length = 100)
  private String username;

  @Column(length = 100)
  private String password;

  @Column(length = 100, name = "first_name")
  private String firstName;

  @Column(length = 100, name = "last_name")
  private String lastName;

  @Column(length = 100, unique = true)
  private String email;

  @Column(length = 100)
  private String phone;

  @Column(name = "is_active", columnDefinition = "boolean default false")
  private Boolean active;

  @OneToMany(orphanRemoval = true, mappedBy = "owner")
  private List<Restaurant> ownedRestaurants;

  // private List<Reservation> reservations; ?

  User() {
    // Just for hibernate
  }

  public User(String username, String password, String firstName, String lastName, String email, String phone) {
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.active = false;
  }

  public User(Long id, String username, String password, String firstName, String lastName, String email, String phone,
      Boolean active) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.active = active;
  }

  public User(Long id, String username, String password, String firstName, String lastName, String email,
      String phone) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.active = false;
  }

  public Boolean isActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return this.username;
  }

  public String getName() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return this.phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public List<Restaurant> getOwnedRestaurants() {
    return ownedRestaurants;
  }

  public void setOwnedRestaurants(List<Restaurant> ownedRestaurants) {
    this.ownedRestaurants = ownedRestaurants;
  }

}
