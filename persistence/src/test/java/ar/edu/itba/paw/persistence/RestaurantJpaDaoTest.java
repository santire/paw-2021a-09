package ar.edu.itba.paw.persistence;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.Tags;

import ar.edu.itba.paw.persistence.config.TestConfig;

// @Rollback
@Transactional
@Sql(scripts = "classpath:restaurant-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RestaurantJpaDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private RestaurantJpaDao restaurantDao;

    // General purpose
    private static final int INSERTED_SIZE = 3;

    // Restaurant to insert
    private static final String NAME = "McDonalds";
    private static final String ADDRESS = "9 de Julio";
    private static final String PHONE_NUMBER = "46511234";
    private static final User OWNER = new User(1L, "mluque", "123456", "manuel", "luque", "mluque@itba.edu.ar",
            "1135679821", true);
    private static final List<Tags> TAGS = Arrays.asList(Tags.ARGENTINO);

    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testRegister() {
        Restaurant restaurant = restaurantDao.registerRestaurant(NAME, ADDRESS, PHONE_NUMBER, TAGS, OWNER, "", "", "");

        assertEquals(NAME, restaurant.getName());
        assertEquals(ADDRESS, restaurant.getAddress());
        assertEquals(PHONE_NUMBER, restaurant.getPhoneNumber());
        assertEquals(Long.valueOf(1), restaurant.getOwner().getId());

        Optional<Restaurant> maybeRestaurant = restaurantDao.findById(restaurant.getId());
        assertTrue(maybeRestaurant.isPresent());
    }

    @Test
    public void testFindById() {

        Optional<Restaurant> maybeRestaurant = restaurantDao.findById(999L);

        assertTrue(maybeRestaurant.isPresent());
        Restaurant restaurant = maybeRestaurant.get();

        assertEquals("KFC", restaurant.getName());
        assertEquals("La Pampa 319", restaurant.getAddress());
        assertEquals("1121146545", restaurant.getPhoneNumber());
        assertEquals(Long.valueOf(999), restaurant.getOwner().getId());
    }

    @Test
    public void testFindByIdWithMenu() {

        Optional<Restaurant> maybeRestaurant = restaurantDao.findByIdWithMenu(1,5,999L);

        assertTrue(maybeRestaurant.isPresent());
        Restaurant restaurant = maybeRestaurant.get();
        List<MenuItem> menuActual = restaurant.getMenuPage();

        List<MenuItem> menuExpected = Arrays.asList(
                new MenuItem("Fried Chicken Original", "Delicious fried chicken", 3.99f),
                new MenuItem("Fried Chicken Crispy", "Delicious fried chicken but crispy", 4.99f),
                new MenuItem("Vegan Friendly Option", "It's literally just water", 10.99f));

        assertEquals("KFC", restaurant.getName());
        assertEquals("La Pampa 319", restaurant.getAddress());
        assertEquals("1121146545", restaurant.getPhoneNumber());
        assertEquals(Long.valueOf(999), restaurant.getOwner().getId());

        assertEquals(menuExpected.get(0).getName(), menuActual.get(0).getName());
        assertEquals(menuExpected.get(0).getDescription(), menuActual.get(0).getDescription());
        assertEquals(menuExpected.get(0).getPrice(), menuActual.get(0).getPrice(), 0.01);

        assertEquals(menuExpected.get(1).getName(), menuActual.get(1).getName());
        assertEquals(menuExpected.get(1).getDescription(), menuActual.get(1).getDescription());
        assertEquals(menuExpected.get(1).getPrice(), menuActual.get(1).getPrice(), 0.01);

        assertEquals(menuExpected.get(2).getName(), menuActual.get(2).getName());
        assertEquals(menuExpected.get(2).getDescription(), menuActual.get(2).getDescription());
        assertEquals(menuExpected.get(2).getPrice(), menuActual.get(2).getPrice(), 0.01);
    }

    @Test
    public void testRestaurantNotFound() {
        Optional<Restaurant> maybeRestaurant = restaurantDao.findById(950L);
        assertFalse(maybeRestaurant.isPresent());
    }

    @Test(expected = RestaurantNotFoundException.class)
    public void testDeleteRestaurantNotFound() {
        restaurantDao.deleteRestaurantById(950L);
    }

    /*
    // Fails because of postgresql specific syntax
    @Test
    public void testFindRestaurantsLikeKfc() {
        final String searchTerm = "kf";
        List<Tags> tags = new ArrayList<>();
//        tags.add(Tags.AMERICANO);
        final List<Restaurant> filteredRestaurants = restaurantDao.getRestaurantsFilteredBy(1, INSERTED_SIZE, searchTerm, tags, 1, 1000, Sorting.NAME, false, 900);

        final Restaurant restaurant = filteredRestaurants.get(0);

        assertEquals("KFC", restaurant.getName());
        assertEquals("La Pampa 319", restaurant.getAddress());
        assertEquals("1121146545", restaurant.getPhoneNumber());
        assertEquals(Long.valueOf(999), restaurant.getOwner().getId());
    }
*/
    @Test
    public void testGetPopularRestaurants() {
        final List<Restaurant> restaurantList = restaurantDao.getPopularRestaurants(2, 1);

        assertEquals(2, restaurantList.size());

        assertEquals("BurgerQueen", restaurantList.get(1).getName());
        assertEquals("BurgerKing", restaurantList.get(0).getName());
    }
    @Test
    public void testMenuBelongsToRestaurant() {
        assertTrue(restaurantDao.menuBelongsToRestaurant(999l,999l));
        assertFalse(restaurantDao.menuBelongsToRestaurant(999l,996l));
    }

    @Test
    public void testGetRestaurantsFromOwner() {
        List<Restaurant> restaurantList = restaurantDao.getRestaurantsFromOwner(3,1, 999);

        assertEquals(1, restaurantList.size());
        assertEquals("BurgerQueen", restaurantList.get(0).getName());

        restaurantList = restaurantDao.getRestaurantsFromOwner(2,2, 999);
        assertEquals(2, restaurantList.size());
        assertEquals("BurgerQueen", restaurantList.get(0).getName());
        assertEquals("KFC", restaurantList.get(1).getName());
    }

    @Test
    public void testDeleteRestaurant() {
        long ID = 996l;

        Optional<Restaurant> maybeRestaurant = restaurantDao.findById(ID);
        assertTrue(maybeRestaurant.isPresent());
        final Restaurant r = maybeRestaurant.get();

        restaurantDao.deleteRestaurantById(r.getId());

        Optional<Restaurant> notRestaurant = restaurantDao.findById(ID);
        assertFalse(notRestaurant.isPresent());
    }
}
