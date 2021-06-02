package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Ignore;
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

import static org.junit.Assert.assertEquals;

// @Rollback
// @Sql(scripts = "classpath:likes-test.sql")
// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(classes = TestConfig.class)
// @Ignore
// public class LikesDaoImplTest {
    // private static final int LIKES_INSERTED_SIZE = 1;

    // private static final long USER_ID = 1;
    // private static final long RESTAURANT_ID = 1;
    // private static final long ANOTHER_RESTAURANT_ID = 2;
    // private static final long NON_EXISTENT_RESTAURANT_ID = 3;

    // @Autowired
    // private DataSource ds;

    // @Autowired
    // private LikesDaoImpl likesDao;

    // private JdbcTemplate jdbcTemplate;

    // @Before
    // public void setUp() {
        // jdbcTemplate = new JdbcTemplate(ds);
    // }

    // @Test
    // public void like(){
        // boolean success = likesDao.like(USER_ID, ANOTHER_RESTAURANT_ID);

        // assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "likes", "restaurant_id = " + ANOTHER_RESTAURANT_ID));
    // }

    // @Test
    // public void dislike(){
        // boolean success = likesDao.dislike(USER_ID, RESTAURANT_ID);

        // assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "likes", "restaurant_id = " + ANOTHER_RESTAURANT_ID));
    // }

    // @Test
    // public void getLikedRestaurantsId(){
        // List<Long> ids = likesDao.getLikedRestaurantsId(USER_ID);

        // assertEquals(1, ids.size());
    // }

    // @Test
    // public void getNonexistenceLikedRestaurantsId(){
        // List<Long> ids = likesDao.getLikedRestaurantsId(USER_ID + 1);

        // assertEquals(0, ids.size());
    // }
// }
