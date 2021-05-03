package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RestaurantDaoImpl implements RestaurantDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantDaoImpl.class);

    @Autowired
    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Restaurant> RESTAURANT_ROW_MAPPER = (rs, rowNum) -> {
                Restaurant restaurant = new Restaurant(
                rs.getLong("restaurant_id"), rs.getString("name"), rs.getString("address"), rs.getString("phone_number"),
                rs.getFloat("rating"), rs.getLong("user_id"));
            byte[] imageData = rs.getBytes("image_data");
            Image profileImage = null;
            if (imageData != null) {
                profileImage = new Image(imageData);
            }
            restaurant.setProfileImage(profileImage);
            return restaurant;
    };

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
            if (menuItemId != 0 && !menuItemName.trim().isEmpty()){
                restaurant.addMenuItem(menuItem);
            }
            byte[] imageData = rs.getBytes("image_data");
            Image profileImage = null;
            if (imageData != null) {
                profileImage = new Image(imageData);
            }
            restaurant.setProfileImage(profileImage);
        }
        return restaurantsById.values();
    };

    @Autowired
    public RestaurantDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("restaurants")
            .usingGeneratedKeyColumns("restaurant_id");
    }

    // CREATE

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
    public boolean setImageByRestaurantId(Image image, long restaurantId) {
        byte[] imageData = image.getData();
        int response = jdbcTemplate.update(
                "INSERT INTO restaurant_images(image_data, restaurant_id)"
                +
                " VALUES(?, ?) ON CONFLICT(restaurant_id) DO UPDATE"
                +
                " SET image_data=?", imageData, restaurantId, imageData);
       return response == 1; 
    }

    // READ

    @Override
    public Optional<Restaurant> findById(long id) {
        return jdbcTemplate.query(
                "SELECT r.*, i.image_data"
                +
                " FROM restaurants r"
                +
                " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id"
                +
                " WHERE r.restaurant_id=?"
                , RESTAURANT_ROW_MAPPER, id)
                .stream().findFirst();

    }

    @Override
    public Optional<Restaurant> findByIdWithMenu(long id, int menuPage, int amountOnMenuPage) {
        LOGGER.error("MENU PAGE: {}", menuPage);
        return jdbcTemplate.query(
                " SELECT r.*, m.menu_item_id, m.name as menu_item_name, m.description, m.price, m.restaurant_id, i.image_data"
                + 
                " FROM restaurants r"
                +
                " LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id"
                +
                " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id"
                +
                "  WHERE r.restaurant_id = ?"
                +
                " ORDER BY menu_item_id DESC"
                +
                " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
                ,RESTAURANT_NESTED_MAPPER, id, (menuPage-1)*amountOnMenuPage, amountOnMenuPage)
                .stream().findFirst();
    }

    @Override
    public int findByIdWithMenuPagesCount(int amountOnMenuPage, long id) {

        int amount = jdbcTemplate.query(
                " SELECT CEILING(COUNT(m.menu_item_id)::numeric/?) as c"
                + 
                " FROM restaurants r"
                +
                " LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id"
                +
                "  WHERE r.restaurant_id = ?"
                ,(r, n) -> r.getInt("c"), amountOnMenuPage, id)
                .stream().findFirst().orElse(0);
        return amount <= 0 ? 1 : amount;

    }

    @Override
    public List<Restaurant> findByName(String name) {
        return jdbcTemplate.query(
                " SELECT r.*, m.menu_item_id, m.name as menu_item_name, m.description, m.price, m.restaurant_id, i.image_data"
                +
                " FROM restaurants r"
                +
                " LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id"
                + 
                " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id"
                + 
                " WHERE name LIKE ?",
                RESTAURANT_NESTED_MAPPER, name + '%').stream().collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> getAllRestaurants(int page, int amountOnPage) {
        return jdbcTemplate.query(
                "SELECT * FROM restaurants r"
                +
                " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id"
                +
                " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
                ,RESTAURANT_ROW_MAPPER, (page-1)*amountOnPage, amountOnPage).stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> getRestaurantsFromOwner(int page, int amountOnPage, long userId) {
        return jdbcTemplate.query(
                "SELECT * FROM restaurants r"
                +
                " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id"
                +
                " WHERE user_id = ?"
                +
                " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
                , RESTAURANT_ROW_MAPPER, userId, (page-1)*amountOnPage, amountOnPage).stream()
                .collect(Collectors.toList());
    }

   @Override
   public int getRestaurantsFromOwnerPagesCount(int amountOnPage, long userId) {
        int amount =  jdbcTemplate.query(
                "SELECT CEILING(COUNT(*)::numeric/?) as c FROM restaurants"
                +
                " WHERE user_id = ?"
                , (r,s) -> r.getInt("c"), amountOnPage, userId)
                .stream().findFirst().orElse(0);
        return amount <= 0 ? 1 : amount;
   }

    @Override
    public int getAllRestaurantPagesCount(int amountOnPage, String searchTerm) {
        int amount =  jdbcTemplate.query(
                "SELECT CEILING(COUNT(*)::numeric/?) as c FROM restaurants"
                + 
                " WHERE name ILIKE ?"
                ,(r, n) -> r.getInt("c"), amountOnPage, '%' + searchTerm + '%')
                .stream().findFirst().orElse(0);
        return amount <= 0 ? 1 : amount;
    }

    @Override
    public List<Restaurant> getAllRestaurants(int page, int amountOnPage, String searchTerm) {
        return jdbcTemplate
            .query(
                "SELECT * FROM restaurants r"
                +
                " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id"
                +
                " WHERE name ILIKE ?"
                +
                " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
                , RESTAURANT_ROW_MAPPER, "%" + searchTerm.trim() + "%", (page-1)*amountOnPage, amountOnPage)
                .stream().collect(Collectors.toList());
    }

    // @Override
    // public List<Restaurant> getAllRestaurants(String searchTerm) {
       // return jdbcTemplate
                // .query("SELECT *, similarity(name, ?) AS sml FROM restaurants r"
                        // +
                        // " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id WHERE similarity(name, ?)>0 ORDER BY sml DESC, name ", RESTAURANT_ROW_MAPPER, searchTerm, searchTerm)
                // .stream().collect(Collectors.toList());
        // return jdbcTemplate
                // .query("SELECT * FROM restaurants r"
                        // +
                        // " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id WHERE name ILIKE ?", RESTAURANT_ROW_MAPPER, '%' + searchTerm + '%')
                // .stream().collect(Collectors.toList());
    // }

    // TODO: this would probably be better as getRestaurantsByMinRating and
    // pass the rating as an argument
    @Override
    public List<Restaurant> getPopularRestaurants(int limit, int minValue) {
        return jdbcTemplate.query(
                "SELECT * FROM restaurants r"
                +
                " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id WHERE rating >= ?"
                +
                " ORDER BY rating DESC"
                +
                " FETCH NEXT ? ROWS ONLY"
                , RESTAURANT_ROW_MAPPER, minValue, limit).stream()
                .collect(Collectors.toList());
    }

    // UPDATE

    @Override
    public void updateName(long id, String name) {
        jdbcTemplate.update("UPDATE restaurants SET name = ? WHERE restaurant_id = ?", name, id);
    }
    @Override
    public void updateAddress(long id, String address) {
        jdbcTemplate.update("UPDATE restaurants SET address = ? WHERE restaurant_id = ?", address, id);
    }
    @Override
    public void updatePhoneNumber(long id, String phoneNumber) {
        jdbcTemplate.update("UPDATE restaurants SET phone_number = ? WHERE restaurant_id = ?", phoneNumber, id);
    }
    @Override
    public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber) {
        jdbcTemplate.update("UPDATE restaurants SET name = ?, address = ?, phone_number = ? WHERE restaurant_id = ?", name, address, phoneNumber, id);
        return findById(id);
    }
    @Override
    public void updateRating(long id, int rating){
        jdbcTemplate.update("UPDATE restaurants SET rating = ? WHERE restaurant_id = ?", rating, id);
    }

    // DESTROY

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

    // ??

    @Override
    public Optional<User> findRestaurantOwner(long id) {

        Long userId = jdbcTemplate.query("SELECT * FROM restaurants WHERE restaurant_id =?", new Object[] { id },
                (rs, rowNum) -> new Long(rs.getString("user_id"))).stream().findFirst().orElse(new Long(-1));

        return userDao.findById(userId);
    }


}
