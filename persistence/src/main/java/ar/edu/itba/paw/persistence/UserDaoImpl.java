package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {
    //@Autowired
    //private DataSource ds;

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public UserDaoImpl(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("userid");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users( "
                + "userid SERIAL PRIMARY KEY,"
                + "username varchar(100)"
                + ")");
    }

    private final static RowMapper<User> ROW_MAPPER = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getLong("userid") ,rs.getString("username"));
        }
    };

    @Override
    public User findById(long id) {
        final List<User> results = jdbcTemplate.query("SELECT * FROM users WHERE userid = ?", ROW_MAPPER, id);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    @Override
    public User register(final String username) {
        final Map<String, Object> args = new HashMap<>();
        args.put("username", username);
        final Number userId = jdbcInsert.executeAndReturnKey(args);
        return new User(userId.longValue(), username);
    }


}
