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
@Sql(scripts = "classpath:comment-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class CommentJpaDaoTest {
    @Autowired
    private DataSource ds;

    @Autowired
    private CommentJpaDao commentJpaDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    final long USER_ID = 1;
    final long RESTAURANT_ID = 1;
    final String USER_COMMENT = "first";

    @Test
    public void testGetComment(){
        final Optional<Comment> maybeComment = commentJpaDao.findById(1);

        assertTrue(maybeComment.isPresent());
        final Comment comment = maybeComment.get();

        assertEquals((long)1, (long)comment.getUser().getId());
        assertEquals((long)1, (long)comment.getRestaurant().getId());
        assertEquals(USER_COMMENT, comment.getUserComment());
    }

    @Test
    public void testFindByUserAndRestaurant(){
        final Optional<Comment> maybeComment = commentJpaDao.findByUserAndRestaurantId(1, 1);

        assertTrue(maybeComment.isPresent());
        Comment comment = maybeComment.get();

        assertEquals(USER_ID, (long)comment.getUser().getId());
        assertEquals(RESTAURANT_ID, (long)comment.getRestaurant().getId());
        assertEquals(USER_COMMENT, comment.getUserComment());
    }
}
