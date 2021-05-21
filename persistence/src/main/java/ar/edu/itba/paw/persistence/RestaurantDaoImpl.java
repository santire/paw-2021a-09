package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// @Repository
// public class RestaurantDaoImpl implements RestaurantDao {

    // private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantDaoImpl.class);

    // @Autowired
    // private UserDao userDao;
    // private JdbcTemplate jdbcTemplate;
    // private final SimpleJdbcInsert jdbcInsert;

    // private static final RowMapper<Restaurant> RESTAURANT_ROW_MAPPER = (rs, rowNum) -> {
                // Restaurant restaurant = new Restaurant(
                // rs.getLong("restaurant_id"), rs.getString("name"), rs.getString("address"), rs.getString("phone_number"),
                // rs.getFloat("rating"), rs.getLong("user_id"));
            // byte[] imageData = rs.getBytes("image_data");
            // Image profileImage = null;
            // if (imageData != null) {
                // profileImage = new Image(imageData);
            // }
            // restaurant.setProfileImage(profileImage);
            // like and hot not always present
            // LOGGER.debug("REST NAME: {}, HOT: {}, LIKES: {}", restaurant.getName(), rs.getString("hotness"), rs.getString("likes"));
            // return restaurant;
    // };

    // private static final ResultSetExtractor<Collection<Restaurant>> RESTAURANT_NESTED_MAPPER = (rs) -> {
        // Map<Long, Restaurant> restaurantsById = new HashMap<>();
        // while (rs.next()) {
            // Long id = rs.getLong("restaurant_id");
            // String name = rs.getString("name");
            // String address = rs.getString("address");
            // String phoneNumber = rs.getString("phone_number");
            // Float rating = rs.getFloat("rating");
            // Long userId = rs.getLong("user_id");
            // Restaurant restaurant = restaurantsById.get(id);
            // if (restaurant == null) {
                // restaurant = new Restaurant(id, name, address, phoneNumber, rating, userId);
                // restaurantsById.put(restaurant.getId(), restaurant);
            // }
            // Long menuItemId = rs.getLong("menu_item_id");
            // String menuItemName = rs.getString("menu_item_name");
            // String menuItemDescrip = rs.getString("description");
            // Float menuItemPrice = rs.getFloat("price");
            // MenuItem menuItem = new MenuItem(menuItemId, menuItemName, menuItemDescrip, menuItemPrice);
            // if (menuItemId != 0 && !menuItemName.trim().isEmpty()){
                // restaurant.addMenuItem(menuItem);
            // }
            // byte[] imageData = rs.getBytes("image_data");
            // Image profileImage = null;
            // if (imageData != null) {
                // profileImage = new Image(imageData);
            // }
            // restaurant.setProfileImage(profileImage);
        // }
        // return restaurantsById.values();
    // };

    // @Autowired
    // public RestaurantDaoImpl(final DataSource ds) {
        // jdbcTemplate = new JdbcTemplate(ds);
        // jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            // .withTableName("restaurants")
            // .usingGeneratedKeyColumns("restaurant_id");
    // }

    // CREATE

    // @Override
    // public Restaurant registerRestaurant(String name, String address, String phoneNumber, float rating, long userId) {
        // MapSqlParameterSource params = new MapSqlParameterSource();
        // params.addValue("name", name);
        // params.addValue("address", address);
        // params.addValue("phone_number", phoneNumber);
        // params.addValue("rating", rating);
        // params.addValue("user_id", userId);
        // final Number restaurantId = jdbcInsert.executeAndReturnKey(params);
        // return new Restaurant(restaurantId.longValue(), name, address, phoneNumber, rating, userId);
    // }

    // @Override
    // public boolean setImageByRestaurantId(Image image, long restaurantId) {
        // byte[] imageData = image.getData();
        // int response = jdbcTemplate.update(
                // "INSERT INTO restaurant_images(image_data, restaurant_id)"
                // +
                // " VALUES(?, ?) ON CONFLICT(restaurant_id) DO UPDATE"
                // +
                // " SET image_data=?", imageData, restaurantId, imageData);
       // return response == 1; 
    // }

    // READ

    // @Override
    // public Optional<Restaurant> findById(long id) {
        // return jdbcTemplate.query(
                // "SELECT r.*, i.image_data"
                // +
                // " FROM restaurants r"
                // +
                // " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id"
                // +
                // " WHERE r.restaurant_id=?"
                // , RESTAURANT_ROW_MAPPER, id)
                // .stream().findFirst();

    // }

    // @Override
    // public Optional<Restaurant> findByIdWithMenu(long id, int menuPage, int amountOnMenuPage) {
        // LOGGER.error("MENU PAGE: {}", menuPage);
        // return jdbcTemplate.query(
                // " SELECT r.*, m.menu_item_id, m.name as menu_item_name, m.description, m.price, m.restaurant_id, i.image_data"
                // + 
                // " FROM restaurants r"
                // +
                // " LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id"
                // +
                // " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id"
                // +
                // "  WHERE r.restaurant_id = ?"
                // +
                // " ORDER BY menu_item_id ASC"
                // +
                // " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
                // ,RESTAURANT_NESTED_MAPPER, id, (menuPage-1)*amountOnMenuPage, amountOnMenuPage)
                // .stream().findFirst();
    // }

    // @Override
    // public int findByIdWithMenuPagesCount(int amountOnMenuPage, long id) {

        // int amount = jdbcTemplate.query(
                // " SELECT CEILING(COUNT(m.menu_item_id)::numeric/?) as c"
                // + 
                // " FROM restaurants r"
                // +
                // " LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id"
                // +
                // "  WHERE r.restaurant_id = ?"
                // ,(r, n) -> r.getInt("c"), amountOnMenuPage, id)
                // .stream().findFirst().orElse(0);
        // return amount <= 0 ? 1 : amount;

    // }

    // @Override
    // public List<Restaurant> findByName(String name) {
        // return jdbcTemplate.query(
                // " SELECT r.*, m.menu_item_id, m.name as menu_item_name, m.description, m.price, m.restaurant_id, i.image_data"
                // +
                // " FROM restaurants r"
                // +
                // " LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id"
                // + 
                // " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id"
                // + 
                // " WHERE name LIKE ?",
                // RESTAURANT_NESTED_MAPPER, name + '%').stream().collect(Collectors.toList());
    // }

    // @Override
    // public List<Restaurant> getAllRestaurants(int page, int amountOnPage) {
        // return jdbcTemplate.query(
                // "SELECT * FROM restaurants r"
                // +
                // " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id"
                // +
                // " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
                // ,RESTAURANT_ROW_MAPPER, (page-1)*amountOnPage, amountOnPage).stream()
                // .collect(Collectors.toList());
    // }

    // @Override
    // public List<Restaurant> getRestaurantsFromOwner(int page, int amountOnPage, long userId) {
        // return jdbcTemplate.query(
                // "SELECT * FROM restaurants r"
                // +
                // " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id"
                // +
                // " WHERE user_id = ?"
                // +
                // " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
                // , RESTAURANT_ROW_MAPPER, userId, (page-1)*amountOnPage, amountOnPage).stream()
                // .collect(Collectors.toList());
    // }

   // @Override
   // public int getRestaurantsFromOwnerPagesCount(int amountOnPage, long userId) {
        // int amount =  jdbcTemplate.query(
                // "SELECT CEILING(COUNT(*)::numeric/?) as c FROM restaurants"
                // +
                // " WHERE user_id = ?"
                // , (r,s) -> r.getInt("c"), amountOnPage, userId)
                // .stream().findFirst().orElse(0);
        // return amount <= 0 ? 1 : amount;
   // }

    // @Override
    // public int getAllRestaurantPagesCount(int amountOnPage, String searchTerm) {
        // int amount =  jdbcTemplate.query(
                // "SELECT CEILING(COUNT(*)::numeric/?) as c FROM restaurants"
                // + 
                // " WHERE name ILIKE ?"
                // ,(r, n) -> r.getInt("c"), amountOnPage, '%' + searchTerm + '%')
                // .stream().findFirst().orElse(0);
        // return amount <= 0 ? 1 : amount;
    // }

    // @Override
    // public List<Restaurant> getAllRestaurants(int page, int amountOnPage, String searchTerm) {
        // return jdbcTemplate
            // .query(
                // "SELECT * FROM restaurants r"
                // +
                // " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id"
                // +
                // " WHERE name ILIKE ?"
                // +
                // " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
                // , RESTAURANT_ROW_MAPPER, "%" + searchTerm.trim() + "%", (page-1)*amountOnPage, amountOnPage)
                // .stream().collect(Collectors.toList());
    // }

    // TODO: this would probably be better as getRestaurantsByMinRating and
    // pass the rating as an argument
    // @Override
    // public List<Restaurant> getPopularRestaurants(int limit, int minValue) {
        // return jdbcTemplate.query(
                // "SELECT * FROM ("
                    // +
                    // " SELECT r.*, COUNT(like_id) as likes FROM restaurants r"
                    // +
                    // " LEFT JOIN likes l ON r.restaurant_id=l.restaurant_id"
                    // +
                    // " GROUP BY r.restaurant_id"
                    // +
                // ") AS r"
                // +
                // " LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id"
                // +
                // " WHERE likes >= ?"
                // +
                // " ORDER BY likes DESC"
                // +
                // " FETCH NEXT ? ROWS ONLY"
                // , RESTAURANT_ROW_MAPPER, minValue, limit).stream()
                // .collect(Collectors.toList());
    // }

    // @Override
    // public List<Restaurant> getHotRestaurants(int lastDays) {
        // return jdbcTemplate.query(
                // "SELECT hot.id as restaurant_id, name, address, phone_number, rating, user_id, image_data FROM( SELECT b.restaurant_id, COUNT(*) AS cant FROM reservations b"
                        // +
                        // " WHERE date- interval '"+lastDays+" DAYS' < CURRENT_TIMESTAMP GROUP BY b.restaurant_id ORDER BY cant DESC) AS hot(id,reservations) JOIN"
                        // +
                        // " (SELECT * FROM restaurants r LEFT JOIN restaurant_images i ON r.restaurant_id = i.restaurant_id) AS rest(id, name, address, phone_number, rating, user_id, image_id, image_data, r_id)"
                        // +
                        // "ON hot.id = rest.id ORDER BY reservations DESC, rating DESC ", RESTAURANT_ROW_MAPPER).stream()
                // .collect(Collectors.toList());
    // }


    // UPDATE

    // @Override
    // public void updateName(long id, String name) {
        // jdbcTemplate.update("UPDATE restaurants SET name = ? WHERE restaurant_id = ?", name, id);
    // }
    // @Override
    // public void updateAddress(long id, String address) {
        // jdbcTemplate.update("UPDATE restaurants SET address = ? WHERE restaurant_id = ?", address, id);
    // }
    // @Override
    // public void updatePhoneNumber(long id, String phoneNumber) {
        // jdbcTemplate.update("UPDATE restaurants SET phone_number = ? WHERE restaurant_id = ?", phoneNumber, id);
    // }
    // @Override
    // public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber) {
        // jdbcTemplate.update("UPDATE restaurants SET name = ?, address = ?, phone_number = ? WHERE restaurant_id = ?", name, address, phoneNumber, id);
        // return findById(id);
    // }
    // @Override
    // public void updateRating(long id, int rating){
        // jdbcTemplate.update("UPDATE restaurants SET rating = ? WHERE restaurant_id = ?", rating, id);
    // }

    // DESTROY

    // @Override
    // public boolean deleteRestaurantById(long id) {
        // String sql = "DELETE FROM restaurants WHERE restaurant_id = ?";
        // Object[] args = new Object[] { id };

        // return jdbcTemplate.update(sql, args) == 1;
    // }

    // @Override
    // public boolean deleteRestaurantByName(String name) {
        // String sql = "DELETE FROM restaurants WHERE name = ?";
        // Object[] args = new Object[] { name };

        // return jdbcTemplate.update(sql, args) == 1;
    // }

    // ??

    // @Override
    // public Optional<User> findRestaurantOwner(long id) {

        // Long userId = jdbcTemplate.query("SELECT * FROM restaurants WHERE restaurant_id =?", new Object[] { id },
                // (rs, rowNum) -> new Long(rs.getString("user_id"))).stream().findFirst().orElse(new Long(-1));

        // return userDao.findById(userId);
    // }


    // @Override
    // public boolean addTag(long restaurantId, int tagId) {
        // String sql = "INSERT INTO restaurant_tags(restaurant_id, tag_id) VALUES(?, ?) ";
        // Object[] args = new Object[] { restaurantId, tagId };

        // return jdbcTemplate.update(sql, args) == 1;
    // }

    // @Override
    // public boolean removeTag(long restaurantId, int tagId) {
        // String sql = "DELETE FROM restaurant_tags WHERE restaurant_id = ? AND tag_id = ?";
        // Object[] args = new Object[] { restaurantId, tagId };

        // return jdbcTemplate.update(sql, args) == 1;
    // }

    // @Override
    // public List<Tags> tagsInRestaurant(long restaurantId) {

        // List<Tags> tags = jdbcTemplate.query("SELECT * FROM restaurant_tags  WHERE restaurant_id =?", new Object[]{restaurantId},(rs, rowNum) ->
                // Tags.valueOf(rs.getInt("tag_id"))).stream().collect(Collectors.toList());

        // return tags;
    // }

    // @Override
    // public List<Restaurant> getRestaurantsWithTags(List<Tags> tags) {
        // String t = ",";

        // for(int i=0; i<tags.size(); i++){
            // t += tags.get(i).getValue();
            // if(i!=tags.size()-1)
                // t += ", ";
        // }

        // return jdbcTemplate.query(
                // "SELECT DISTINCT r.restaurant_id, name, address, phone_number, rating, user_id, image_data  FROM " +
                        // "(SELECT * FROM restaurants r NATURAL JOIN restaurant_tags WHERE tag_id IN ( 0"+ t +"))AS r " +
                        // "LEFT join restaurant_images i ON r.restaurant_id = i.restaurant_id"


                // , RESTAURANT_ROW_MAPPER).stream()
                // .collect(Collectors.toList());
    // }
    // @Override
    // public int getRestaurantsFilteredByPageCount(int amountOnPage, String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice) {

        // String TAG_CHECK_QUERY = " ";
        // if(!tags.isEmpty()){
            // TAG_CHECK_QUERY += " WHERE tag_id IN (";
            // for(int i=0; i<tags.size(); i++){
                // TAG_CHECK_QUERY += tags.get(i).getValue();
                // if(i!=tags.size()-1)
                    // TAG_CHECK_QUERY += ", ";
            // }
            // TAG_CHECK_QUERY+=")";
        // }

        // int amount = jdbcTemplate.query(
                // "SELECT CEILING(COUNT(r2.restaurant_id)::numeric/?) as c"
                // +
                // " FROM ("
                // +
                // " SELECT r.restaurant_id, AVG(price) as price"
                // +
                // " FROM ("
                        // +
                        // left join to get restaurants with no tag too
                        // " SELECT r1.* FROM restaurants r1 LEFT JOIN restaurant_tags rt"
                        // +
                        // " ON r1.restaurant_id = rt.restaurant_id"
                        // +
                        // TAG_CHECK_QUERY
                        // +
                        // ") AS r"
                    // +
                    // " LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id"
                    // +
                    // " WHERE r.name ILIKE ?"
                    // +
                    // " AND price BETWEEN ? AND ?"
                    // +
                    // " GROUP BY r.restaurant_id"
                    // +
                // ") AS r2"
                // , (r, n) -> r.getInt("c"), amountOnPage, "%"+name+"%", minAvgPrice, maxAvgPrice)
                // .stream()
                // .findFirst().orElse(0);
        // return amount <= 0 ? 1 : amount;
    // }

    // @Override
    // public List<Restaurant> getRestaurantsFilteredBy(int page, int amountOnPage, String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice, Sorting sort, boolean desc, int lastDays) {
        // String TAG_CHECK_QUERY = " ";
        // if(!tags.isEmpty()){
            // TAG_CHECK_QUERY += " WHERE tag_id IN (";
            // for(int i=0; i<tags.size(); i++){
                // TAG_CHECK_QUERY += tags.get(i).getValue();
                // if(i!=tags.size()-1)
                    // TAG_CHECK_QUERY += ", ";
            // }
            // TAG_CHECK_QUERY+=")";
        // }

        // String orderBy=sort.getSortType();
        // String order="DESC";
        // LOGGER.debug("SORTING FOR: {} {}", orderBy, order);
        // LOGGER.debug("Where: {}", TAG_CHECK_QUERY);


        // if(!desc)
            // order="ASC";

        // List<Restaurant> restaurants = jdbcTemplate.query(
                // "SELECT r.restaurant_id, r.name, r.address, r.phone_number,"
                // +
                        // " r.rating, r.user_id, image_data,"
                        // +
                        // " AVG(price) as price, COALESCE(q,0) as hot, COALESCE(l, 0) as likes"
                // +
                // " FROM ("
                    // +
                    // " SELECT r1.* FROM restaurants r1 LEFT JOIN restaurant_tags rt"
                    // +
                    // " ON r1.restaurant_id = rt.restaurant_id"
                    // +
                    // TAG_CHECK_QUERY
                    // +
                    // ") AS r"
                // +
                // " LEFT JOIN ("
                    // +
                    // " SELECT restaurant_id, COUNT(reservation_id)"
                    // +
                    // " FROM reservations"
                    // +
                    // " WHERE date > 'now'::timestamp - '"+lastDays+" day'::interval"
                    // +
                    // " GROUP BY restaurant_id"
                    // +
                    // ") AS hot(rid, q)"
                    // +
                    // " ON r.restaurant_id=hot.rid"
                // +
                // " LEFT JOIN ("
                    // +
                    // " SELECT restaurant_id, COUNT(like_id)"
                    // +
                    // " FROM likes"
                    // +
                    // " GROUP BY restaurant_id"
                    // +
                    // ") AS lik(rid, l)"
                    // +
                    // " ON r.restaurant_id=lik.rid"
                // +
                // " LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id"
                // +
                // " LEFT JOIN restaurant_images i ON r.restaurant_id=i.restaurant_id"
                // +
                // " WHERE r.name ILIKE ?"
                // +
                // " AND price BETWEEN ? AND ?"
                // +
                // " GROUP BY r.restaurant_id, r.name, r.phone_number, r.rating,"
                        // + " r.address, r.user_id, image_data, hot, likes"
                // +
                // " ORDER BY " + orderBy+ " " + order 
                // +
                // " OFFSET ? FETCH NEXT ? ROWS ONLY"
                // , RESTAURANT_ROW_MAPPER, "%"+name+"%", minAvgPrice, maxAvgPrice, (page-1)*amountOnPage, amountOnPage).stream()
                // .collect(Collectors.toList());

        // return restaurants;
    // }


// }
