package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationDao {

    // CREATE
    public Reservation addReservation(long userId, long restaurantId, Date date, long quantity);

    // READ
    public List<Reservation> findByUser(long userId);
    public List<Reservation> findByUser(int page, int amountOnPage, long userId);
    public int findByUserPageCount(int amountOnPage, long userId);

    public List<Reservation> findByRestaurant(long restaurantId);
    public List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId);
    public int findByRestaurantPageCount(int amountOnPage, long restaurantId);

    public Optional<Reservation> findById(int id);

    // UPDATE
    public Optional<Reservation> modifyReservation(int reservationId, Date date, long quantity);

    // DESTROY
    public boolean cancelReservation(int id);

}
