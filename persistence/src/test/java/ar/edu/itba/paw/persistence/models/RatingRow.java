package ar.edu.itba.paw.persistence.models;

import org.springframework.jdbc.core.RowMapper;

public class RatingRow {

    public static RowMapper<RatingRow> rowMapper = ((rs, rowNum) ->
            new RatingRow(
                    rs.getLong("rating_id"),
                    rs.getFloat("rating"),
                    rs.getLong("user_id"),
                    rs.getLong("restaurant_id")
            ));
    private Long id;
    private Long restaurantId;
    private Long userId;
    private float rating;

    public RatingRow(Long id, float rating, Long userId, Long restaurantId) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
