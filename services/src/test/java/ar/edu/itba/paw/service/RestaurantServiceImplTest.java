package ar.edu.itba.paw.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.persistence.RestaurantDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


// @RunWith(MockitoJUnitRunner.class)
// public class RestaurantServiceImplTest {
    // private static final long ID = 1;
    // private static final String NAME = "McDonalds";
    // private static final String ADDRESS = "9 de Julio";
    // private static final String PHONE_NUMBER = "46511234";
    // private static final float RATING = 0;
    // private static final long USER_ID = 1;


    // @InjectMocks
    // private RestaurantServiceImpl restaurantService = new RestaurantServiceImpl();

    // @Mock
    // private RestaurantDao mockDao;

    // @Test
    // public void registerRestaurant(){
        // Mockito.when(mockDao.registerRestaurant(Mockito.eq(NAME), Mockito.eq(ADDRESS), Mockito.eq(PHONE_NUMBER),
                // Mockito.eq(RATING), Mockito.eq(USER_ID))).thenReturn(new Restaurant(
                        // 1, NAME, ADDRESS, PHONE_NUMBER, RATING, USER_ID));
        // Restaurant restaurant = restaurantService.registerRestaurant(NAME, ADDRESS, PHONE_NUMBER, RATING, USER_ID);

        // assertEquals(NAME, restaurant.getName());
        // assertEquals(ADDRESS, restaurant.getAddress());
        // assertEquals(PHONE_NUMBER, restaurant.getPhoneNumber());
        // assertEquals(USER_ID, restaurant.getUserId());
    // }

    // @Test
    // public void deleteRestaurantById(){
        // Mockito.when(mockDao.deleteRestaurantById(Mockito.eq(ID))).thenReturn(true);
        // boolean result = restaurantService.deleteRestaurantById(ID);
        // assertTrue(result);
    // }

    // @Test
    // public void deleteRestaurantByName(){
        // Mockito.when(mockDao.deleteRestaurantByName(Mockito.eq(NAME))).thenReturn(true);
        // boolean result = restaurantService.deleteRestaurantByName(NAME);
        // assertEquals(true, result);
    // }
// }
