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
    final User user = userDao.register(USERNAME);

    assertEquals(USERNAME, user.getName());

    // Should return 2 rows as the first user is inserted with the sql script in
    // the @Sql annotation and the other one is the one being registered in this
    // method
    assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));

  }

  @Test
  public void findById() {

    Optional<User> maybeUser = userDao.findById(1);

    assertTrue(maybeUser.isPresent());
  }
}