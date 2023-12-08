package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.models.LikeRow;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


@Transactional
@Sql(scripts = "classpath:likes-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class LikeJpaDaoTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    private DataSource ds;
    @Autowired
    private LikeJpaDao likeJpaDao;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);

        // Make sure like 15 is always inserted before each method
        jdbcTemplate.update("INSERT INTO likes (like_id, user_id, restaurant_id) VALUES (14, 21, 997)");
    }

    @After
    public void tearDown() {
        // Delete like from likeTest
        jdbcTemplate.update("DELETE FROM likes WHERE user_id=21 AND restaurant_id=998");
    }

    @Test
    public void testGetLikesByUserId() {
        final List<Like> likesList = likeJpaDao.getLikesByUserId(1, 5, 20L);

        assertEquals(3, likesList.size());
        assertEquals(997, likesList.get(2).getRestaurant().getId().longValue());
        assertEquals(998, likesList.get(1).getRestaurant().getId().longValue());
        assertEquals(999, likesList.get(0).getRestaurant().getId().longValue());
    }

    @Test
    public void testGetLikesByUserIdCount() {
        final int likesCount = likeJpaDao.getLikesByUserIdCount(20L);
        assertEquals(3, likesCount);
    }

    @Test
    public void testUserLikesRestaurant() {
        final long userId = 20L;
        final long restaurantId = 999L;
        assertTrue(likeJpaDao.userLikesRestaurant(userId, restaurantId));
    }

    @Test
    public void testUserDontLikesRestaurant() {
        final long userId = 21L;
        final long restaurantId = 999L;
        assertFalse(likeJpaDao.userLikesRestaurant(userId, restaurantId));
    }

    @Test
    public void testGetUserLikesByRestaurantIds() {
        final long userId = 20L;
        final List<Long> likedRestaurantIds = Arrays.asList(997L, 998L, 999L);
        final Long unlikedRestaurantId = 996L;
        final List<Long> restaurantIds = new ArrayList<>(likedRestaurantIds);
        restaurantIds.add(unlikedRestaurantId);

        final List<Like> userLikes = likeJpaDao.userLikesRestaurants(userId, restaurantIds);

        assertEquals(3, userLikes.size());
        final Set<Long> actualRestaurantIds = userLikes.stream().map(l -> l.getRestaurant().getId()).collect(Collectors.toSet());
        final Set<Long> expectedRestaurantIds = new HashSet<>(likedRestaurantIds);
        assertEquals(actualRestaurantIds, expectedRestaurantIds);

    }

    @Test
    public void testLike() {
        final long userId = 21L;
        final long unlikedRestaurantId = 998L;

        // Check like did not exist before
        List<LikeRow> likeQueryRows = jdbcTemplate.query("SELECT * FROM likes WHERE user_id=" + userId + " AND restaurant_id=" + unlikedRestaurantId, LikeRow.rowMapper);
        assertTrue(likeQueryRows.isEmpty());

        // Prepare for like
        UserRow userRow = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = " + userId, UserRow.rowMapper);
        RestaurantRow restaurantRow = jdbcTemplate.queryForObject("SELECT * FROM restaurants WHERE restaurant_id= " + unlikedRestaurantId, RestaurantRow.rowMapper);

        User user = userRow.toUser();
        Restaurant restaurant = restaurantRow.toRestaurant(user);

        Like like = likeJpaDao.like(user, restaurant);
        em.flush();
        assertNotNull(like);

        // Check like was inserted
        LikeRow likeRow = jdbcTemplate.queryForObject("SELECT * FROM likes WHERE like_id=" + like.getId(), LikeRow.rowMapper);
        assertEquals(userId, likeRow.getUserId().longValue());
        assertEquals(unlikedRestaurantId, likeRow.getRestaurantId().longValue());
    }

    @Test
    public void testDislike() {
        final long likeId = 14L;
        final long userId = 21L;
        final long likedRestaurantId = 997L;

        // Check like existed before
        LikeRow likeRow = jdbcTemplate.queryForObject("SELECT * FROM likes WHERE user_id=" + userId + " AND restaurant_id=" + likedRestaurantId, LikeRow.rowMapper);

        assertEquals(likeId, likeRow.getId().longValue());
        assertEquals(userId, likeRow.getUserId().longValue());
        assertEquals(likedRestaurantId, likeRow.getRestaurantId().longValue());

        // Dislike
        likeJpaDao.dislike(userId, likedRestaurantId);
        em.flush();

        // Check like no longer exists
        List<LikeRow> likeQueryRows = jdbcTemplate.query("SELECT * FROM likes WHERE user_id=" + userId + " AND restaurant_id=" + likedRestaurantId, LikeRow.rowMapper);
        assertTrue(likeQueryRows.isEmpty());
    }


}
