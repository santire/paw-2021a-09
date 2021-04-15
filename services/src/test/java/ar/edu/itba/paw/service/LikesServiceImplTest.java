package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.persistence.LikesDao;
import ar.edu.itba.paw.persistence.RestaurantDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LikesServiceImplTest {
    private static final long USER_ID = 1;

    private static final long RESTAURANT_ID = 1;
    private static final long NON_EXISTENT_RESTAURANT_ID = 10;
    private static final String NAME = "McDonalds";
    private static final String ADDRESS = "9 de Julio";
    private static final String PHONE_NUMBER = "46511234";
    private static final float RATING = 4;

    @InjectMocks
    private LikesServiceImpl likesService = new LikesServiceImpl();

    @Mock
    private LikesDao likesDao;

    @Mock
    private RestaurantDao restaurantDao;

    @Test
    public void like(){
        Mockito.when(likesDao.like(Mockito.eq(USER_ID), Mockito.eq(RESTAURANT_ID)))
                .thenReturn(true);

        boolean success = likesService.like(USER_ID, RESTAURANT_ID);

        assertTrue(success);
    }

    @Test
    public void likeNonExistentRestaurant(){
        boolean success = likesService.like(USER_ID, RESTAURANT_ID);

        assertFalse(success);
    }

    @Test
    public void dislike(){
        Mockito.when(likesDao.dislike(Mockito.eq(USER_ID), Mockito.eq(RESTAURANT_ID)))
                .thenReturn(true);
        Mockito.when(likesDao.userLikesRestaurant(Mockito.eq(USER_ID), Mockito.eq(RESTAURANT_ID)))
                .thenReturn(true);

        boolean success = likesService.dislike(USER_ID, RESTAURANT_ID);

        assertTrue(success);
    }

    @Test
    public void getLikedRestaurants(){
        Mockito.when(likesDao.getLikedRestaurantsId(Mockito.eq(USER_ID)))
                .thenReturn(new ArrayList<>(Collections.singletonList(1L)));
        Mockito.when(restaurantDao.findById(Mockito.eq(RESTAURANT_ID)))
                .thenReturn(java.util.Optional.of(new Restaurant(RESTAURANT_ID, NAME, ADDRESS, PHONE_NUMBER, RATING, USER_ID)));

        List<Restaurant> restaurants = likesService.getLikedRestaurants(USER_ID);

        assertEquals(1, restaurants.size());
    }


}
