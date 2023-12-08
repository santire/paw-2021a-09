package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.models.CommentRow;
import ar.edu.itba.paw.persistence.models.RestaurantRow;
import ar.edu.itba.paw.persistence.models.UserRow;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Sql(scripts = "classpath:comment-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class CommentJpaDaoTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    private DataSource ds;
    @Autowired
    private CommentJpaDao commentJpaDao;


    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }


    @Test
    public void testFindById() {
        final Optional<Comment> maybeComment = commentJpaDao.findById(11);

        assertTrue(maybeComment.isPresent());
        final Comment comment = maybeComment.get();

        assertEquals(1L, comment.getUser().getId().longValue());
        assertEquals(1L, comment.getRestaurant().getId().longValue());
        assertEquals("firstBK", comment.getUserComment());
    }

    @Test
    public void testFindByUserAndRestaurant() {
        final List<Comment> maybeComment = commentJpaDao.findFilteredComments(1, 5, 1L, 2L, true);

        assertNotNull(maybeComment.get(0));
        Comment comment = maybeComment.get(0);

        assertEquals(1L, comment.getUser().getId().longValue());
        assertEquals(2L, comment.getRestaurant().getId().longValue());
        assertEquals("firstBQ", comment.getUserComment());
    }

    @Test
    public void testFindByUserAndRestaurantCount() {
        final int commentsCount = commentJpaDao.findFilteredCommentsCount(1L, 2L);
        assertEquals(1, commentsCount);
    }

    @Test
    public void testFindByRestaurant() {
        final List<Comment> commentList = commentJpaDao.findFilteredComments(1, 5, null, 2L, true);

        assertEquals(2L, commentList.size());
        assertEquals(2L, commentList.get(1).getUser().getId().longValue());
        assertEquals(2L, commentList.get(1).getRestaurant().getId().longValue());
        assertEquals("secondBQ", commentList.get(1).getUserComment());
    }

    @Test
    public void testFindByRestaurantCount() {
        final int commentsCount = commentJpaDao.findFilteredCommentsCount(null, 2L);
        assertEquals(2, commentsCount);
    }

    @Test
    public void testDeleteComment() {

        long commentId = 15L;

        CommentRow comment = jdbcTemplate.queryForObject("SELECT * FROM comments WHERE comment_id = " + commentId, CommentRow.rowMapper);

        assertEquals(15L, comment.getId().longValue());
        assertEquals("2021-05-15", comment.getDate());
        assertEquals("mycomment", comment.getMessage());
        assertEquals(3L, comment.getRestaurantId().longValue());
        assertEquals(2L, comment.getUserId().longValue());

        commentJpaDao.deleteComment(comment.getId());
        em.flush();
        List<CommentRow> commentAfterDelete = jdbcTemplate.query("SELECT * FROM comments WHERE comment_id = " + commentId, CommentRow.rowMapper);


        assertTrue(commentAfterDelete.isEmpty());
    }

    @Test
    public void testCreateComment() {
        LocalDate date = LocalDate.now();

        long userId = 1L;
        long restaurantId = 1L;

        UserRow userRow = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = " + userId, UserRow.rowMapper);
        RestaurantRow restaurantRow = jdbcTemplate.queryForObject("SELECT * FROM restaurants WHERE restaurant_id= " + restaurantId, RestaurantRow.rowMapper);

        User user = userRow.toUser();
        Restaurant restaurant = restaurantRow.toRestaurant(user);


        String c = "good comment";
        final Comment comment = commentJpaDao.addComment(user, restaurant, c, date);
        em.flush();

        CommentRow commentRow = jdbcTemplate.queryForObject("SELECT * FROM comments WHERE comment_id = " + comment.getId(), CommentRow.rowMapper);

        assertEquals(comment.getId(), commentRow.getId());
        assertEquals(comment.getUserComment(), commentRow.getMessage());
        assertEquals(comment.getRestaurant().getId(), commentRow.getRestaurantId());
        assertEquals(comment.getUser().getId(), commentRow.getUserId());
    }


}
