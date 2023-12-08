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
import javax.persistence.PersistenceContext;
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

        assertTrue(maybeUser.isPresent());
        final User user = maybeUser.get();

        SimpleUser retrievedUser = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = " + OWNER_ID , (rs, rowNum) ->
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

        assertEquals(retrievedUser.user_id.longValue(), user.getId().longValue());
        assertEquals(retrievedUser.username, user.getUsername());
        assertEquals(retrievedUser.password, user.getPassword());
        assertEquals(retrievedUser.first_name, user.getFirstName());
        assertEquals(retrievedUser.last_name, user.getLastName());
        assertEquals(retrievedUser.phone, user.getPhone());
        assertEquals(retrievedUser.is_active, user.isActive());
    }

    @Test
    public void testFindByIdNotUser() {
        final Optional<User> notUser = userDao.findById(50L);
        assertFalse(notUser.isPresent());
    }

    @Test
    public void testFindByEmail() {

        final Optional<User> maybeUser = userDao.findByEmail(OWNER_EMAIL);

        assertTrue(maybeUser.isPresent());
        final User user = maybeUser.get();

        SimpleUser retrievedUser = jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = '" + OWNER_EMAIL + "'" , (rs, rowNum) ->
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

        assertEquals(retrievedUser.user_id.longValue(), user.getId().longValue());
        assertEquals(retrievedUser.username, user.getUsername());
        assertEquals(retrievedUser.password, user.getPassword());
        assertEquals(retrievedUser.first_name, user.getFirstName());
        assertEquals(retrievedUser.last_name, user.getLastName());
        assertEquals(retrievedUser.phone, user.getPhone());
        assertEquals(retrievedUser.is_active, user.isActive());
    }

    @Test
    public void testFindByEmailNotUser() {
        final Optional<User> notUser = userDao.findByEmail("notanuser@email.com");
        assertFalse(notUser.isPresent());
    }

    @Test(expected = Exception.class)
    public void testCreateEmailInUse() {
        userDao.register("username", "password", "firstname", "lastname", OWNER_EMAIL, "123456789");
    }

    @Test
    public void testCreateUser() {
        String username = "TEST_NAME";
        String EMAIL = "myemail@email.com";

        User user = userDao.register(username, "password", "firstname", "lastname", EMAIL, "123456789");
        em.flush();

        SimpleUser retrievedUser = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id =" + user.getId().toString(), (rs, rowNum) ->
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


        assertEquals(username, retrievedUser.username);
        assertEquals("password", retrievedUser.password);
        assertEquals("firstname", retrievedUser.first_name);
        assertEquals("lastname", retrievedUser.last_name);
        assertEquals(EMAIL, retrievedUser.email);
        assertEquals("123456789", retrievedUser.phone);
        assertFalse(retrievedUser.is_active);
    }

    @Test
    public void testAssignToken() {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
        String TOKEN = "TOKEN00";

        SimpleUser user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = '" + OWNER_EMAIL + "'" , (rs, rowNum) ->
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

        userDao.assignTokenToUser(TOKEN, time, user.user_id);
        em.flush();

        SimpleToken retrievedToken = jdbcTemplate.queryForObject("SELECT * FROM verification_tokens WHERE token = '" + TOKEN + "'" , (rs, rowNum) ->
                new SimpleToken(
                        rs.getLong("token_id"),
                        rs.getString("token"),
                        rs.getString("created_at"),
                        rs.getLong("user_id")
                ));

        assertEquals(user.user_id, retrievedToken.user_id);
        assertEquals(TOKEN, retrievedToken.token);
    }

    @Test
    public void testAssignPasswordToken() {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
        String PTOKEN = "PTOKEN00";

        SimpleUser user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = '" + OWNER_EMAIL + "'" , (rs, rowNum) ->
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

        userDao.assignPasswordTokenToUser(PTOKEN, time, user.user_id);
        em.flush();

        SimpleToken retrievedToken = jdbcTemplate.queryForObject("SELECT * FROM password_tokens WHERE token = '" + PTOKEN + "'" , (rs, rowNum) ->
                new SimpleToken(
                        rs.getLong("token_id"),
                        rs.getString("token"),
                        rs.getString("created_at"),
                        rs.getLong("user_id")
                ));

        assertEquals(user.user_id, retrievedToken.user_id);
        assertEquals(PTOKEN, retrievedToken.token);
    }

    @Test
    public void testDeleteToken() {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
        String TOKEN = "0TOKEN0";

        SimpleUser user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = '" + OWNER_EMAIL + "'" , (rs, rowNum) ->
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

        List <SimpleToken> retrievedTokens = jdbcTemplate.query("SELECT * FROM verification_tokens WHERE token = '" + TOKEN + "'" , (rs, rowNum) ->
                new SimpleToken(
                        rs.getLong("token_id"),
                        rs.getString("token"),
                        rs.getString("created_at"),
                        rs.getLong("user_id")
                ));

        assertFalse(retrievedTokens.isEmpty());

        userDao.deleteToken(TOKEN);
        em.flush();

        retrievedTokens = jdbcTemplate.query("SELECT * FROM verification_tokens WHERE token = '" + TOKEN + "'" , (rs, rowNum) ->
                new SimpleToken(
                        rs.getLong("token_id"),
                        rs.getString("token"),
                        rs.getString("created_at"),
                        rs.getLong("user_id")
                ));

        assertTrue(retrievedTokens.isEmpty());

    }

    @Test
    public void testDeletePasswordToken() {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
        String PTOKEN = "0PTOKEN0";

        SimpleUser simpleUser = jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = '" + OWNER_EMAIL + "'" , (rs, rowNum) ->
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

        List <SimpleToken> retrievedTokens = jdbcTemplate.query("SELECT * FROM password_tokens WHERE token = '" + PTOKEN + "'" , (rs, rowNum) ->
                new SimpleToken(
                        rs.getLong("token_id"),
                        rs.getString("token"),
                        rs.getString("created_at"),
                        rs.getLong("user_id")
                ));

        assertFalse(retrievedTokens.isEmpty());

        userDao.deleteAssociatedPasswordTokens(user);

        retrievedTokens = jdbcTemplate.query("SELECT * FROM password_tokens WHERE token = '" + PTOKEN + "'" , (rs, rowNum) ->
                new SimpleToken(
                        rs.getLong("token_id"),
                        rs.getString("token"),
                        rs.getString("created_at"),
                        rs.getLong("user_id")
                ));

        assertTrue(retrievedTokens.isEmpty());

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

    private static class SimpleToken {
        Long token_id;
        String token;
        String created_at;
        Long user_id;

        public SimpleToken(Long token_id,  String token, String created_at, Long user_id) {
            this.token_id = token_id;
            this.token = token;
            this.created_at = created_at;
            this.user_id = user_id;
        }
    }
}
