package ar.edu.itba.paw.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;


import javax.persistence.Entity;
import javax.persistance.GeneratedValue;
import javax.persistance.GeneratedType;
import javax.persistence.Id;
import javax.persistance.SequenceGenerator;


public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservations_reservation_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "reservations_reservation_id_seq", name = "reservations_reservation_id_seq")
    private Long id;

    @Column
    private LocalDateTime date

    @Column
    private long quantity;

    @Column
    private boolean confirmed;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public Reservation(Long id, long quantity, boolean confirmed, User user, Restaurant restaurant) {
        this.id = id;
        this.quantity = quantity;
        this.confirmed = confirmed;
        this.user = user;
        this.restaurant = restaurant;
    }

    public Reservation(Long id, long quantity, User user, Restaurant restaurant) {
        this.id = id;
        this.quantity = quantity;
        this.user = user;
        this.restaurant = restaurant;
    }

    public Reservation(long quantity,User user, Restaurant restaurant) {
        this.quantity = quantity;
        this.user = user;
        this.restaurant = restaurant;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public boolean isConfirmed() {
        return this.confirmed;
    }

    public boolean getConfirmed() {
        return this.confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    }




}