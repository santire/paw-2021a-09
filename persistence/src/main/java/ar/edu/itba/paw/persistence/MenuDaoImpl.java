package ar.edu.itba.paw.persistence;

import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.model.MenuItem;

// @Repository
// public class MenuDaoImpl implements MenuDao {

    // private static final RowMapper<MenuItem> MENU_ITEM_ROW_MAPPER = (rs, rowNum) -> new MenuItem(
            // rs.getLong("menu_item_id"), rs.getString("name"), rs.getString("description"), rs.getFloat("price"));

    // private JdbcTemplate jdbcTemplate;
    // private final SimpleJdbcInsert jdbcInsert;

    // @Autowired
    // public MenuDaoImpl(final DataSource ds) {
        // jdbcTemplate = new JdbcTemplate(ds);
        // jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("menu_items")
                // .usingGeneratedKeyColumns("menu_item_id");
    // }

    // @Override
    // public List<MenuItem> findMenuByRestaurantId(long id) {
        // return jdbcTemplate.query("SELECT * FROM menu_items WHERE restaurant_id = ?", MENU_ITEM_ROW_MAPPER, id).stream()
                // .collect(Collectors.toList());
    // }

    // @Override
    // public void addItemToRestaurant(long restaurantId, MenuItem item) {
        // MapSqlParameterSource params = new MapSqlParameterSource();
        // params.addValue("name", item.getName().trim());
        // params.addValue("description", item.getDescription().trim());
        // params.addValue("price", item.getPrice());
        // params.addValue("restaurant_id", restaurantId);

        // jdbcInsert.execute(params);
    // }

    // @Override
    // public void deleteItemById(long menuId) {
        // jdbcTemplate.update("DELETE FROM menu_items where menu_item_id = ?", menuId);
    // }

// }
