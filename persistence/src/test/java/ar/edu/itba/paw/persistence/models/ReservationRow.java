package ar.edu.itba.paw.persistence.models;

import org.springframework.jdbc.core.RowMapper;

public class ReservationRow {
    public static RowMapper<ReservationRow> rowMapper = ((rs, rowNum) -> new ReservationRow(
            rs.getLong("reservation_id"),
            rs.getLong("user_id"),
            rs.getLong("restaurant_id"),
            rs.getString("date"),
            rs.getInt("quantity"),
            rs.getString("status")
    ));
    private Long id;
    private Long userId;
    private Long restaurantId;
    private String date;
    private int quantity;
    private String status;

    public ReservationRow(Long id, Long userId, Long restaurantId, String date, int quantity, String status) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.date = date;
        this.quantity = quantity;
        this.status = status;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
