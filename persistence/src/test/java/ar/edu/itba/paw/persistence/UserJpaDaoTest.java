package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.PasswordToken;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.persistence.config.TestConfig;
import junit.framework.TestCase;

import org.junit.Assert;
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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Sql(scripts = "classpath:restaurant-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJpaDaoTest {

    private static final Long OWNER_ID = 999L;
    private static final String OWNER_EMAIL = "mluque@itba.edu.ar";
    @Autowired
    private DataSource ds;
    @Autowired
    private UserJpaDao userDao;
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    EntityManager em;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);

    }

    @Test
    public void testFindById() {
        final Optional<User> maybeUser = userDao.findById(OWNER_ID);

        final Optional<User> notUser = userDao.findById(50L);
        assertFalse(notUser.isPresent());

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

        assertEquals(4, ownedRestaurants.size());
    }

    @Test
    public void testFindByEmail() {
        final Optional<User> maybeUser = userDao.findByEmail(OWNER_EMAIL);

        final Optional<User> notUser = userDao.findByEmail("notanuser@email.com");
        assertFalse(notUser.isPresent());


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

        assertEquals(4, ownedRestaurants.size());
    }

    @Test(expected = Exception.class)
    public void testCreateEmailInUse() {
        userDao.register("username", "password", "firstname", "lastname", OWNER_EMAIL, "123456789");
    }

    @Test
    public void testCreateUser() {
        String username = "TEST_NAME";
        String EMAIL = "myemail@email.com";

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :name", User.class);
        query.setParameter("name", username);
        User nullUser = null; 
        try {
            nullUser = query.getSingleResult();
        } catch (NoResultException e) {
        }
        Assert.assertNull(nullUser);

        userDao.register(username, "password", "firstname", "lastname", EMAIL, "123456789");

        TypedQuery<User> userQuery = em.createQuery("SELECT u FROM User u WHERE u.username = :name", User.class);
        userQuery.setParameter("name", username);
        User retrievedUser = null; 
        try {
            retrievedUser = userQuery.getSingleResult();
        } catch (NoResultException e) {
        }
        Assert.assertNotNull(retrievedUser);

        assertEquals(username, retrievedUser.getName());
        assertEquals("password", retrievedUser.getPassword());
        assertEquals("firstname", retrievedUser.getFirstName());
        assertEquals("lastname", retrievedUser.getLastName());
        assertEquals(EMAIL, retrievedUser.getEmail());
        assertEquals("123456789", retrievedUser.getPhone());
        assertFalse(retrievedUser.isActive());
    }

    @Test
    public void testAssignToken() {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
        String TOKEN = "TOKEN00";
        String PTOKEN = "PTOKEN00";

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", OWNER_EMAIL);
        User user = null; 
        try {
            user = query.getSingleResult();
        } catch (NoResultException e) {
        }
        Assert.assertNotNull(user);

        userDao.assignTokenToUser(TOKEN, time, user.getId());
        userDao.assignPasswordTokenToUser(PTOKEN, time, user.getId());

        Optional<PasswordToken> maybePToken = userDao.getPasswordToken(PTOKEN);
        TestCase.assertTrue(maybePToken.isPresent());

        Optional<VerificationToken> maybeToken = userDao.getToken(TOKEN);
        TestCase.assertTrue(maybeToken.isPresent());
        VerificationToken verificationToken = maybeToken.get();
        final User u = verificationToken.getUser();

        assertEquals(user, u);
    }

    @Test
    public void testDeleteToken() {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
        String TOKEN = "0TOKEN0";
        String PTOKEN = "0PTOKEN0";

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", OWNER_EMAIL);
        User user = null; 
        try {
            user = query.getSingleResult();
        } catch (NoResultException e) {
        }
        Assert.assertNotNull(user);

        userDao.deleteToken(TOKEN);
        userDao.deleteAssociatedPasswordTokens(user);

        Optional<PasswordToken> notPToken = userDao.getPasswordToken(PTOKEN);
        TestCase.assertFalse(notPToken.isPresent());

    }
}
