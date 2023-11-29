package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.ReservationNotFoundException;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.ReservationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantServiceImpl.class);
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private MessageSource messageSource;

    // CREATE
    @Override
    @Transactional
    public Reservation addReservation(long userId, long restaurantId, LocalDateTime date, long quantity, String ownerUrl, String userUrl) {
        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        User owner = restaurant.getOwner();
        Reservation reservation = reservationDao.addReservation(user, restaurant, date, quantity);
//        String url = baseUrl + "/paw-2021a-09/restaurants/" + restaurant.getId() + "/reservations?tab=pending";

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
    public List<Reservation> findByUser(int page, int amountOnPage, long userId) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Reservation> reservations = reservationDao.findByUser(page, amountOnPage, userId, currentTime);
        return reservations;
    }

    @Override
    @Transactional
    public int findByUserCount(long userId) {
        LocalDateTime currentTime = LocalDateTime.now();
        return reservationDao.findByUserCount(userId, currentTime);
    }


    @Override
    @Transactional
    public List<Reservation> findByUserHistory(int page, int amountOnPage, long userId) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Reservation> reservations = reservationDao.findByUserHistory(page, amountOnPage, userId, currentTime);
        return reservations;
    }

    @Override
    @Transactional
    public int findByUserHistoryCount(long userId) {
        LocalDateTime currentTime = LocalDateTime.now();
        return reservationDao.findByUserHistoryCount(userId, currentTime);
    }

    @Override
    @Transactional
    public List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId) {
        List<Reservation> reservations = reservationDao.findByRestaurant(page, amountOnPage, restaurantId);
        return reservations;
    }

    @Override

    public int findByRestaurantCount(long restaurantId) {
        return reservationDao.findByRestaurantCount(restaurantId);
    }

    @Override
    @Transactional
    public List<Reservation> findConfirmedByRestaurant(int page, int amountOnPage, long restaurantId) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Reservation> reservations = reservationDao.findConfirmedByRestaurant(page, amountOnPage, restaurantId, currentTime);
        return reservations;
    }

    @Override
    @Transactional
    public int findConfirmedByRestaurantCount(long restaurantId) {
        LocalDateTime currentTime = LocalDateTime.now();
        return reservationDao.findConfirmedByRestaurantCount(restaurantId, currentTime);
    }

    @Override
    @Transactional
    public List<Reservation> findPendingByRestaurant(int page, int amountOnPage, long restaurantId) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Reservation> reservations = reservationDao.findPendingByRestaurant(page, amountOnPage, restaurantId, currentTime);
        return reservations;
    }

    @Override
    @Transactional
    public int findPendingByRestaurantCount(long restaurantId) {
        LocalDateTime currentTime = LocalDateTime.now();
        return reservationDao.findPendingByRestaurantCount(restaurantId, currentTime);
    }

    @Override
    @Transactional
    public List<Reservation> findHistoryByRestaurant(int page, int amountOnPage, long restaurantId) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Reservation> reservations = reservationDao.findHistoryByRestaurant(page, amountOnPage, restaurantId, currentTime);
        return reservations;
    }

    @Override
    @Transactional
    public int findHistoryByRestaurantCount(long restaurantId) {
        LocalDateTime currentTime = LocalDateTime.now();
        return reservationDao.findHistoryByRestaurantCount(restaurantId, currentTime);
    }

    @Override
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
    public Reservation confirmReservation(long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Restaurant restaurant = reservation.getRestaurant();
        User user = reservation.getUser();

        // TODO: Check if date has already passed
        // TODO: Check if pending, shouldn't be able to confirm/deny otherwise

        emailService.sendReservationConfirmationEmail(user.getEmail(), restaurant.getName(), reservation.getDate(), reservation.getQuantity());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservation;
    }
    @Override
    @Transactional
    public Reservation ownerCancelReservation(long reservationId, String message) {
        Reservation reservation = reservationDao.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Restaurant restaurant = reservation.getRestaurant();
        User user = reservation.getUser();

        emailService.sendOwnerReservationCancelEmail(user.getEmail(), user.getName(), restaurant.getName(), reservation.getDate(), reservation.getQuantity(), message);
        reservation.setStatus(ReservationStatus.DENIED);
        return reservation;
    }

    // DESTROY
    @Override
    @Transactional
    public boolean userCancelReservation(long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Restaurant restaurant = reservation.getRestaurant();
        User owner = restaurant.getOwner();
        User user = reservation.getUser();

        emailService.sendUserReservationCancelEmail(user.getEmail(), owner.getEmail(), user.getName(), owner.getName(), restaurant.getName(), reservation.getDate(), reservation.getQuantity());

        return reservationDao.cancelReservation(reservationId);
    }



}
