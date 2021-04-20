package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.persistence.config.TestConfig;
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

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Rollback
@Sql(scripts = "classpath:rating-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RatingDaoImplTest {
    private static final int RATINGS_INSERTED_SIZE = 3;

    private static final long USER_ID = 1;
    private static final long RESTAURANT_ID = 1;
    private static final long ANOTHER_RESTAURANT_ID = 2;

    private static final int RATING = 5;
    private static final int ANOTHER_RATING = 2;


    @Autowired
    private DataSource ds;

    @Autowired
    private RatingDaoImpl ratingDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void getRating(){
        Optional<Rating> rating = ratingDao.getRating(USER_ID, RESTAURANT_ID);

        assertTrue(rating.isPresent());
        assertEquals(RATING, rating.get().getRating());
        assertEquals(USER_ID, rating.get().getUserId());
        assertEquals(RESTAURANT_ID, rating.get().getRestaurantId());
    }

    @Test
    public void rateRestaurant(){
        Rating rating = ratingDao.rateRestaurant(USER_ID, ANOTHER_RESTAURANT_ID, ANOTHER_RATING);

        assertEquals(RATINGS_INSERTED_SIZE + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "ratings"));
        assertEquals(ANOTHER_RATING, rating.getRating());
    }

    @Test
    public void modifyRating(){
        boolean success = ratingDao.modifyRestaurantRating(USER_ID, RESTAURANT_ID, ANOTHER_RATING);

        assertTrue(success);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "ratings", "rating = " + ANOTHER_RATING));
    }

    @Test
    public void getRatedRestaurantByUserId(){
        List<Rating> ratings = ratingDao.getRatedRestaurantsByUserId(USER_ID);

        assertEquals(RATINGS_INSERTED_SIZE-1, ratings.size());
    }

    @Test
    public void getNumberOfRates(){
        int number = ratingDao.getNumberOfRates(RESTAURANT_ID);

        assertEquals(2, number);
    }
}
