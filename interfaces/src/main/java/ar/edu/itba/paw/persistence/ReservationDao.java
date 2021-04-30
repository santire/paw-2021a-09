package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationDao {

    public List<Reservation> findByUser(long userId);
    public List<Reservation> findByRestaurant(long restaurantId);
    public Optional<Reservation> findById(int id);

    public Reservation addReservation(long userId, long restaurantId, Date date, long quantity);
    public boolean cancelReservation(int id);
    public Optional<Reservation> modifyReservation(int reservationId, Date date, long quantity);
}
