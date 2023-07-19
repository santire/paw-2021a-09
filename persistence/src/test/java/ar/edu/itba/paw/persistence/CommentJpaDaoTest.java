package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Comment;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Sql(scripts = "classpath:comment-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class CommentJpaDaoTest {
    @Autowired
    private DataSource ds;

    @Autowired
    private CommentJpaDao commentJpaDao;

    @Autowired
    private RestaurantJpaDao restaurantJpaDao;
    @Autowired
    private UserJpaDao userJpaDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }


    @Test
    public void testFindById(){
        final Optional<Comment> maybeComment = commentJpaDao.findById(11);

        assertTrue(maybeComment.isPresent());
        final Comment comment = maybeComment.get();

        assertEquals((long)1, (long)comment.getUser().getId());
        assertEquals((long)1, (long)comment.getRestaurant().getId());
        assertEquals("firstBK", comment.getUserComment());
    }

    @Test
    public void testFindByUserAndRestaurant(){
        final Optional<Comment> maybeComment = commentJpaDao.findByUserAndRestaurantId(1, 2);

        assertTrue(maybeComment.isPresent());
        Comment comment = maybeComment.get();

        assertEquals(1, (long)comment.getUser().getId());
        assertEquals(2, (long)comment.getRestaurant().getId());
        assertEquals("firstBQ", comment.getUserComment());
    }

    @Test
    public void testFindByRestaurant(){
        List<Comment> commentList = commentJpaDao.findByRestaurant(2, 1, 2);

        assertEquals(1, (long)commentList.size());

        assertEquals(2, commentList.get(0).getUser().getId().longValue());
        assertEquals(2, commentList.get(0).getRestaurant().getId().longValue());
        assertEquals("secondBQ", commentList.get(0).getUserComment());
    }

    @Test
    public void testDeleteComment(){

        final Optional<Comment> maybeComment = commentJpaDao.findById(15);
        assertTrue(maybeComment.isPresent());

        commentJpaDao.deleteComment(15);

        final Optional<Comment> notComment = commentJpaDao.findById(15);
        assertFalse(notComment.isPresent());
    }

    @Test
    public void testCreateComment(){
        LocalDate date = LocalDate.now();

        final Comment c = commentJpaDao.addComment(userJpaDao.findById(2).get(), restaurantJpaDao.findById(3).get(), "commentary", date);

        final Optional<Comment> maybeComment = commentJpaDao.findById(c.getId());

        assertTrue(maybeComment.isPresent());
        final Comment comment = maybeComment.get();
        assertEquals("commentary", comment.getUserComment());
    }
}
