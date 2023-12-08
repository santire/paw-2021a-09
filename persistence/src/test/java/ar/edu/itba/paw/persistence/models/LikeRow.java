package ar.edu.itba.paw.persistence.models;

import org.springframework.jdbc.core.RowMapper;

public class LikeRow {
    public static RowMapper<LikeRow> rowMapper = ((rs, rowNum) -> new LikeRow(rs.getLong("like_id"),
            rs.getLong("user_id"),
            rs.getLong("restaurant_id")));
    private Long id;
    private Long userId;
    private Long restaurantId;

    public LikeRow(Long id, Long userId, Long restaurantId) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
