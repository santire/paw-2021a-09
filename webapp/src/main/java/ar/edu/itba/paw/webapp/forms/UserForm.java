package ar.edu.itba.paw.webapp.forms;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserForm {



  @Size(min = 6, max = 100)
  @Pattern(regexp = "[a-zA-Z0-9]+")
  private String username;

  @Size(min = 8, max = 100)
  private String password;

  @Size(min = 8, max = 100)
  private String repeatPassword;

  @Size(min = 2, max = 100)
  private String first_name;

  @Size(min = 2, max = 100)
  private String last_name;

  @Size(min = 6, max = 100)
  //@Pattern(regexp = "/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/")
  @Email
  private String email;

  @Size(min = 6, max = 15)
  private String phone;

  public UserForm() {
  }


  public String getUsername() {
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

  public String getRepeatPassword() {
    return this.repeatPassword;
  }

  public void setRepeatPassword(String repeatPassword) {
    this.repeatPassword = repeatPassword;
  }

  public String getFirst_name() {
    return this.first_name;
  }

  public void setFirst_name(String first_name) {
    this.first_name = first_name;
  }

  public String getLast_name() {
    return this.last_name;
  }

  public void setLast_name(String last_name) {
    this.last_name = last_name;
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
