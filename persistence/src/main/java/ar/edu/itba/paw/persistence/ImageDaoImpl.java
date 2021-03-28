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

    private static final RowMapper<Image> IMAGE_ROW_MAPPER_ROW_MAPPER = (rs, rowNum) ->
            new Image(rs.getLong("imageid"), rs.getBytes("file"), rs.getString("extension"), rs.getLong("userid"));

    // TODO: Check if worth later
    private static final RowMapper<Image> RESTAURANT_IMAGE_ROW_MAPPER_ROW_MAPPER = (rs, rowNum) ->
            new Image(rs.getLong("imageid"), rs.getBytes("file"), rs.getString("extension"), rs.getLong("restaurantid"));

    @Autowired
    public ImageDaoImpl(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertUserImage = new SimpleJdbcInsert(ds)
                .withTableName("userImages")
                .usingGeneratedKeyColumns("imageid")
                .usingColumns("file", "extension", "userid");

        jdbcInsertRestaurantImage = new SimpleJdbcInsert(ds)
                .withTableName("restaurantImages")
                .usingGeneratedKeyColumns("imageid")
                .usingColumns("file", "extension", "restaurantid");
    }

    @Override
    public Optional<Image> getImageByUserId(long userId) {
        return jdbcTemplate.query("SELECT * FROM userImages WHERE userid = ?", new Object[]{userId}, IMAGE_ROW_MAPPER_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Image> getImageByRestaurantId(long restaurantId) {
        return jdbcTemplate.query("SELECT * FROM restaurantImages WHERE restaurantid = ?", new Object[]{restaurantId}, IMAGE_ROW_MAPPER_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public boolean saveUserImage(Image image){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("file", image.getData());
        params.addValue("extension", image.getType());
        params.addValue("userid", image.getOwnerId());

        jdbcInsertUserImage.execute(params);
        return true;
    }

    @Override
    public boolean saveRestaurantImage(Image image){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("file", image.getData());
        params.addValue("extension", image.getType());
        params.addValue("restaurantid", image.getOwnerId());

        jdbcInsertRestaurantImage.execute(params);
        return true;
    }

    @Override
    public boolean userHasImage(long userId) {
        Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM userImages WHERE userid = ?", new Object[]{userId}, Integer.class);
        if(count == 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean restaurantHasImage(long restaurantId) {
        Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM restaurantImages WHERE userid = ?", new Object[]{restaurantId}, Integer.class);
        if(count == 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateUserImage(Image image){
        jdbcTemplate.update("UPDATE userImages SET file = ? WHERE userid = ?", image.getData(), image.getOwnerId());
        return true;
    }

    @Override
    public boolean updateRestaurantImage(Image image){
        jdbcTemplate.update("UPDATE restaurantImages SET file = ? WHERE restaurantid = ?", image.getData(), image.getOwnerId());
        return true;
    }
}
