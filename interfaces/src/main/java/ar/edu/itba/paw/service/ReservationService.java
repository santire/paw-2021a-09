package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Reservation;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface ReservationService {

    // CREATE
    public Reservation addReservation(long userId, long restaurantId, LocalDateTime date, long quantity);

    // READ
    public List<Reservation> findByUser(long userId);
    public List<Reservation> findByUser(int page, int amountOnPage, long userId);
    public int findByUserPageCount(int amountOnPage, long userId);

    public List<Reservation> findByRestaurant(long restaurantId);
    public List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId);
    public List<Reservation> findConfirmedByRestaurant(int page, int amountOnPage, long restaurantId);
    public List<Reservation> findPendingByRestaurant(int page, int amountOnPage, long restaurantId);
    public int findByRestaurantPageCount(int amountOnPage, long restaurantId);
    public HashMap<String, List<Reservation>> findByRestaurantByConfirmation(long restaurantId);

    public Optional<Reservation> findById(int id);

    // UPDATE
    public Optional<Reservation> modifyReservation(int reservationId, Date date, long quantity);
    public boolean confirmReservation(int reservationId);

    //DESTROY
    public boolean cancelReservation(int id);

}
