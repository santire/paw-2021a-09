package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.config.TestConfig;

@Rollback
@Sql(scripts = "classpath:user-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserDaoImplTest {

  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private static final String FIRST_NAME = "first_name";
  private static final String LAST_NAME = "last_name";
  private static final String EMAIL = "email";
  private static final String PHONE = "phone";


  @Autowired
  private DataSource ds;

  @Autowired
  private UserDaoImpl userDao;

  private JdbcTemplate jdbcTemplate;

  @Before
  public void setUp() {
    jdbcTemplate = new JdbcTemplate(ds);
  }

  @Test
  public void testRegister() {
    final User user = userDao.register(USERNAME,PASSWORD,FIRST_NAME,LAST_NAME,EMAIL,PHONE);

    assertEquals(USERNAME, user.getName());
    assertEquals(USERNAME, user.getUsername());
    assertEquals(PASSWORD, user.getPassword());
    assertEquals(FIRST_NAME, user.getFirst_name());
    assertEquals(LAST_NAME, user.getLast_name());
    assertEquals(EMAIL, user.getEmail());
    assertEquals(PHONE, user.getPhone());

    // Should return 2 rows as the first user is inserted with the sql script in
    // the @Sql annotation and the other one is the one being registered in this
    // method
    assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));

  }

  @Test
  public void testFindById() {

    Optional<User> maybeUser = userDao.findById(1);

    assertTrue(maybeUser.isPresent());
  }

  @Test
  public void testFindByEmail() {

    Optional<User> maybeUser = userDao.findByEmail("paw");

    assertTrue(maybeUser.isPresent());
  }
}
