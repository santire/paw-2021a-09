package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.sql.DataSource;

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

import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.persistence.config.TestConfig;

// @Rollback
// @Sql(scripts = "classpath:menu-test.sql")
// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(classes = TestConfig.class)
// @Ignore
// public class MenuDaoImplTest {

	// @Autowired
    // private DataSource ds;

    // @Autowired
    // private MenuDaoImpl menuDao;

    // private JdbcTemplate jdbcTemplate;

    // @Before
    // public void setUp() {
        // jdbcTemplate = new JdbcTemplate(ds);
    // }

    // @Test
    // public void testFindMenuByBadRestaurantId() {
        // List<MenuItem> menu = menuDao.findMenuByRestaurantId(666);
        // assertEquals(0, menu.size());
    // }

    // @Test
    // public void testFindByRestaurantId() {
        // List<MenuItem> menu = menuDao.findMenuByRestaurantId(1);
        // assertEquals(3, menu.size());
    // }

// }
