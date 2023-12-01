package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.Restaurant;
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
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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

        assertEquals(1, comment.getUser().getId().longValue());
        assertEquals(1, comment.getRestaurant().getId().longValue());
        assertEquals("firstBK", comment.getUserComment());
    }

    @Test
    public void testFindByUserAndRestaurant() {
        final List<Comment> maybeComment = commentJpaDao.findFilteredComments(1, 5, 1L, 2L, true);

        assertNotNull(maybeComment.get(0));
        Comment comment = maybeComment.get(0);

        assertEquals(1, comment.getUser().getId().longValue());
        assertEquals(2, comment.getRestaurant().getId().longValue());
        assertEquals("firstBQ", comment.getUserComment());
    }

    @Test
    public void testFindByRestaurant() {
        List<Comment> commentList = commentJpaDao.findFilteredComments(1, 5, null, 2L, true);

        assertEquals(2, commentList.size());
        assertEquals(2, commentList.get(1).getUser().getId().longValue());
        assertEquals(2, commentList.get(1).getRestaurant().getId().longValue());
        assertEquals("secondBQ", commentList.get(1).getUserComment());
    }

    @Test
    public void testDeleteComment() {

        long commentId = 15;
        Comment comment = em.find(Comment.class, commentId);
        assertNotNull(comment);

        commentJpaDao.deleteComment(15);

        Comment deletedComment = em.find(Comment.class, commentId);
        assertNull(deletedComment);
    }

    @Test
    public void testCreateComment() {
        LocalDate date = LocalDate.now();

        long userId = 1;
        long restaurantId = 1;

        User user = em.find(User.class, userId);
        Restaurant restaurant = em.find(Restaurant.class, restaurantId);

        String comment = "good comment";
        final Comment c = commentJpaDao.addComment(user, restaurant, comment, date);

        TypedQuery<Comment> query = em.createQuery("SELECT c FROM Comment c WHERE c.userComment = :comment", Comment.class);
        query.setParameter("comment", comment); // Replace "good comment" with the value you're searching for

        Comment retrievedComment = query.getSingleResult();
        //int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM comments WHERE user_comment = ?", Integer.class, comment);

        assertNotNull(retrievedComment);
        assertEquals(retrievedComment.getUserComment(), comment);
    }
}
