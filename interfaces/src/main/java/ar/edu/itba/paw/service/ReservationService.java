package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationService {

    // CREATE
    public Reservation addReservation(long userId, long restaurantId, LocalDateTime date, long quantity, String baseUrl);

    // READ
    public Optional<Reservation> findById(long reservationId);

    public List<Reservation> findByUser(int page, int amountOnPage, long userId);
    public int findByUserPageCount(int amountOnPage, long userId);

    public List<Reservation> findByUserHistory(int page, int amountOnPage, long userId);
    public int findByUserHistoryPageCount(int amountOnPage, long userId);

    public List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId);
    public int findByRestaurantPageCount(int amountOnPage, long restaurantId);

    public List<Reservation> findConfirmedByRestaurant(int page, int amountOnPage, long restaurantId);
    public int findConfirmedByRestaurantPageCount(int amountOnPage, long restaurantId);

    public List<Reservation> findPendingByRestaurant(int page, int amountOnPage, long restaurantId);
    public int findPendingByRestaurantPageCount(int amountOnPage, long restaurantId);

    // UPDATE
    public boolean confirmReservation(long reservationId);

    //DESTROY
    public boolean ownerCancelReservation(long reservationId, String message);
    public boolean userCancelReservation(long reservationId);


}
