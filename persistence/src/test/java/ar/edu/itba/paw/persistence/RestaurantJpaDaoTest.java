package ar.edu.itba.paw.persistence;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;

import org.junit.Assert;
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
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    EntityManager em;


    // General purpose
    private static final int INSERTED_SIZE = 3;

    // Restaurant to insert
    private static final String NAME = "McDonalds";
    private static final String ADDRESS = "9 de Julio";
    private static final String PHONE_NUMBER = "46511234";

    private static final List<Tags> TAGS = Arrays.asList(Tags.ARGENTINO);

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testRegister() {
        SimpleUser simpleUser = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = " + 999  , (rs, rowNum) ->
                new SimpleUser(
                        rs.getLong("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("is_active")
                ));

        User user = new User(simpleUser.user_id, simpleUser.username, simpleUser.password, simpleUser.first_name, simpleUser.last_name, simpleUser.email, simpleUser.phone, simpleUser.is_active);


        Restaurant restaurant = restaurantDao.registerRestaurant(NAME, ADDRESS, PHONE_NUMBER, TAGS, user, "facebook.com/myprofile", "twitter.com/myprofile", "instagram.com/myprofile");
        em.flush();

        SimpleRestaurant retrievedRestaurant =  jdbcTemplate.queryForObject( "SELECT * FROM restaurants WHERE restaurant_id= " + restaurant.getId().toString(), (rs, rowNum) ->
                        new SimpleRestaurant(
                                rs.getLong("restaurant_id"),
                                rs.getString("name"),
                                rs.getString("address"),
                                rs.getString("phone_number"),
                                rs.getFloat("rating"),
                                rs.getLong("user_id"),
                                rs.getString("facebook"),
                                rs.getString("instagram"),
                                rs.getString("twitter")
                        )
        );

        assertEquals(restaurant.getId(), retrievedRestaurant.id);
        assertEquals(restaurant.getName(), retrievedRestaurant.name);
        assertEquals(restaurant.getAddress(), retrievedRestaurant.address);
        assertEquals(restaurant.getPhoneNumber(), retrievedRestaurant.phoneNumber);
        assertEquals(restaurant.getOwner().getId(), retrievedRestaurant.ownerId);
        assertEquals(restaurant.getFacebook(), retrievedRestaurant.facebook);
        assertEquals(restaurant.getInstagram(), retrievedRestaurant.instagram);
        assertEquals(restaurant.getTwitter(), retrievedRestaurant.twitter);
    }

    @Test
    public void testFindById() {

        Optional<Restaurant> maybeRestaurant = restaurantDao.findById(999L);

        assertTrue(maybeRestaurant.isPresent());
        Restaurant restaurant = maybeRestaurant.get();

        SimpleRestaurant retrievedRestaurant =  jdbcTemplate.queryForObject( "SELECT * FROM restaurants WHERE restaurant_id= " + restaurant.getId().toString(), (rs, rowNum) ->
                new SimpleRestaurant(
                        rs.getLong("restaurant_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getFloat("rating"),
                        rs.getLong("user_id"),
                        rs.getString("facebook"),
                        rs.getString("instagram"),
                        rs.getString("twitter")
                )
        );

        assertEquals(retrievedRestaurant.name, restaurant.getName());
        assertEquals(retrievedRestaurant.address, restaurant.getAddress());
        assertEquals(retrievedRestaurant.phoneNumber, restaurant.getPhoneNumber());
        assertEquals(retrievedRestaurant.id, restaurant.getOwner().getId());
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
        List<Restaurant> restaurantList = restaurantDao.getRestaurantsFromOwner(3,1, userId);

        assertEquals(1, restaurantList.size());
        assertEquals("BurgerQueen", restaurantList.get(0).getName());
    }

    @Test
    public void testGetRestaurantsFromOwnerTwoPerPage() {
        long userId = 999;
        List<Restaurant> restaurantList =  restaurantDao.getRestaurantsFromOwner(2,2, userId);
        assertEquals(2, restaurantList.size());
        assertEquals("BurgerQueen", restaurantList.get(0).getName());
        assertEquals("KFC", restaurantList.get(1).getName());
    }

    @Test
    public void testDeleteRestaurant() {
        long restaurantId = 996;

        SimpleRestaurant restaurant =  jdbcTemplate.queryForObject( "SELECT * FROM restaurants WHERE restaurant_id= " + restaurantId, (rs, rowNum) ->
                new SimpleRestaurant(
                        rs.getLong("restaurant_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getFloat("rating"),
                        rs.getLong("user_id"),
                        rs.getString("facebook"),
                        rs.getString("instagram"),
                        rs.getString("twitter")
                )
        );

        restaurantDao.deleteRestaurantById(restaurant.id);
        em.flush();

        List <SimpleRestaurant> retrievedRestaurant =  jdbcTemplate.query( "SELECT * FROM restaurants WHERE restaurant_id= " + restaurantId, (rs, rowNum) ->
                new SimpleRestaurant(
                        rs.getLong("restaurant_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getFloat("rating"),
                        rs.getLong("user_id"),
                        rs.getString("facebook"),
                        rs.getString("instagram"),
                        rs.getString("twitter")
                )
        );

        assertTrue(retrievedRestaurant.isEmpty());
    }

    private class SimpleRestaurant {
        Long id;
        String name;
        String address;
        String phoneNumber;
        Long ownerId;
        Float rating;
        String facebook;
        String instagram;
        String twitter;

        public SimpleRestaurant(Long id, String name, String address, String phoneNumber,  Float rating,Long ownerId, String facebook, String instagram, String twitter) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.ownerId = ownerId;
            this.rating = rating;
            this.facebook = facebook;
            this.instagram = instagram;
            this.twitter = twitter;
        }
    }
    private static class SimpleUser {
        Long user_id;
        String username;
        String password;
        String first_name;
        String last_name;
        String email;
        String phone;
        boolean is_active;

        public SimpleUser(Long user_id,  String username, String password, String first_name, String last_name, String email, String phone, boolean is_active) {
            this.user_id = user_id;
            this.username = username;
            this.password = password;
            this.first_name = first_name;
            this.last_name = last_name;
            this.email = email;
            this.phone = phone;
            this.is_active = is_active;
        }
    }
}
