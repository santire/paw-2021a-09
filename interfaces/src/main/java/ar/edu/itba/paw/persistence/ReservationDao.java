package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.ReservationStatus;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationDao {

    // CREATE
    Reservation addReservation(User user, Restaurant restaurant, LocalDateTime date, long quantity);

    // READ
    List<Reservation> findFilteredReservations(int page, int amountOnPage, Long userId, Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate, ReservationStatus status, boolean desc);

    int findFilteredReservationsCount(Long userId, Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate, ReservationStatus status);

    Optional<Reservation> findById(long id);


    // DESTROY
    void cancelReservation(long id);

}
