package ar.edu.itba.paw.persistence.models;

import org.springframework.jdbc.core.RowMapper;

public class CommentRow {
    public static RowMapper<CommentRow> rowMapper = ((rs, rowNum) ->
            new CommentRow(
                    rs.getLong("comment_id"),
                    rs.getString("date"),
                    rs.getString("user_comment"),
                    rs.getLong("restaurant_id"),
                    rs.getLong("user_id"))
    );
    private Long id;
    private String date;
    private String message;
    private Long restaurantId;
    private Long userId;

    public CommentRow(Long id, String date, String message, Long restaurantId, Long userId) {
        this.id = id;
        this.date = date;
        this.message = message;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
