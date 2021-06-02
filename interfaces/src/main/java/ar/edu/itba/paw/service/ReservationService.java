package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationService {

    // CREATE
    public Reservation addReservation(long userId, Restaurant restaurant, LocalDateTime date, long quantity);

    // READ
    public List<Reservation> findByUser(User user);
    public List<Reservation> findByUser(int page, int amountOnPage, User user);
    public int findByUserPageCount(int amountOnPage, User user);

    public List<Reservation> findByRestaurant(Restaurant restaurant);
    public List<Reservation> findByRestaurant(int page, int amountOnPage, Restaurant restaurant);
    public List<Reservation> findConfirmedByRestaurant(int page, int amountOnPage, Restaurant restaurant);
    public List<Reservation> findPendingByRestaurant(int page, int amountOnPage, Restaurant restaurant);
    public int findByRestaurantPageCount(int amountOnPage, Restaurant restaurant);
    public int findConfirmedByRestaurantPageCount(int amountOnPage, Restaurant restaurant);
    public int findPendingByRestaurantPageCount(int amountOnPage, Restaurant restaurant);

    // UPDATE
    public Optional<Reservation> modifyReservation(int reservationId, LocalDateTime date, long quantity);
    public boolean confirmReservation(Reservation reservation);

    //DESTROY
    public boolean ownerCancelReservation(Reservation reservation, String message);
    public boolean userCancelReservation(Reservation reservation);


}
