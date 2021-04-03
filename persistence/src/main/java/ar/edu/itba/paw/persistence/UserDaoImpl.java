package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(
        rs.getLong("user_id"),
        rs.getString("username"),
        rs.getString("password"),
        rs.getString("first_name"),
        rs.getString("last_name"),
        rs.getString("email"),
        rs.getString("phone")
        );

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public UserDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("user_id");
    }

    @Override
    public Optional<User> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE user_id = ?", USER_ROW_MAPPER, id)
            .stream().findFirst();
    }

    @Override
    public User register(final String username,final String password,final String first_name,final String last_name,final String email,final String phone) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", username);
        params.addValue("password", password);
        params.addValue("first_name", first_name);
        params.addValue("last_name", last_name);
        params.addValue("email", email);
        params.addValue("phone", phone);
        final Number userId = jdbcInsert.executeAndReturnKey(params);
        return new User(userId.longValue(),username,password, first_name,last_name,email,phone);
    }

}
