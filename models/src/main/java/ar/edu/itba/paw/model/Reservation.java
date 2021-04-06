package ar.edu.itba.paw.model;

import java.util.Date;

public class Reservation {

    private long id;
    private long restaurantId;
    private long userId;
    private Date date;
    private long quantity;

    public Reservation(long id, long userId, long restaurantId, Date date, long quantity) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.date = date;
        this.quantity = quantity;
    }

    public Reservation(long userId, long restaurantId, Date date, long quantity) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

}
