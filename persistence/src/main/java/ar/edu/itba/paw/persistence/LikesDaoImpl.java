package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LikesDaoImpl implements LikesDao {
    private static final RowMapper<Long> LIKES_ROW_MAPPER = (rs, rowNum) -> rs.getLong("restaurant_id");

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public LikesDaoImpl(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("likes").usingGeneratedKeyColumns("like_id");
    }

    @Override
    public boolean like(long userId, long restaurantId){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("restaurant_id", restaurantId);
        jdbcInsert.execute(params);
        return true;
    }

    @Override
    public boolean dislike(long userId, long restaurantId){
        String sql = "DELETE FROM likes WHERE user_id = ? AND restaurant_id = ?";
        Object[] args = new Object[] {userId, restaurantId};

        return jdbcTemplate.update(sql, args) == 1;
    }

    @Override
    public List<Long> getLikedRestaurantsId(long userId){
        return new ArrayList<>(jdbcTemplate.query("SELECT * FROM likes WHERE user_id = ?", LIKES_ROW_MAPPER, userId));
    }

    @Override
    public boolean userLikesRestaurant(long userId, long restaurantId){
        return jdbcTemplate.query("SELECT * FROM likes WHERE user_id =? AND restaurant_id = ?", new Object[]{userId, restaurantId},(rs, rowNum) ->
                Boolean.TRUE).stream().findFirst().orElse(false);
    }
}
