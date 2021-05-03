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
        rs.getString("phone"),
        rs.getBoolean("is_active")
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
    public boolean isTheRestaurantOwner(long userId, long restaurantId) {

        return jdbcTemplate.query(
                "SELECT COUNT(*) as c"
                +
                " FROM restaurants"
                +
                " WHERE user_id = ? AND restaurant_id = ?"
                ,(r,n) -> r.getInt("c"), userId, restaurantId)
                .stream().findFirst().orElse(0) > 0;
    }

    @Override
    public boolean isRestaurantOwner(long userId) {

        return jdbcTemplate.query(
                "SELECT COUNT(*) as c"
                +
                " FROM restaurants"
                +
                " WHERE user_id = ?"
                ,(r,n) -> r.getInt("c"), userId)
                .stream().findFirst().orElse(0) > 0;
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

        LOGGER.debug("INSERTED INACTIVE USER. RETURNED ID: {}", userId.longValue());
        User newUser = new User(userId.longValue(),username,password, firstName,lastName,email,phone);
        return Optional.of(newUser);
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
    public void deleteToken(String token) {
        try {
            jdbcTemplate.update("DELETE FROM verification_tokens WHERE token = ?", token);
        } catch (Exception e) {
            LOGGER.error("Something went wrong deleting token {}", token);
        }
    }

    @Override
    public void updateUser(long id, String username, String password, String firstName, String lastName, String email, String phone) {
        jdbcTemplate.update(
                "UPDATE users"
                +
                " SET username = ?, password = ?, first_name = ?,"
                +
                " last_name = ?, email = ?, phone = ? WHERE user_id = ?"
                , username, password, firstName, lastName, email, phone, id);
    }


}
