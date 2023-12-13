package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.ReservationStatus;
import ar.edu.itba.paw.model.ReservationType;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationService {

    // CREATE
    Reservation addReservation(long userId, long restaurantId, LocalDateTime date, long quantity, URI baseUri);

    // READ
    List<Reservation> findReservations(int page, int amountOnPage, Long userId, Long restaurantId, ReservationStatus status, ReservationType type);
    int findReservationsCount(Long userId, Long restaurantId, ReservationStatus status, ReservationType type);

    Optional<Reservation> findById(long reservationId);

    // UPDATE
    Reservation confirmReservation(long reservationId);

    //DESTROY
    Reservation ownerCancelReservation(long reservationId, String message);
    void userCancelReservation(long reservationId);


}
