package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.Tags;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.models.RestaurantRow;
import ar.edu.itba.paw.persistence.models.UserRow;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// @Rollback
@Transactional
@Sql(scripts = "classpath:restaurant-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RestaurantJpaDaoTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    private DataSource ds;
    @Autowired
    private RestaurantJpaDao restaurantDao;
    private JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);

        Object[] restaurant = new Object[]{10000L, "MODIFY_TEST_REST", "ADDRESS", "12341234",0, 997L, "f", "t", "i"};

        String sql = "INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id, facebook, twitter, instagram) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Insert test restaurant for delete
        jdbcTemplate.update(sql, restaurant);
    }
    @After
    public void tearDown() {
        Object[] restaurantId = new Object[]{10000L};

        String sql = "DELETE FROM restaurants WHERE restaurant_id=?";

        // Delete test restaurant for delete
        jdbcTemplate.update(sql, restaurantId);
    }

    @Test
    public void testRegister() {
        // Obtain user
        final long userId = 999;
        final UserRow userRow = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = " + userId, UserRow.rowMapper);
        User user = userRow.toUser();

        final String name = "McDonalds";
        final String address = "9 de Julio";
        final String phoneNumber = "46511234";

        final List<Tags> tagsList = new ArrayList<>();
        tagsList.add(Tags.ARGENTINO);

        final String facebook = "facebook.com/myprofile";
        final String instagram = "instagram.com/myprofile";
        final String twitter = "twitter.com/myprofile";


        Restaurant restaurant = restaurantDao.registerRestaurant(name, address, phoneNumber, tagsList, user, facebook, twitter, instagram);
        em.flush();

        final String sql = String.format("SELECT * FROM restaurants WHERE restaurant_id=%d", restaurant.getId());
        RestaurantRow restaurantRow = jdbcTemplate.queryForObject(sql, RestaurantRow.rowMapper);

        assertEquals(restaurant.getId(), restaurantRow.getId());

        assertEquals(name, restaurantRow.getName());
        assertEquals(address, restaurantRow.getAddress());
        assertEquals(phoneNumber, restaurantRow.getPhoneNumber());
        assertEquals(userId, restaurantRow.getOwnerId().longValue());
        assertEquals(facebook, restaurantRow.getFacebook());
        assertEquals(instagram, restaurantRow.getInstagram());
        assertEquals(twitter, restaurantRow.getTwitter());
    }

    @Test
    public void testFindById() {
        final Optional<Restaurant> maybeRestaurant = restaurantDao.findById(999L);
        assertTrue(maybeRestaurant.isPresent());
        Restaurant restaurant = maybeRestaurant.get();

        assertEquals("KFC", restaurant.getName());
        assertEquals("La Pampa 319", restaurant.getAddress());
        assertEquals("1121146545", restaurant.getPhoneNumber());
        assertEquals(999L, restaurant.getOwner().getId().longValue());
    }

    @Test
    public void testRestaurantNotFound() {
        final Optional<Restaurant> maybeRestaurant = restaurantDao.findById(950L);
        assertFalse(maybeRestaurant.isPresent());
    }

    @Test(expected = RestaurantNotFoundException.class)
    public void testDeleteRestaurantNotFound() {
        restaurantDao.deleteRestaurantById(950L);
    }

    @Test
    public void testGetPopularRestaurants() {
        final List<Restaurant> restaurantList = restaurantDao.getPopularRestaurants(2, 1);

        assertEquals(2, restaurantList.size());

        assertEquals("BurgerQueen", restaurantList.get(1).getName());
        assertEquals("BurgerKing", restaurantList.get(0).getName());
    }

    @Test
    public void testGetRestaurantsFromOwnerOnePerPage() {
        long userId = 999;
        List<Restaurant> restaurantList = restaurantDao.getRestaurantsFromOwner(3, 1, userId);

        assertEquals(1, restaurantList.size());
        assertEquals("BurgerQueen", restaurantList.get(0).getName());
    }

    @Test
    public void testGetRestaurantsFromOwnerTwoPerPage() {
        long userId = 999;
        List<Restaurant> restaurantList = restaurantDao.getRestaurantsFromOwner(2, 2, userId);
        assertEquals(2, restaurantList.size());
        assertEquals("BurgerQueen", restaurantList.get(0).getName());
        assertEquals("KFC", restaurantList.get(1).getName());
    }

    @Test
    public void testDeleteRestaurant() {
        long restaurantId = 10000L;

        // Check Restaurant existed before
        final String sql = String.format("SELECT * FROM restaurants WHERE restaurant_id=%d", restaurantId);
        RestaurantRow restaurantRow = jdbcTemplate.queryForObject(sql, RestaurantRow.rowMapper);

        assertEquals(restaurantId, restaurantRow.getId().longValue());
        assertEquals("MODIFY_TEST_REST", restaurantRow.getName());
        assertEquals("ADDRESS", restaurantRow.getAddress());
        assertEquals("12341234", restaurantRow.getPhoneNumber());
        assertEquals("f", restaurantRow.getFacebook());
        assertEquals("t", restaurantRow.getTwitter());
        assertEquals("i", restaurantRow.getInstagram());

        // Delete Restaurant
        restaurantDao.deleteRestaurantById(restaurantId);
        em.flush();

        // Check doesn't exist anymore
        List<RestaurantRow> restaurantRowList = jdbcTemplate.query(sql, RestaurantRow.rowMapper);
        assertTrue(restaurantRowList.isEmpty());
    }

}
