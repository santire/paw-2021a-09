package ar.edu.itba.paw.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import ar.edu.itba.paw.model.User;
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

  @Test
  public void testRegister() {

    Mockito.when(mockDao.register(
    Mockito.eq(USERNAME),
    Mockito.eq(PASSWORD),
    Mockito.eq(FIRST_NAME),
    Mockito.eq(LAST_NAME),
    Mockito.eq(EMAIL),
    Mockito.eq(PHONE)
    
    )).thenReturn(
      new User(1, USERNAME,PASSWORD,FIRST_NAME,LAST_NAME,EMAIL,PHONE));
    
    
    User user = userService.register(USERNAME,PASSWORD,FIRST_NAME,LAST_NAME,EMAIL,PHONE);

    assertEquals(USERNAME, user.getName());
    assertEquals(USERNAME, user.getUsername());
    assertEquals(PASSWORD, user.getPassword());
    assertEquals(FIRST_NAME, user.getFirst_name());
    assertEquals(LAST_NAME, user.getLast_name());
    assertEquals(EMAIL, user.getEmail());
    assertEquals(PHONE, user.getPhone());
  }

  // TODO: Test many cases, for example: testRegisterEmptyPassword, testRegisterAlreadyExists, ...
}
