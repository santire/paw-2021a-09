package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.Tags;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.models.LikeRow;
import ar.edu.itba.paw.persistence.models.RatingRow;
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
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Sql(scripts = "classpath:rating-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RatingJpaDaoTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    private DataSource ds;
    @Autowired
    private RatingJpaDao ratingJpaDao;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);

        // Make sure rating 100 is always inserted before each method
        jdbcTemplate.update("INSERT INTO ratings (rating_id, user_id, restaurant_id, rating) VALUES (100, 2, 1, 3)");
    }

    @After
    public void tearDown() {
        // Delete rating from create rating test
        jdbcTemplate.update("DELETE FROM ratings WHERE user_id=2 AND restaurant_id=2");
    }

    @Test
    public void testGetRating() {
        long userId = 1;
        long restaurantId = 1;
        final Optional<Rating> maybeRating = ratingJpaDao.getRating(userId, restaurantId);

        assertTrue(maybeRating.isPresent());
        final Rating rating = maybeRating.get();

        assertEquals(5.0, rating.getRating(), 0.01);
    }

    @Test
    public void testMakeRating() {
        final long userId = 2L;
        final long restaurantId = 2L;

        // Check rating didn't exist
        List<RatingRow> ratingRowList = jdbcTemplate.query("SELECT * FROM ratings WHERE user_id="+ userId + " AND restaurant_id="+restaurantId, RatingRow.rowMapper);
        assertTrue(ratingRowList.isEmpty());

        // Obtain user and restaurant
        UserRow userRow = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = " + userId, UserRow.rowMapper);
        RestaurantRow restaurantRow = jdbcTemplate.queryForObject("SELECT * FROM restaurants WHERE restaurant_id= " + restaurantId, RestaurantRow.rowMapper);
        User user = userRow.toUser();
        Restaurant restaurant = restaurantRow.toRestaurant(user);

        // Add rating
        final Rating rating = ratingJpaDao.createRating(user, restaurant, 2.5);
        em.flush();

        RatingRow ratingResult = jdbcTemplate.queryForObject("SELECT * FROM ratings where rating_id = " + rating.getId().toString(), RatingRow.rowMapper);

        assertEquals(2.5f, ratingResult.getRating(), 0.01);
    }

    @Test
    public void testDeleteRating() {

        final long userId = 2L;
        final long restaurantId = 1L;
        final double expectedRating = 3;

        // Check rating exists before
        RatingRow ratingRow = jdbcTemplate.queryForObject("SELECT * FROM ratings WHERE user_id="+ userId + " AND restaurant_id="+restaurantId, RatingRow.rowMapper);
        assertEquals(userId, ratingRow.getUserId().longValue());
        assertEquals(restaurantId, ratingRow.getRestaurantId(), 0.01);
        assertEquals(expectedRating, ratingRow.getRating(), 0.01);

        // Obtain user and restaurant
        UserRow userRow = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = " + userId, UserRow.rowMapper);
        RestaurantRow restaurantRow = jdbcTemplate.queryForObject("SELECT * FROM restaurants WHERE restaurant_id= " + restaurantId, RestaurantRow.rowMapper);
        User user = userRow.toUser();
        Restaurant restaurant = restaurantRow.toRestaurant(user);

        // Delete rating
        ratingJpaDao.deleteRating(ratingRow.getId());
        em.flush();

        // Check rating doesn't exist anymore
        List<RatingRow> ratingRowList = jdbcTemplate.query("SELECT * FROM ratings WHERE user_id="+ userId + " AND restaurant_id="+restaurantId, RatingRow.rowMapper);
        assertTrue(ratingRowList.isEmpty());
    }


}
