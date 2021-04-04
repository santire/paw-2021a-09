package ar.edu.itba.paw.model;

import java.util.Date;

public class Reservation {

    long id;
    long restaurantId;
    long userId;
    Date date;
    long quantity;

    public Reservation(long id, long userId, long restaurantId, Date date, long quantity){
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.date = date;
        this.quantity = quantity;
    }

}
