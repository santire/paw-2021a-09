package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


@Transactional
@Sql(scripts = "classpath:likes-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class LikeJpaDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private LikeJpaDao likeJpaDao;

    @PersistenceContext
    EntityManager em;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("likes")
                .usingGeneratedKeyColumns("like_id");
    }

    @Test
    public void testGetLikesByUserId(){
        List<Like> likesList = likeJpaDao.getLikesByUserId(1, 5,20L);

        Assert.assertEquals(3, likesList.size());
        Assert.assertEquals(997, likesList.get(2).getRestaurant().getId().longValue());
        Assert.assertEquals(998, likesList.get(1).getRestaurant().getId().longValue());
        Assert.assertEquals(999, likesList.get(0).getRestaurant().getId().longValue());
    }

    @Test
    public void testUserLikesRestaurant(){
        Assert.assertTrue(likeJpaDao.userLikesRestaurant(20,999));
    }

    @Test
    public void testUserDontLikesRestaurant(){
        Assert.assertFalse(likeJpaDao.userLikesRestaurant(21,999));
    }

    @Test
    public void testDislike(){
        long userId = 20;
        long restaurantId = 997;

        SimpleUser user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = " + userId  , (rs, rowNum) ->
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

        List <SimpleLike> retrievedLikes = jdbcTemplate.query("SELECT * FROM likes WHERE user_id = " + user.user_id + " AND restaurant_id = " + restaurant.id, (rs, rowNum) ->
                new SimpleLike(
                        rs.getLong("like_id"),
                        rs.getLong("user_id"),
                        rs.getLong("restaurant_id")
                ));

        assertFalse(retrievedLikes.isEmpty());

        boolean result = likeJpaDao.dislike(user.user_id, restaurant.id);
        em.flush();

        retrievedLikes = jdbcTemplate.query("SELECT * FROM likes WHERE user_id = " + user.user_id + " AND restaurant_id = " + restaurant.id, (rs, rowNum) ->
                new SimpleLike(
                        rs.getLong("like_id"),
                        rs.getLong("user_id"),
                        rs.getLong("restaurant_id")
                ));

        assertTrue(retrievedLikes.isEmpty());



    }

    @Test
    public void testLike(){
        long userId = 21;
        long restaurantId = 997;

        SimpleUser simpleUser = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = " + userId  , (rs, rowNum) ->
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

        SimpleRestaurant simpleRestaurant =  jdbcTemplate.queryForObject( "SELECT * FROM restaurants WHERE restaurant_id= " + restaurantId, (rs, rowNum) ->
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

        Restaurant restaurant = new Restaurant(simpleRestaurant.id, simpleRestaurant.name, simpleRestaurant.address, simpleRestaurant.phoneNumber, null, user, simpleRestaurant.facebook, simpleRestaurant.twitter, simpleRestaurant.instagram);

        Like like = likeJpaDao.like(user, restaurant);
        em.flush();

        SimpleLike retrievedLike = jdbcTemplate.queryForObject("SELECT * FROM likes WHERE like_id = " + like.getId() , (rs, rowNum) ->
                new SimpleLike(
                        rs.getLong("like_id"),
                        rs.getLong("user_id"),
                        rs.getLong("restaurant_id")
                ));

        assertEquals(like.getId(), retrievedLike.like_id);
        assertEquals(like.getRestaurant().getId(), retrievedLike.restaurant_id);
        assertEquals(like.getUser().getId(), retrievedLike.user_id);

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

    private static class SimpleLike {
        Long like_id;
        Long user_id;
        Long restaurant_id;


        public SimpleLike(Long like_id,  Long user_id, Long restaurant_id) {
            this.like_id = like_id;
            this.user_id = user_id;
            this.restaurant_id = restaurant_id;
        }
    }

}
