package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    RestaurantDao restaurantDao;

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
    public Optional<User> findByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?", USER_ROW_MAPPER, email)
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

    @Override
    public void updatePassword(long id, String password) {
        jdbcTemplate.update("UPDATE users SET password = ? WHERE user_id = ?", password, id);
    }
    @Override
    public void updateUsername(long id, String username) {
        jdbcTemplate.update("UPDATE users SET username = ? WHERE user_id = ?", username, id);
    }
    @Override
    public void updateFistName(long id, String first_name) {
        jdbcTemplate.update("UPDATE users SET first_name = ? WHERE user_id = ?", first_name, id);
    }
    @Override
    public void updateLastName(long id, String last_name) {
        jdbcTemplate.update("UPDATE users SET last_name = ? WHERE user_id = ?", last_name, id);
    }
    @Override
    public void updatePhone(long id, String phone) {
        jdbcTemplate.update("UPDATE users SET phone = ? WHERE user_id = ?", phone, id);
    }

    @Override
    public void updateEmail(long id, String email) {
        jdbcTemplate.update("UPDATE users SET email = ? WHERE user_id = ?", email, id);
    }

    @Override
    public void updateUser(long id, String username, String password, String first_name, String last_name, String email, String phone) {
        jdbcTemplate.update("UPDATE users SET username = ?, password = ?, first_name = ?, last_name = ?, email = ?, phone = ? WHERE userid = ?", username, password, first_name, last_name, email, phone);
    }


}
