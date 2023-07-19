package ar.edu.itba.paw.persistence;

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

    @Autowired
    private UserJpaDao userJpaDao;
    @Autowired
    private RestaurantJpaDao restaurantJpaDao;

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
        List<Long> likesList = likeJpaDao.getLikesByUserId(20l);

        Assert.assertEquals(3, likesList.size());
        Assert.assertEquals(997, likesList.get(0).longValue());
        Assert.assertEquals(998, likesList.get(1).longValue());
        Assert.assertEquals(999, likesList.get(2).longValue());
    }

    @Test
    public void testUserLikesRestaurant(){
        Assert.assertTrue(likeJpaDao.userLikesRestaurant(20,999));
        Assert.assertTrue(likeJpaDao.userLikesRestaurant(21,998));
        Assert.assertFalse(likeJpaDao.userLikesRestaurant(21,999));
    }

    @Test
    public void testDislike(){
        boolean result = likeJpaDao.dislike(21, 998);
        Assert.assertTrue(result);
        Assert.assertFalse(likeJpaDao.userLikesRestaurant(21,998));

    }

    @Test
    public void testLike(){
        boolean result = likeJpaDao.like(userJpaDao.findById(21).get(), restaurantJpaDao.findById(996).get());
        Assert.assertTrue(result);
        Assert.assertTrue(likeJpaDao.userLikesRestaurant(21,996));
    }

}
