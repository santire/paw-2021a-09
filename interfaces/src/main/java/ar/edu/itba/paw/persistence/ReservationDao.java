package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationDao {

    // CREATE
    public Reservation addReservation(long userId, long restaurantId, LocalDateTime date, long quantity);

    // READ
    public List<Reservation> findByUser(long userId);
    public List<Reservation> findByUser(int page, int amountOnPage, long userId, Timestamp currentTime);
    public int findByUserPageCount(int amountOnPage, long userId, Timestamp currentTime);

    public List<Reservation> findByRestaurant(long restaurantId);
    public List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId);
    public List<Reservation> findConfirmedByRestaurant(int page, int amountOnPage, long restaurantId, Timestamp currentTime);
    public List<Reservation> findPendingByRestaurant(int page, int amountOnPage, long restaurantId, Timestamp currentTime);
    public int findByRestaurantPageCount(int amountOnPage, long restaurantId);
    public int findConfirmedByRestaurantPageCount(int amountOnPage, long restaurantId, Timestamp currentTime);
    public int findPendingByRestaurantPageCount(int amountOnPage, long restaurantId, Timestamp currentTime);

    public Optional<Reservation> findById(int id);

    // UPDATE
    public Optional<Reservation> modifyReservation(int reservationId, LocalDateTime date, long quantity);
    public boolean confirmReservation(int reservationId);

    // DESTROY
    public boolean cancelReservation(int id);

}
