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
    public int findByUserCount( long userId);

    public List<Reservation> findByUserHistory(int page, int amountOnPage, long userId);
    public int findByUserHistoryCount(long userId);

    public List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId);
    public int findByRestaurantCount(long restaurantId);

    public List<Reservation> findConfirmedByRestaurant(int page, int amountOnPage, long restaurantId);
    public int findConfirmedByRestaurantCount( long restaurantId);

    public List<Reservation> findPendingByRestaurant(int page, int amountOnPage, long restaurantId);
    public int findPendingByRestaurantCount(long restaurantId);

    public List<Reservation> findHistoryByRestaurant(int page, int amountOnPage, long restaurantId);
    public int findHistoryByRestaurantCount( long restaurantId);

    // UPDATE
    public boolean confirmReservation(long reservationId);

    //DESTROY
    public boolean ownerCancelReservation(long reservationId, String message);
    public boolean userCancelReservation(long reservationId);


}
