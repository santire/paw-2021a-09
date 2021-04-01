package ar.edu.itba.paw.model;

public class User {

  private final long id;
  private String username;
  private String password;
  private String fullname;
  private String email;
  private String phone;

  public User(long id) {
    this.id = id;
  }

  public User(long id, String username, String password, String fullname, String email, String phone) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.fullname = fullname;
    this.email = email;
    this.phone = phone;
  }

  public long getId() {
    return this.id;
  }


  public String getName() {
    return this.username;
  }

  public void setName(String username) {
    this.username = username;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFullname() {
    return this.fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
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

}