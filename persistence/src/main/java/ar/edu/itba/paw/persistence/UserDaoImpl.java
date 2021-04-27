package ar.edu.itba.paw.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import javax.sql.DataSource;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class UserDaoImpl implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

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

    private static final RowMapper<VerificationToken> TOKEN_ROW_MAPPER = (rs, rowNum) -> new VerificationToken(
        rs.getString("token"),
        rs.getTimestamp("created_at"),
        rs.getLong("user_id")
        );

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcInsertToken;

    @Autowired
    public UserDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("user_id");
        jdbcInsertToken = new SimpleJdbcInsert(jdbcTemplate).withTableName("verification_tokens").usingGeneratedKeyColumns("token_id");
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
    public Optional<User> register(final String username,final String password,final String firstName,final String lastName,final String email,final String phone) {


        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", username);
        params.addValue("password", password);
        params.addValue("first_name", firstName);
        params.addValue("last_name", lastName);
        params.addValue("email", email);
        params.addValue("phone", phone);
        params.addValue("is_active", false);
        Number userId;
        try {
            userId = jdbcInsert.executeAndReturnKey(params);
        } catch(DuplicateKeyException e) {
            LOGGER.warn("Can't register, email: {} already in use", email);
            return Optional.empty();
        }

        return Optional.of(new User(userId.longValue(),username,password, firstName,lastName,email,phone));
    }

    @Override
    public void assignTokenToUser(String token, Timestamp createdAt, long userId) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("token", token);
        params.addValue("created_at", createdAt);
        params.addValue("user_id", userId);

        jdbcInsertToken.execute(params);
    }

    @Override
    public Optional<VerificationToken> getToken(String token) {
        return jdbcTemplate.query("SELECT * FROM verification_tokens WHERE token = ?", TOKEN_ROW_MAPPER, token)
            .stream().findFirst();
    }

    @Override
    public Optional<User> activateUserById(long userId) {
        jdbcTemplate.update("UPDATE users SET is_active = true WHERE user_id = ?", userId);
        return findById(userId);
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
