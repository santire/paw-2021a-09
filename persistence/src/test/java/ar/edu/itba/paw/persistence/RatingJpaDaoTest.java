package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.Rating;
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

import javax.sql.DataSource;
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

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testGetRating(){
        final Optional<Rating> maybeRating = ratingJpaDao.getRating(1, 1);

        assertTrue(maybeRating.isPresent());
        final Rating rating = maybeRating.get();

        assertEquals((double)5.0, (double)rating.getRating(), (double)0.01);
    }
}
