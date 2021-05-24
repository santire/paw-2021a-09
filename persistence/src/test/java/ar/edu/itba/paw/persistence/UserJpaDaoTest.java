package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.config.TestConfig;

@Transactional
@Sql(scripts = "classpath:restaurant-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJpaDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private UserJpaDao userDao;

    private JdbcTemplate jdbcTemplate;

    private static Long OWNER_ID = 999L;
    private static String OWNER_EMAIL = "mluque@itba.edu.ar";

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testFindById() {
        final Optional<User> maybeUser = userDao.findById(OWNER_ID);

        assertTrue(maybeUser.isPresent());
        final User user = maybeUser.get();

        assertEquals(OWNER_EMAIL, user.getEmail());
        assertEquals("mluque", user.getName());
        assertEquals("123456", user.getPassword());
        assertEquals("manuel", user.getFirstName());
        assertEquals("luque", user.getLastName());
        assertEquals("1135679821", user.getPhone());
        assertFalse(user.isActive());

        List<Restaurant> ownedRestaurants = user.getOwnedRestaurants();

        assertEquals(3, ownedRestaurants.size());
    }

    @Test
    public void testFindByEmail() {
        final Optional<User> maybeUser = userDao.findByEmail(OWNER_EMAIL);

        assertTrue(maybeUser.isPresent());
        final User user = maybeUser.get();

        assertEquals(OWNER_ID.longValue(), user.getId().longValue());
        assertEquals("mluque", user.getName());
        assertEquals("123456", user.getPassword());
        assertEquals("manuel", user.getFirstName());
        assertEquals("luque", user.getLastName());
        assertEquals("1135679821", user.getPhone());
        assertFalse(user.isActive());

        List<Restaurant> ownedRestaurants = user.getOwnedRestaurants();

        assertEquals(3, ownedRestaurants.size());
    }

}
