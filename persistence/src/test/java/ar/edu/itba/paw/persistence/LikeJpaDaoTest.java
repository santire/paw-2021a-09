package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.util.List;


@Transactional
@Sql(scripts = "classpath:likes-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class LikeJpaDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private LikeJpaDao likeJpaDao;

    @PersistenceContext
    EntityManager em;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("likes")
                .usingGeneratedKeyColumns("like_id");
    }

    @Test
    public void testGetLikesByUserId(){
        List<Like> likesList = likeJpaDao.getLikesByUserId(20l);

        Assert.assertEquals(3, likesList.size());
        Assert.assertEquals(997, likesList.get(0).getRestaurant().getId().longValue());
        Assert.assertEquals(998, likesList.get(1).getRestaurant().getId().longValue());
        Assert.assertEquals(999, likesList.get(2).getRestaurant().getId().longValue());
    }

    @Test
    public void testUserLikesRestaurant(){
        Assert.assertTrue(likeJpaDao.userLikesRestaurant(20,999));
        Assert.assertTrue(likeJpaDao.userLikesRestaurant(21,998));
        Assert.assertFalse(likeJpaDao.userLikesRestaurant(21,999));
    }

    @Test
    public void testDislike(){
        long userId = 20;
        long restaurantId = 997;
        User user = em.find(User.class, userId);
        Restaurant restaurant = em.find(Restaurant.class, restaurantId);

        TypedQuery<Like> query = em.createQuery("SELECT c FROM Like c WHERE c.user = :user AND c.restaurant = :restaurant", Like.class);
        query.setParameter("user", user); 
        query.setParameter("restaurant", restaurant);
        Like retrievedLike = query.getSingleResult();; // Initialize to null initially
        
        Assert.assertNotNull(retrievedLike);

        boolean result = likeJpaDao.dislike(userId, restaurantId);
        Assert.assertTrue(result);

        TypedQuery<Like> deletedQuery = em.createQuery("SELECT c FROM Like c WHERE c.user = :user AND c.restaurant = :restaurant", Like.class);
        deletedQuery.setParameter("user", user); 
        deletedQuery.setParameter("restaurant", restaurant);
        Like deletedLike = null; // Initialize to null initially

        try {
            retrievedLike = query.getSingleResult();
        } catch (NoResultException e) {
        }
        Assert.assertNull(deletedLike);
    }

    @Test
    public void testLike(){
        long userId = 21;
        long restaurantId = 997;
        User user = em.find(User.class, userId);
        Restaurant restaurant = em.find(Restaurant.class, restaurantId);

        TypedQuery<Like> query = em.createQuery("SELECT c FROM Like c WHERE c.user = :user AND c.restaurant = :restaurant", Like.class);
        query.setParameter("user", user); 
        query.setParameter("restaurant", restaurant);
        Like retrievedLike = null; // Initialize to null initially

        try {
            retrievedLike = query.getSingleResult();
        } catch (NoResultException e) {
        }
        Assert.assertNull(retrievedLike);

        boolean result = likeJpaDao.like(user, restaurant);
        Assert.assertTrue(result);

        TypedQuery<Like> likeQuery = em.createQuery("SELECT c FROM Like c WHERE c.user = :user AND c.restaurant = :restaurant", Like.class);
        likeQuery.setParameter("user", user); 
        likeQuery.setParameter("restaurant", restaurant);
        Like createdLike = likeQuery.getSingleResult();

        Assert.assertNotNull(createdLike);
    }

}
