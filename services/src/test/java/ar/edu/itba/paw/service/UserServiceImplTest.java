/*
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

  @InjectMocks
  private UserServiceImpl userService = new UserServiceImpl();

  @Mock
  private UserDao mockDao;

  @Test
  public void testRegister() {

    Mockito.when(mockDao.register(Mockito.eq(USERNAME))).thenReturn(new User(1, USERNAME));
    User user = userService.register(USERNAME);

    assertEquals(USERNAME, user.getName());
  }

  // TODO: Test many cases, for example: testRegisterEmptyPassword, testRegisterAlreadyExists, ...
}
*/