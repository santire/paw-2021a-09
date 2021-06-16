package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
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

    private JdbcTemplate jdbcTemplate;

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
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testRegister() {
        Restaurant restaurant = restaurantDao.registerRestaurant(NAME, ADDRESS, PHONE_NUMBER, TAGS, OWNER);
        // Restaurant restaurant2 = restaurantDao.registerRestaurant(NAME, ADDRESS,
        // PHONE_NUMBER, TAGS, OWNER);
        // Restaurant restaurant3 = restaurantDao.registerRestaurant(NAME, ADDRESS,
        // PHONE_NUMBER, TAGS, OWNER);
        // Restaurant restaurantAgain =
        // restaurantDao.findById(1L).orElseThrow(RestaurantNotFoundException::new);

        // System.out.println("RESTAURANT ID: " + restaurant.getId());
        // System.out.println("RESTAURANT ID: " + restaurant2.getId());
        // System.out.println("RESTAURANT ID: " + restaurant3.getId());
        // System.out.println("RESTAURANT ID: " + restaurantAgain.getId());
        // System.out.println("RESTAURANT NAME: " + restaurantAgain.getName());
        // System.out.println("RESTAURANT NAME: " + restaurant.getName());
        // System.out.println("RESTAURANT ADDRESS: " + restaurant.getAddress());
        // System.out.println("RESTAURANT PHONE_NUMBER: " +
        // restaurant.getPhoneNumber());
        // System.out.println("OWNER NAME: " + restaurant.getOwner().getName());
        // System.out.println("OWNER ID: " + restaurant.getOwner().getId());

        assertEquals(NAME, restaurant.getName());
        assertEquals(ADDRESS, restaurant.getAddress());
        assertEquals(PHONE_NUMBER, restaurant.getPhoneNumber());
        assertEquals(Long.valueOf(1), restaurant.getOwner().getId());

        // assertEquals(INSERTED_SIZE + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate,
        // "restaurants"));
    }

    // @Test
    public void testFindById() {

        Optional<Restaurant> maybeRestaurant = restaurantDao.findById(999L);

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
    public void testFindAllRestaurantsPageCount() {
        // There are 3 restaurants, so:

        // When amount on page is 1 there should be 3 pages
        assertEquals(3, restaurantDao.getAllRestaurantPagesCount(1, ""));

        // When amount on page is 2 there should be 3 pages
        assertEquals(2, restaurantDao.getAllRestaurantPagesCount(2, ""));

        // When amount on page is >=3 there should be 1 pages
        assertEquals(1, restaurantDao.getAllRestaurantPagesCount(3, ""));
        assertEquals(1, restaurantDao.getAllRestaurantPagesCount(5, ""));
    }

    @Test
    public void testFindAllRestaurants() {
        final int amountOnPage = 2;
        final int expectedPage1 = 2;
        final int expectedPage2 = 1;

        final List<Restaurant> allRestaurantsPage1 = restaurantDao.getAllRestaurants(1, amountOnPage, "");
        final List<Restaurant> allRestaurantsPage2 = restaurantDao.getAllRestaurants(2, amountOnPage, "");

        assertEquals(expectedPage1, allRestaurantsPage1.size());
        assertEquals(expectedPage2, allRestaurantsPage2.size());
    }

    @Test
    public void testFindRestaurantsLikeKfc() {
        final String searchTerm = "kf";
        final List<Restaurant> filteredRestaurants = restaurantDao.getAllRestaurants(1, INSERTED_SIZE, searchTerm);

        // I should only have 1 page with just kfc
        assertEquals(1, restaurantDao.getAllRestaurantPagesCount(INSERTED_SIZE, searchTerm));
        assertEquals(1, filteredRestaurants.size());

        final Restaurant restaurant = filteredRestaurants.get(0);

        assertEquals("KFC", restaurant.getName());
        assertEquals("La Pampa 319", restaurant.getAddress());
        assertEquals("1121146545", restaurant.getPhoneNumber());
        assertEquals(Long.valueOf(999), restaurant.getOwner().getId());
    }

    @Test
    public void testNoResultsStillOnePage() {
        final String searchTerm = "This string doesn't match any restaurant";
        final List<Restaurant> filteredRestaurants = restaurantDao.getAllRestaurants(1, INSERTED_SIZE, searchTerm);

        // I should only have 1 page with no results
        assertEquals(1, restaurantDao.getAllRestaurantPagesCount(INSERTED_SIZE, searchTerm));
        assertEquals(0, filteredRestaurants.size());
    }
}
