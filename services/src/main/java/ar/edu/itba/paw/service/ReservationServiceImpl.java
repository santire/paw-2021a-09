package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.ReservationNotFoundException;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.ReservationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;

    // CREATE
    @Override
    @Transactional
    public Reservation addReservation(long userId, long restaurantId, LocalDateTime date, long quantity, URI baseUri) {
        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        User owner = restaurant.getOwner();
        Reservation reservation = reservationDao.addReservation(user, restaurant, date, quantity);

        final String ownerUrl = UriBuilder.fromUri(baseUri).path("restaurants")
                .path(reservation.getRestaurant().getId().toString())
                .path("reservations")
                .queryParam("tab", "pending")
                .build().toString();

        final String userUrl = UriBuilder.fromUri(baseUri)
                .path("user")
                .path("reservations")
                .build().toString();

        emailService.sendNewReservationOwnerEmail(owner.getEmail(), owner.getFirstName(), user.getName(), restaurant.getName(), ownerUrl);

        emailService.sendNewReservationUserEmail(user.getEmail(), user.getName(), restaurant.getName(), userUrl);

        return reservation;
    }


    // READ
    @Override
    @Transactional
    public Optional<Reservation> findById(long id) {
        return reservationDao.findById(id);
    }

    @Override
    @Transactional
    public List<Reservation> findReservations(int page, int amountOnPage, Long userId, Long restaurantId, ReservationStatus status, ReservationType type) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (type == null) {
            return reservationDao.findFilteredReservations(page, amountOnPage, userId, restaurantId, null, null, status, true);
        }

        switch (type) {
            case HISTORY:
                return reservationDao.findFilteredReservations(page, amountOnPage, userId, restaurantId, null, currentTime, status, true);
            case CURRENT:
                return reservationDao.findFilteredReservations(page, amountOnPage, userId, restaurantId, currentTime, null, status, false);
            default:
                return reservationDao.findFilteredReservations(page, amountOnPage, userId, restaurantId, null, null, status, true);

        }
    }

    @Override
    @Transactional
    public int findReservationsCount(Long userId, Long restaurantId, ReservationStatus status, ReservationType type) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (type == null) {
            return reservationDao.findFilteredReservationsCount(userId, restaurantId, null, null, status);
        }

        switch (type) {
            case HISTORY:
                return reservationDao.findFilteredReservationsCount(userId, restaurantId, null, currentTime, status);
            case CURRENT:
                return reservationDao.findFilteredReservationsCount(userId, restaurantId, currentTime, null, status);
            default:
                return reservationDao.findFilteredReservationsCount(userId, restaurantId, null, null, status);
        }
    }
// UPDATE

    @Override
    @Transactional
    public void confirmReservation(long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Restaurant restaurant = reservation.getRestaurant();
        User user = reservation.getUser();

        // TODO: Check if date has already passed
        // TODO: Check if pending, shouldn't be able to confirm/deny otherwise

        emailService.sendReservationConfirmationEmail(user.getEmail(), restaurant.getName(), reservation.getDate(), reservation.getQuantity());
        reservation.setStatus(ReservationStatus.CONFIRMED);
    }

    @Override
    @Transactional
    public void ownerCancelReservation(long reservationId, String message) {
        Reservation reservation = reservationDao.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Restaurant restaurant = reservation.getRestaurant();
        User user = reservation.getUser();

        emailService.sendOwnerReservationCancelEmail(user.getEmail(), user.getName(), restaurant.getName(), reservation.getDate(), reservation.getQuantity(), message);
        reservation.setStatus(ReservationStatus.DENIED);
    }

    // DESTROY
    @Override
    @Transactional
    public void userCancelReservation(long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Restaurant restaurant = reservation.getRestaurant();
        User owner = restaurant.getOwner();
        User user = reservation.getUser();
        reservationDao.cancelReservation(reservationId);
        emailService.sendUserReservationCancelEmail(user.getEmail(), owner.getEmail(), user.getName(), owner.getName(), restaurant.getName(), reservation.getDate(), reservation.getQuantity());
    }


}
