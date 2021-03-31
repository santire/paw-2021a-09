package ar.edu.itba.paw.model;

public class User {

  private final long id;
  private String name;
  private String password;

  public User(long id) {
    this.id = id;
  }

  public User(long id, String name, String password) {
    this.id = id;
    this.name = name;
    this.password = password;
  }

  public User(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}