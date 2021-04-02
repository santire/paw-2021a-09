package ar.edu.itba.paw.persistence;
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
public class RestaurantDaoImpl implements RestaurantDao {

    private static final RowMapper<Restaurant> RESTAURANT_ROW_MAPPER = (rs, rowNum) -> new Restaurant(
            rs.getLong("restaurant_id"),
            rs.getString("name"),
            rs.getString("address"),
            rs.getString("phone_number"),
            rs.getFloat("rating"),
            rs.getLong("user_id")
    );

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public RestaurantDaoImpl(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("restaurants").usingGeneratedKeyColumns("restaurant_id");
    }

    @Override
    public Optional<Restaurant> findById(long id){
        return jdbcTemplate.query("SELECT * FROM restaurants WHERE restaurant_id = ?", RESTAURANT_ROW_MAPPER, id)
                .stream().findFirst();
    }

    @Override
    public Optional<Restaurant> findByName(String name){
        return jdbcTemplate.query("SELECT * FROM restaurants WHERE name LIKE ?", RESTAURANT_ROW_MAPPER, name + "%")
                .stream().findFirst();
    }

    //SELECT * FROM restaurants WHERE name ILIKE '%?%"

    @Override
    public Restaurant registerRestaurant(String name, String address, String phoneNumber,
                                         float rating, long userId){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
        params.addValue("address", address);
        params.addValue("phone_number", phoneNumber);
        params.addValue("rating", rating);
        params.addValue("user_id", userId);
        final Number restaurantId = jdbcInsert.executeAndReturnKey(params);
        return new Restaurant(restaurantId.longValue(), name, address, phoneNumber,rating,userId);
    }

    @Override
    public List<Restaurant> getAllRestaurants(){
        return jdbcTemplate.query("SELECT * FROM restaurants", RESTAURANT_ROW_MAPPER).stream().collect(Collectors.toList());
    }
}
