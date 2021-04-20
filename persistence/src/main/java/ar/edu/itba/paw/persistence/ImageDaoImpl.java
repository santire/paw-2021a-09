package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;


@Repository
public class ImageDaoImpl implements ImageDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertUserImage;
    private SimpleJdbcInsert jdbcInsertRestaurantImage;

    private static final RowMapper<Image> RESTAURANT_IMAGE_ROW_MAPPER_ROW_MAPPER = (rs, rowNum) ->
            new Image(rs.getLong("image_id"), rs.getBytes("image_data"));

    @Autowired
    public ImageDaoImpl(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertRestaurantImage = new SimpleJdbcInsert(ds)
                .withTableName("restaurant_images")
                .usingGeneratedKeyColumns("image_id")
                .usingColumns("image_data");
    }


    @Override
    public Optional<Image> getImageByRestaurantId(long restaurantId) {
        return jdbcTemplate.query(
                "SELECT * FROM restaurant_images"
                +
                " WHERE restaurant_id = ?", new Object[]{restaurantId}
                , RESTAURANT_IMAGE_ROW_MAPPER_ROW_MAPPER).stream().findFirst();
    }


    @Override
    public boolean saveRestaurantImage(Image image){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("image_data", image.getData());
        // params.addValue("restaurant_id", image.getOwnerId());

        jdbcInsertRestaurantImage.execute(params);
        return true;
    }

    @Override
    public boolean restaurantHasImage(long restaurantId) {
        // Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM restaurant_images WHERE restaurant_id = ?", new Object[]{restaurantId}, Integer.class);
        // if(count == 0) {
            // return false;
        // }
        return true;
    }

    @Override
    public boolean updateRestaurantImage(Image image){
        // jdbcTemplate.update("UPDATE restaurant_images SET file = ? WHERE restaurant_id = ?", image.getData(), image.getOwnerId());
        return true;
    }
}
