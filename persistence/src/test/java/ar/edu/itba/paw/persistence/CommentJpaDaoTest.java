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

        SimpleComment comment = jdbcTemplate.queryForObject("SELECT * FROM comments WHERE comment_id = " + commentId , (rs, rowNum) ->
                new SimpleComment(
                        rs.getLong("comment_id"),
                        rs.getString("date"),
                        rs.getString("user_comment"),
                        rs.getLong("restaurant_id"),
                        rs.getLong("user_id")
                ));

        commentJpaDao.deleteComment(comment.comment_id);
        em.flush();

        List <SimpleComment> comments = jdbcTemplate.query("SELECT * FROM comments WHERE comment_id = " + commentId , (rs, rowNum) ->
                new SimpleComment(
                        rs.getLong("comment_id"),
                        rs.getString("date"),
                        rs.getString("user_comment"),
                        rs.getLong("restaurant_id"),
                        rs.getLong("user_id")
                ));

        assertTrue(comments.isEmpty());
    }

    @Test
    public void testCreateComment() {
        LocalDate date = LocalDate.now();

        long userId = 1;
        long restaurantId = 1;

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

        String c = "good comment";
        final Comment comment = commentJpaDao.addComment(user, restaurant, c, date);
        em.flush();

       SimpleComment retrievedComment = jdbcTemplate.queryForObject("SELECT * FROM comments WHERE comment_id = " + comment.getId() , (rs, rowNum) ->
                new SimpleComment(
                        rs.getLong("comment_id"),
                        rs.getString("date"),
                        rs.getString("user_comment"),
                        rs.getLong("restaurant_id"),
                        rs.getLong("user_id")
                ));

        assertEquals(comment.getId(), retrievedComment.comment_id);
        assertEquals(comment.getUserComment(), retrievedComment.user_comment);
        assertEquals(comment.getRestaurant().getId(), retrievedComment.restaurant_id);
        assertEquals(comment.getUser().getId(), retrievedComment.user_id);
    }

    private static class SimpleComment {
        Long comment_id;
        String date;
        String user_comment;
        Long restaurant_id;
        Long user_id;

        public SimpleComment(Long comment_id, String date, String user_comment, Long restaurant_id, Long user_id) {
            this.comment_id = comment_id;
            this.date = date;
            this.user_comment = user_comment;
            this.restaurant_id = restaurant_id;
            this.user_id = user_id;
        }
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
