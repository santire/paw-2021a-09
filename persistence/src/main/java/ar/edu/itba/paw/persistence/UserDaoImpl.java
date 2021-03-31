package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(rs.getLong("user_id"),
            rs.getString("username"));

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public UserDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("user_id");
        // jdbcTemplate.execute(
                // "CREATE TABLE IF NOT EXISTS users( " + "user_id SERIAL PRIMARY KEY," + "username varchar(100)" + ")");
    }

    @Override
    public Optional<User> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE user_id = ?", USER_ROW_MAPPER, id)
            .stream().findFirst();
    }

    @Override
    public User register(final String username) {
        final Number userId = jdbcInsert.executeAndReturnKey(Collections.singletonMap("username", username));
        return new User(userId.longValue(), username);
    }

}
