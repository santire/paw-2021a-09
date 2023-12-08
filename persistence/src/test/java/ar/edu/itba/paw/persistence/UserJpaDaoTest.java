package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.models.TokenRow;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Sql(scripts = "classpath:user-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJpaDaoTest {

    private static final long OWNER_ID = 999L;
    private static final String OWNER_EMAIL = "mluque@itba.edu.ar";
    private static final String USERNAME = "mluque";
    private static final String FIRST_NAME = "manuel";
    private static final String LAST_NAME = "luque";
    private static final String PHONE = "1135679821";

    private static final String TOKEN = "TOK";
    private static final String TOKEN_DATE = "2021-06-02 16:55:38";
    private static final long TOKEN_USER = 997L;
    private static final DateTimeFormatter TOKEN_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @PersistenceContext
    EntityManager em;
    @Autowired
    private DataSource ds;
    @Autowired
    private UserJpaDao userDao;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);

        Object[] token = new Object[]{999L, TOKEN, TOKEN_DATE, TOKEN_USER};

        String verificationSql = "INSERT INTO verification_tokens(token_id, token, created_at, user_id) VALUES (?, ?, ?, ?)";
        String passwordSql = "INSERT INTO password_tokens(token_id, token, created_at, user_id) VALUES (?, ?, ?, ?)";

        // Insert test verification token for delete
        jdbcTemplate.update(verificationSql, token);
        // Insert test password token for delete
        jdbcTemplate.update(passwordSql, token);
    }

    @After
    public void tearDown() {
        Object[] tokenId = new Object[]{999L};

        String verificationSql = "DELETE FROM verification_tokens WHERE token_id=?";
        String passwordSql = "DELETE FROM password_tokens WHERE token_id=?";

        // Insert test verification token for delete
        jdbcTemplate.update(verificationSql, tokenId);
        // Insert test password token for delete
        jdbcTemplate.update(passwordSql, tokenId);
    }

    @Test
    public void testFindById() {
        final Optional<User> maybeUser = userDao.findById(OWNER_ID);

        assertTrue(maybeUser.isPresent());
        final User user = maybeUser.get();

        assertEquals(OWNER_ID, user.getId().longValue());
        assertEquals(USERNAME, user.getUsername());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(PHONE, user.getPhone());
        assertFalse(user.isActive());
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

        assertEquals(OWNER_ID, user.getId().longValue());
        assertEquals(USERNAME, user.getUsername());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(PHONE, user.getPhone());
        assertFalse(user.isActive());
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
        final String firstName = "first";
        final String lastName = "last";
        final String email = "myemail@email.com";
        final String username = "testuser";
        final String password = "secret";
        final String phone = "123456789";

        User user = userDao.register(username, password, firstName, lastName, email, phone);
        em.flush();

        final String sql = String.format("SELECT * FROM users WHERE user_id=%d", user.getId());
        UserRow userRow = jdbcTemplate.queryForObject(sql, UserRow.rowMapper);

        assertEquals(username, userRow.getUsername());
        assertEquals(password, userRow.getPassword());
        assertEquals(firstName, userRow.getFirstName());
        assertEquals(lastName, userRow.getLastName());
        assertEquals(email, userRow.getEmail());
        assertEquals(phone, userRow.getPhone());
        assertFalse(userRow.isActive());
    }

    @Test
    public void testAssignToken() {
        final LocalDateTime time = LocalDateTime.parse("2021-06-02T16:55:38");
        final String token = "TOKEN00";
        final long userId = 999L;


        userDao.assignTokenToUser(token, time, userId);
        em.flush();

        final String sql = String.format("SELECT * FROM verification_tokens WHERE token='%s'", token);
        TokenRow tokenRow = jdbcTemplate.queryForObject(sql, TokenRow.rowMapper);

        assertEquals(userId, tokenRow.getUserId().longValue());
        assertEquals(time.format(TOKEN_DATE_FORMATTER), tokenRow.getCreatedAt().substring(0, 19));
        assertEquals(token, tokenRow.getToken());
    }

    @Test
    public void testAssignPasswordToken() {
        final LocalDateTime time = LocalDateTime.parse("2021-06-02T16:55:38");
        final String token = "TOKEN00";
        final long userId = 999L;

        userDao.assignPasswordTokenToUser(token, time, userId);
        em.flush();

        final String sql = String.format("SELECT * FROM password_tokens WHERE token='%s'", token);
        TokenRow tokenRow = jdbcTemplate.queryForObject(sql, TokenRow.rowMapper);

        assertEquals(userId, tokenRow.getUserId().longValue());
        assertEquals(time.format(TOKEN_DATE_FORMATTER), tokenRow.getCreatedAt().substring(0, 19));
        assertEquals(token, tokenRow.getToken());
    }

    @Test
    public void testDeleteToken() {
        // Check token exists
        final long tokenId = 999L;
        final String sql = String.format("SELECT * FROM verification_tokens WHERE token_id=%d", tokenId);
        TokenRow tokenRow = jdbcTemplate.queryForObject(sql, TokenRow.rowMapper);

        assertEquals(tokenId, tokenRow.getId().longValue());
        assertEquals(TOKEN, tokenRow.getToken());
        assertEquals(TOKEN_USER, tokenRow.getUserId().longValue());
        assertEquals(TOKEN_DATE, tokenRow.getCreatedAt().substring(0, 19));

        // Delete token
        userDao.deleteToken(TOKEN);
        em.flush();

        // Check token was deleted
       List<TokenRow> tokenRowList = jdbcTemplate.query(sql, TokenRow.rowMapper);
       assertTrue(tokenRowList.isEmpty());
    }

    @Test
    public void testDeletePasswordTokens() {
        // Check token exists
        final long tokenId = 999L;
        final String sql = String.format("SELECT * FROM password_tokens WHERE token_id=%d", tokenId);
        TokenRow tokenRow = jdbcTemplate.queryForObject(sql, TokenRow.rowMapper);

        assertEquals(tokenId, tokenRow.getId().longValue());
        assertEquals(TOKEN, tokenRow.getToken());
        assertEquals(TOKEN_USER, tokenRow.getUserId().longValue());
        assertEquals(TOKEN_DATE, tokenRow.getCreatedAt().substring(0, 19));

        // Obtain token user
        final String userSql = String.format("SELECT * FROM users WHERE user_id=%d", TOKEN_USER);
        UserRow userRow = jdbcTemplate.queryForObject(userSql, UserRow.rowMapper);

        User user = userRow.toUser();

        // Delete tokens
        userDao.deleteAssociatedPasswordTokens(user);
        em.flush();

        // Check token was deleted
        List<TokenRow> tokenRowList = jdbcTemplate.query(sql, TokenRow.rowMapper);
        assertTrue(tokenRowList.isEmpty());

    }
}
