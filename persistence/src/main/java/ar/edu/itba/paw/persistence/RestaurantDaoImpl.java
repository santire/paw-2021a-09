package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.sql.ResultSetMetaData;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RestaurantDaoImpl implements RestaurantDao {

    @Autowired
    private UserDao userDao;

    private static final RowMapper<Restaurant> RESTAURANT_ROW_MAPPER = (rs, rowNum) -> new Restaurant(
            rs.getLong("restaurant_id"), rs.getString("name"), rs.getString("address"), rs.getString("phone_number"),
            rs.getFloat("rating"), rs.getLong("user_id"));

    private static final ResultSetExtractor<Collection<Restaurant>> RESTAURANT_NESTED_MAPPER = (rs) -> {
        Map<Long, Restaurant> restaurantsById = new HashMap<>();
        while (rs.next()) {
            Long id = rs.getLong("restaurant_id");
            String name = rs.getString("name");
            String address = rs.getString("address");
            String phoneNumber = rs.getString("phone_number");
            Float rating = rs.getFloat("rating");
            Long userId = rs.getLong("user_id");
            Restaurant restaurant = restaurantsById.get(id);
            if (restaurant == null) {
                restaurant = new Restaurant(id, name, address, phoneNumber, rating, userId);
                restaurantsById.put(restaurant.getId(), restaurant);
            }
            Long menuItemId = rs.getLong("menu_item_id");
            String menuItemName = rs.getString("menu_item_name");
            String menuItemDescrip = rs.getString("description");
            Float menuItemPrice = rs.getFloat("price");
            MenuItem menuItem = new MenuItem(menuItemId, menuItemName, menuItemDescrip, menuItemPrice);
            if (menuItemId != 0 && !menuItemName.isBlank())
                restaurant.addMenuItem(menuItem);
        }
        return restaurantsById.values();
    };

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public RestaurantDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("restaurants")
                .usingGeneratedKeyColumns("restaurant_id");
    }

    @Override
    public Optional<Restaurant> findById(long id) {
        return jdbcTemplate.query(
                " SELECT r.*, m.menu_item_id, m.name as menu_item_name, m.description, m.price, m.restaurant_id"
                +
                " FROM restaurants r LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id  WHERE r.restaurant_id = ?"
                , RESTAURANT_NESTED_MAPPER, id)
                .stream().findFirst();
    }

    @Override
    public List<Restaurant> findByName(String name) {
        return jdbcTemplate.query(
                " SELECT r.*, m.menu_item_id, m.name as menu_item_name, m.description, m.price, m.restaurant_id"
                +
                " FROM restaurants r LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id WHERE name LIKE ?"
                , RESTAURANT_NESTED_MAPPER, name + '%')
                .stream().collect(Collectors.toList());
    }

    @Override
    public Restaurant registerRestaurant(String name, String address, String phoneNumber, float rating, long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
        params.addValue("address", address);
        params.addValue("phone_number", phoneNumber);
        params.addValue("rating", rating);
        params.addValue("user_id", userId);
        final Number restaurantId = jdbcInsert.executeAndReturnKey(params);
        return new Restaurant(restaurantId.longValue(), name, address, phoneNumber, rating, userId);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return jdbcTemplate.query("SELECT * FROM restaurants", RESTAURANT_ROW_MAPPER).stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> findAll() {
        return jdbcTemplate.query(
                 " SELECT r.*, m.menu_item_id, m.name as menu_item_name, m.description, m.price, m.restaurant_id"
                +
                 " FROM restaurants r LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id"
                  , RESTAURANT_NESTED_MAPPER)
            .stream().collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> getAllRestaurants(String searchTerm) {
        return jdbcTemplate
                .query("SELECT * FROM restaurants WHERE name ILIKE ?", RESTAURANT_ROW_MAPPER, "%" + searchTerm + "%")
                .stream().collect(Collectors.toList());
    }

    // TODO: this would probably be better as getRestaurantsByMinRating and
    // pass the rating as an argument
    @Override
    public List<Restaurant> getPopularRestaurants() {
        return jdbcTemplate.query("SELECT * FROM restaurants WHERE rating >= 9", RESTAURANT_ROW_MAPPER).stream()
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteRestaurantById(long id) {
        String sql = "DELETE FROM restaurants WHERE restaurant_id = ?";
        Object[] args = new Object[] { id };

        return jdbcTemplate.update(sql, args) == 1;
    }

    @Override
    public boolean deleteRestaurantByName(String name) {
        String sql = "DELETE FROM restaurants WHERE name = ?";
        Object[] args = new Object[] { name };

        return jdbcTemplate.update(sql, args) == 1;
    }

    @Override
    public Optional<User> findRestaurantOwner(long id) {

        Long userId = jdbcTemplate.query("SELECT * FROM restaurants WHERE restaurant_id =?", new Object[] { id },
                (rs, rowNum) -> new Long(rs.getString("user_id"))).stream().findFirst().orElse(new Long(-1));

        return userDao.findById(userId);
    }

    @Override
    public List<Restaurant> getRestaurantsFromOwner(long userId) {
        return jdbcTemplate.query("SELECT * FROM restaurants WHERE user_id = ?", RESTAURANT_ROW_MAPPER, userId).stream()
                .collect(Collectors.toList());
    }
}
