package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationService {

    // CREATE
    Reservation addReservation(long userId, long restaurantId, LocalDateTime date, long quantity, String baseUrl);

    // READ
    Optional<Reservation> findById(long reservationId);

    List<Reservation> findByUser(int page, int amountOnPage, long userId);
    int findByUserCount( long userId);

    List<Reservation> findByUserHistory(int page, int amountOnPage, long userId);
    int findByUserHistoryCount(long userId);

    List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId);
    int findByRestaurantCount(long restaurantId);

    List<Reservation> findConfirmedByRestaurant(int page, int amountOnPage, long restaurantId);
    int findConfirmedByRestaurantCount( long restaurantId);

    List<Reservation> findPendingByRestaurant(int page, int amountOnPage, long restaurantId);
    int findPendingByRestaurantCount(long restaurantId);

    List<Reservation> findHistoryByRestaurant(int page, int amountOnPage, long restaurantId);
    int findHistoryByRestaurantCount( long restaurantId);

    // UPDATE
    boolean confirmReservation(long reservationId);

    //DESTROY
    boolean ownerCancelReservation(long reservationId, String message);
    boolean userCancelReservation(long reservationId);


}
