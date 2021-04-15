package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RatingDaoImpl implements RatingDao {
    private static final RowMapper<Rating> RATING_ROW_MAPPER = (rs, rowNum) -> new Rating(
            rs.getLong("user_id"),
            rs.getLong("restaurant_id"),
            rs.getInt("rating")
    );

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public RatingDaoImpl(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("ratings").usingGeneratedKeyColumns("rating_id");
    }

    @Override
    public Optional<Rating> getRating(long userId, long restaurantId){
        return jdbcTemplate.query("SELECT * FROM ratings WHERE user_id = ? AND restaurant_id = ?", RATING_ROW_MAPPER, new Object[]{userId, restaurantId})
                .stream().findFirst();
    }

    @Override
    public List<Rating> getRatedRestaurantsByUserId(long userId){
        return jdbcTemplate.query("SELECT * FROM ratings WHERE user_id = ?", RATING_ROW_MAPPER, userId).stream().collect(Collectors.toList());
    }

    @Override
    public Rating rateRestaurant(long userId, long restaurantId, int rating){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("restaurant_id", restaurantId);
        params.addValue("rating", rating);
        jdbcInsert.execute(params);
        return new Rating(userId, restaurantId, rating);
    }

    @Override
    public boolean modifyRestaurantRating(long userId, long restaurantId, int rating){
        jdbcTemplate.update("UPDATE ratings SET rating = ? WHERE user_id = ? AND restaurant_id = ?", rating, userId, restaurantId);
        return true;
    }

}