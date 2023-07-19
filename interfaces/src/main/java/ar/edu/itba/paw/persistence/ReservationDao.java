package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationDao {

    // CREATE
    Reservation addReservation(User user, Restaurant restaurant, LocalDateTime date, long quantity);

    // READ
    List<Reservation> findByUser(int page, int amountOnPage, long userId, LocalDateTime currentTime);
    int findByUserCount(long userId, LocalDateTime currentTime);

    List<Reservation> findByUserHistory(int page, int amountOnPage, long userId, LocalDateTime currentTime);
    int findByUserHistoryCount(long userId, LocalDateTime currentTime);

    List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId);
    int findByRestaurantCount(long restaurantId);

    List<Reservation> findConfirmedByRestaurant(int page, int amountOnPage, long restaurantId, LocalDateTime currentTime);
    int findConfirmedByRestaurantCount(long restaurantId, LocalDateTime currentTime);

    List<Reservation> findPendingByRestaurant(int page, int amountOnPage, long restaurantId, LocalDateTime currentTime);
    int findPendingByRestaurantCount(long restaurantId, LocalDateTime currentTime);

    List<Reservation> findHistoryByRestaurant(int page, int amountOnPage, long restaurantId, LocalDateTime currentTime);
    int findHistoryByRestaurantCount(long restaurantId, LocalDateTime currentTime);

    Optional<Reservation> findById(long id);


    // DESTROY
    boolean cancelReservation(long id);

}
