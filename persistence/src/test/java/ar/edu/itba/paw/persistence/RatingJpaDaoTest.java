package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.Tags;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Transactional
@Sql(scripts = "classpath:rating-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RatingJpaDaoTest {
    @Autowired
    private DataSource ds;

    @Autowired
    private RatingJpaDao ratingJpaDao;

    private JdbcTemplate jdbcTemplate;
    @PersistenceContext
    EntityManager em;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testGetRating() {
        long userId = 1;
        long restaurantId = 1;
        final Optional<Rating> maybeRating = ratingJpaDao.getRating(userId, restaurantId);

        assertTrue(maybeRating.isPresent());
        final Rating rating = maybeRating.get();
        List<SimpleRating> list = jdbcTemplate.query("SELECT * FROM ratings", (rs, rowNum) ->
                new SimpleRating(
                        rs.getFloat("rating"),
                        rs.getLong("rating_id"),
                        rs.getLong("user_id"),
                        rs.getLong("restaurant_id")));

        list.forEach(r -> System.out.println(r.rating));

        assertEquals((double) 5.0, (double) rating.getRating(), (double) 0.01);
    }

    @Test
    public void testMakeRating() {
        User user = new User(1L, "mluque", "12345678", "manuel", "luque", "mluque@itba.edu.ar", "1135679821", true);
        Restaurant restaurant = new Restaurant("BurgerKing", "Mendoza 2929", "1123346545", new ArrayList<Tags>(), user, "", "", "");
        restaurant.setId(1L);

        final Rating rating = ratingJpaDao.createRating(user, restaurant, 2.5);
        em.flush();


        SimpleRating ratingResult = jdbcTemplate.queryForObject("SELECT * FROM ratings where rating_id = " + rating.getId().toString(), (rs, rowNum) ->
                new SimpleRating(
                        rs.getFloat("rating"),
                        rs.getLong("rating_id"),
                        rs.getLong("user_id"),
                        rs.getLong("restaurant_id")));

        assertEquals( 2.5f, ratingResult.rating,  0.01);
    }

    private static class SimpleRating {
        float rating;
        long rating_id;
        long user_id;
        long restaurant_id;

        public SimpleRating(float rating, long rating_id, long user_id, long restaurant_id) {
            this.rating = rating;
            this.rating_id = rating_id;
            this.user_id = user_id;
            this.restaurant_id = restaurant_id;
        }
    }
}
