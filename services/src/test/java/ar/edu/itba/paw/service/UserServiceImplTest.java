package ar.edu.itba.paw.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import ar.edu.itba.paw.persistence.UserDao;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private static final String FIRST_NAME = "first_name";
  private static final String LAST_NAME = "last_name";
  private static final String EMAIL = "email";
  private static final String PHONE = "phone";


  @InjectMocks
  private UserServiceImpl userService = new UserServiceImpl();

  @Mock
  private UserDao mockDao;

  @Mock
  private EmailService mockEmailService;

  @Mock
  private PasswordEncoder encoder;

  @Test
  public void testRegister() throws EmailInUseException, TokenCreationException {

    Mockito.when(mockDao.register(
    Mockito.eq(USERNAME),
    Mockito.eq(PASSWORD),
    Mockito.eq(FIRST_NAME),
    Mockito.eq(LAST_NAME),
    Mockito.eq(EMAIL),
    Mockito.eq(PHONE)
    
    )).thenReturn(
      (new User(1, USERNAME,PASSWORD,FIRST_NAME,LAST_NAME,EMAIL,PHONE)));

    Mockito.when(encoder.encode(PASSWORD)).thenReturn(PASSWORD);

    User user= userService.register(USERNAME,PASSWORD,FIRST_NAME,LAST_NAME,EMAIL,PHONE);

    assertEquals(USERNAME, user.getName());
    assertEquals(USERNAME, user.getUsername());
    assertEquals(PASSWORD, user.getPassword());
    assertEquals(FIRST_NAME, user.getFirstName());
    assertEquals(LAST_NAME, user.getLastName());
    assertEquals(EMAIL, user.getEmail());
    assertEquals(PHONE, user.getPhone());
    assertEquals(1, 1);
  }

  // TODO: Test many cases, for example: testRegisterEmptyPassword, testRegisterAlreadyExists, ...
}
