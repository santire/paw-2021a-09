package ar.edu.itba.paw.service;


import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.Tags;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RunWith (MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password00";
    private static final String FIRSTNAME = "myfirstname";
    private static final String LASTNAME = "mylastname";
    private static final String EMAIL = "username@email.com";
    private static final String PHONE = "123456789";
    private static final String BASEURL = "/";
    private static final Long ID = 1l;
    private static final String TOKEN = "token1234";


    @InjectMocks
    private UserServiceImpl userService = new UserServiceImpl();

    @Mock
    private UserDao userDao;

    @Mock
    private EmailService emailService;

    @Mock
    PasswordEncoder encoder;

    @Mock
    VerificationToken verificationToken;


    @Test
    public void testCreateUser(){
        Mockito.when(encoder.encode(PASSWORD)).thenReturn(PASSWORD);

        User u = new User(ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, PHONE, false);


        Mockito.when(userDao.register(USERNAME,PASSWORD,FIRSTNAME,LASTNAME,EMAIL,PHONE)).thenReturn(u);
        Mockito.doNothing().when(userDao).assignTokenToUser(Mockito.anyString(),Mockito.any(),Mockito.anyLong());
        Mockito.doNothing().when(emailService).sendRegistrationEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());


        User user = userService.register(USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, PHONE, BASEURL, URI.create(""));

        Assert.assertEquals(USERNAME, user.getUsername());
        Assert.assertEquals(PASSWORD, user.getPassword());
        Assert.assertEquals(FIRSTNAME, user.getFirstName());
        Assert.assertEquals(LASTNAME, user.getLastName());
        Assert.assertEquals(EMAIL, user.getEmail());
        Assert.assertEquals(PHONE, user.getPhone());
        Assert.assertEquals(ID, user.getId());
    }

    @Test
    public void testActivateUser(){

        LocalDateTime time = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());

        User u = new User(ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, PHONE, false);

        Mockito.when(userDao.getToken(TOKEN)).thenReturn(Optional.of(verificationToken));
        Mockito.when(verificationToken.getCreatedAt()).thenReturn(time);
        Mockito.when(verificationToken.getUser()).thenReturn(u);

        Assert.assertEquals(false, u.isActive());

        userService.activateUserByToken(TOKEN);

        Assert.assertEquals(true, u.isActive());
    }

    @Test
    public void testUpdateUser(){

        User u = new User(ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, PHONE, false);
        u.setActive(true);

        Mockito.when(userDao.findById(ID)).thenReturn(Optional.of(u));

        userService.updateUser(ID, "newpassword", "newfirstname", "newlastname", "987654321");

        Assert.assertEquals(encoder.encode("newpassword"), u.getPassword());
        Assert.assertEquals("newfirstname", u.getFirstName());
        Assert.assertEquals("newlastname", u.getLastName());
        Assert.assertEquals("987654321", u.getPhone());
    }
}
