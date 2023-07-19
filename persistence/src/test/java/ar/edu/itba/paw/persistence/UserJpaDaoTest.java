package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.PasswordToken;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.persistence.config.TestConfig;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
        String EMAIL = "myemail@email.com";
        userDao.register("username", "password", "firstname", "lastname", EMAIL, "123456789");

        final Optional<User> maybeUser = userDao.findByEmail(EMAIL);
        assertTrue(maybeUser.isPresent());

        final User user = maybeUser.get();
        assertEquals("username", user.getName());
        assertEquals("password", user.getPassword());
        assertEquals("firstname", user.getFirstName());
        assertEquals("lastname", user.getLastName());
        assertEquals(EMAIL, user.getEmail());
        assertEquals("123456789", user.getPhone());
        assertFalse(user.isActive());
    }

    @Test
    public void testAssignToken() {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
        String TOKEN = "TOKEN00";
        String PTOKEN = "PTOKEN00";

        final Optional<User> maybeUser = userDao.findByEmail(OWNER_EMAIL);
        assertTrue(maybeUser.isPresent());
        final User user = maybeUser.get();

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

        final Optional<User> maybeUser = userDao.findByEmail(OWNER_EMAIL);
        assertTrue(maybeUser.isPresent());
        final User user = maybeUser.get();

        userDao.deleteToken(TOKEN);
        userDao.deleteAssociatedPasswordTokens(user);

        Optional<PasswordToken> notPToken = userDao.getPasswordToken(PTOKEN);
        TestCase.assertFalse(notPToken.isPresent());

    }
    // Fails because of HSQL limitation
    /*
    @Test
    public void testPurgeTokens() {
        LocalDateTime pastTime = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault()).minusDays(2);
        LocalDateTime futureTime = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault()).plusDays(2);

        LocalDateTime time = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
        String TOKEN1 = "TOKEN01";
        String TOKEN2 = "TOKEN02";
        String PTOKEN1 = "PTOKEN01";
        String PTOKEN2 = "PTOKEN02";


        userDao.assignTokenToUser(TOKEN1, pastTime, 999l);
        userDao.assignTokenToUser(TOKEN2, futureTime, 999l);

        userDao.assignPasswordTokenToUser(PTOKEN1, pastTime, 999l);
        userDao.assignPasswordTokenToUser(PTOKEN2, futureTime, 999l);

        Optional<VerificationToken> maybeToken1 = userDao.getToken(TOKEN1);
        TestCase.assertTrue(maybeToken1.isPresent());
        Optional<VerificationToken> maybeToken2 = userDao.getToken(TOKEN2);
        TestCase.assertTrue(maybeToken2.isPresent());

        Optional<PasswordToken> maybePToken1 = userDao.getPasswordToken(PTOKEN1);
        TestCase.assertTrue(maybePToken1.isPresent());
        Optional<PasswordToken> maybePToken2 = userDao.getPasswordToken(PTOKEN2);
        TestCase.assertTrue(maybePToken2.isPresent());


        userDao.purgeAllExpiredTokensSince(time);

        Optional<VerificationToken> pMaybeToken1 = userDao.getToken(TOKEN1);
        TestCase.assertFalse(pMaybeToken1.isPresent());
        Optional<VerificationToken> pMaybeToken2 = userDao.getToken(TOKEN2);
        TestCase.assertTrue(pMaybeToken2.isPresent());

        Optional<PasswordToken> pMaybePToken1 = userDao.getPasswordToken(PTOKEN1);
        TestCase.assertFalse(pMaybePToken1.isPresent());
        Optional<PasswordToken> pMaybePToken2 = userDao.getPasswordToken(PTOKEN2);
        TestCase.assertTrue(pMaybePToken2.isPresent());
    }
*/

}
