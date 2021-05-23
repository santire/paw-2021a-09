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
        // Restaurant restaurant2 = restaurantDao.registerRestaurant(NAME, ADDRESS, PHONE_NUMBER, TAGS, OWNER);
        // Restaurant restaurant3 = restaurantDao.registerRestaurant(NAME, ADDRESS, PHONE_NUMBER, TAGS, OWNER);
        // Restaurant restaurantAgain = restaurantDao.findById(1L).orElseThrow(RestaurantNotFoundException::new);


        // System.out.println("RESTAURANT ID: " + restaurant.getId());
        // System.out.println("RESTAURANT ID: " + restaurant2.getId());
        // System.out.println("RESTAURANT ID: " + restaurant3.getId());
        // System.out.println("RESTAURANT ID: " + restaurantAgain.getId());
        // System.out.println("RESTAURANT NAME: " + restaurantAgain.getName());
        // System.out.println("RESTAURANT NAME: " + restaurant.getName());
        // System.out.println("RESTAURANT ADDRESS: " + restaurant.getAddress());
        // System.out.println("RESTAURANT PHONE_NUMBER: " + restaurant.getPhoneNumber());
        // System.out.println("OWNER NAME: " + restaurant.getOwner().getName());
        // System.out.println("OWNER ID: " + restaurant.getOwner().getId());

        assertEquals(NAME, restaurant.getName());
        assertEquals(ADDRESS, restaurant.getAddress());
        assertEquals(PHONE_NUMBER, restaurant.getPhoneNumber());
        assertEquals(1, restaurant.getOwner().getId());

        
        // assertEquals(INSERTED_SIZE + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "restaurants"));
    }

    @Test
    public void testFindById() {

        Optional<Restaurant> maybeRestaurant = restaurantDao.findById(999L);

        assertTrue(maybeRestaurant.isPresent());
        Restaurant restaurant = maybeRestaurant.get();
        
        assertEquals("KFC", restaurant.getName());
        assertEquals("La Pampa 319", restaurant.getAddress());
        assertEquals("1121146545", restaurant.getPhoneNumber());
        assertEquals(999, restaurant.getOwner().getId());

    }

}
