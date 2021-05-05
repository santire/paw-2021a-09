package ar.edu.itba.paw.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Reservation {

    private long id;
    private long restaurantId;
    private long userId;
    private LocalDateTime date;
    private long quantity;
    private boolean confirmed;

    private User user;
    private Restaurant restaurant;

    public Reservation(long id, long userId, long restaurantId, LocalDateTime date, long quantity) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.date = date;
        this.quantity = quantity;
    }
    public Reservation(long id, long userId, long restaurantId, LocalDateTime date, long quantity, Restaurant restaurant) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.date = date;
        this.quantity = quantity;
        this.restaurant = restaurant;
    }

    public Reservation(long id, long userId, long restaurantId, Timestamp date, long quantity, boolean confirmed) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.date = date.toLocalDateTime();
        this.quantity = quantity;
        this.confirmed = confirmed;
    }


    public Reservation(long userId, long restaurantId, LocalDateTime date, long quantity) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.date = date;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

    public Restaurant getRestaurant() { return restaurant; }

    public boolean isConfirmed() { return confirmed; }

    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
