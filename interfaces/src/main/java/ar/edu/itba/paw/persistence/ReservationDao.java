package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationDao {

    // CREATE
    public Reservation addReservation(User user, Restaurant restaurant, LocalDateTime date, long quantity);

    // READ
    public List<Reservation> findByUser(int page, int amountOnPage, long userId, LocalDateTime currentTime);
    public int findByUserPageCount(int amountOnPage, long userId, LocalDateTime currentTime);

    public List<Reservation> findByUserHistory(int page, int amountOnPage, long userId, LocalDateTime currentTime);
    public int findByUserHistoryPageCount(int amountOnPage, long userId, LocalDateTime currentTime);

    public List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId);
    public int findByRestaurantPageCount(int amountOnPage, long restaurantId);

    public List<Reservation> findConfirmedByRestaurant(int page, int amountOnPage, long restaurantId, LocalDateTime currentTime);
    public int findConfirmedByRestaurantPageCount(int amountOnPage, long restaurantId, LocalDateTime currentTime);

    public List<Reservation> findPendingByRestaurant(int page, int amountOnPage, long restaurantId, LocalDateTime currentTime);
    public int findPendingByRestaurantPageCount(int amountOnPage, long restaurantId, LocalDateTime currentTime);


    public Optional<Reservation> findById(long id);


    // DESTROY
    public boolean cancelReservation(long id);

}
