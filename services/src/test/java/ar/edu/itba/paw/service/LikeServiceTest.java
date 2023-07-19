package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.AlreadyLikedException;
import ar.edu.itba.paw.model.exceptions.NotLikedException;
import ar.edu.itba.paw.persistence.LikesDao;
import ar.edu.itba.paw.persistence.RestaurantDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class LikeServiceTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password00";
    private static final String FIRSTNAME = "myfirstname";
    private static final String LASTNAME = "mylastname";
    private static final String EMAIL = "username@email.com";
    private static final String PHONE = "123456789";
    private static final String BASEURL = "/";
    private static final Long ID = 1l;
    private static final String TOKEN = "token1234";

    private static final String RESTAURANT_NAME = "myrestaurant";
    private static final String RESTAURANT_ADDRESS = "9 de Julio";
    private static final String RESTAURANT_PHONE = "46511234";
    private static final Long RESTAURANT_ID = 1l;

    @InjectMocks
    private LikesServiceImpl likesService = new LikesServiceImpl();

    @Mock
    private UserDao userDao;

    @Mock
    private RestaurantDao restaurantDao;

    @Mock
    private LikesDao likesDao;

    @Test(expected = AlreadyLikedException.class)
    public void testLikeAlreadyLiked(){

        User u = new User(ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, PHONE, false);
        u.setActive(true);

        Restaurant r = new Restaurant(RESTAURANT_NAME,
                RESTAURANT_ADDRESS,
                RESTAURANT_PHONE,
                new ArrayList<>(),
                u, "", "", "");
        r.setId(RESTAURANT_ID);


        Mockito.when(userDao.findById(ID)).thenReturn(Optional.of(u));
        Mockito.when(restaurantDao.findById(RESTAURANT_ID)).thenReturn(Optional.of(r));

        Mockito.when(likesDao.userLikesRestaurant(u.getId(), r.getId())).thenReturn(true);

        likesService.like(u.getId(), r.getId());
    }

    @Test(expected = NotLikedException.class)
    public void testDislikeNotLiked(){

        User u = new User(ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, PHONE, false);
        u.setActive(true);

        Restaurant r = new Restaurant(RESTAURANT_NAME,
                RESTAURANT_ADDRESS,
                RESTAURANT_PHONE,
                new ArrayList<>(),
                u, "", "", "");
        r.setId(RESTAURANT_ID);

        Mockito.when(userDao.findById(ID)).thenReturn(Optional.of(u));
        Mockito.when(restaurantDao.findById(RESTAURANT_ID)).thenReturn(Optional.of(r));

        Mockito.when(likesDao.userLikesRestaurant(u.getId(), r.getId())).thenReturn(false);

        likesService.dislike(u.getId(), r.getId());
    }

}
