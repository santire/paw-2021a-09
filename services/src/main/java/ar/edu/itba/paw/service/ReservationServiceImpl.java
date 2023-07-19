package ar.edu.itba.paw.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.ReservationNotFoundException;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.ReservationDao;

 @Service
public class ReservationServiceImpl implements ReservationService{

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

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantServiceImpl.class);

    // CREATE
    @Override
    @Transactional
    public Reservation addReservation(long userId, long restaurantId, LocalDateTime date, long quantity, String baseUrl) {
    Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
    User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
    User owner = restaurant.getOwner();
    Reservation reservation = reservationDao.addReservation(user, restaurant,date,quantity);
    String url =baseUrl+"/restaurants/"+restaurant.getId()+"/reservations?tab=pending";

    emailService.sendNewReservationOwnerEmail(
            owner.getEmail(),
            owner.getFirstName(),
            user.getName(),
            restaurant.getName(),
            url
    );

    emailService.sendNewReservationUserEmail(
            user.getEmail(),
            user.getName(),
            owner.getName(),
            restaurant.getName(),
            url
    );

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
     public int findByUserPageCount(int amountOnPage, long userId) {
        LocalDateTime currentTime = LocalDateTime.now();
        return reservationDao.findByUserPageCount(amountOnPage, userId, currentTime);
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
     public int findByUserHistoryPageCount(int amountOnPage, long userId) {
         LocalDateTime currentTime = LocalDateTime.now();
         return reservationDao.findByUserHistoryPageCount(amountOnPage, userId, currentTime);
     }

	  @Override
    @Transactional
	  public List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId) {
        List<Reservation> reservations =  reservationDao.findByRestaurant(page, amountOnPage, restaurantId);
	  	return reservations;
	   }

    @Override

    public int findByRestaurantPageCount(int amountOnPage, long restaurantId) {
      return reservationDao.findByRestaurantPageCount(amountOnPage, restaurantId);
    }

    @Override
    @Transactional
    public List<Reservation> findConfirmedByRestaurant(int page, int amountOnPage, long restaurantId) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Reservation> reservations =  reservationDao.findConfirmedByRestaurant(page, amountOnPage, restaurantId, currentTime);
         return reservations;
     }

    @Override
    @Transactional
    public int findConfirmedByRestaurantPageCount(int amountOnPage, long restaurantId) {
        LocalDateTime currentTime = LocalDateTime.now();
        return reservationDao.findConfirmedByRestaurantPageCount(amountOnPage, restaurantId, currentTime);
    }

    @Override
    @Transactional
    public List<Reservation> findPendingByRestaurant(int page, int amountOnPage, long restaurantId) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Reservation> reservations =  reservationDao.findPendingByRestaurant(page, amountOnPage, restaurantId, currentTime);
         return reservations;
     }

    @Override
    @Transactional
     public int findPendingByRestaurantPageCount(int amountOnPage, long restaurantId) {
        LocalDateTime currentTime = LocalDateTime.now();
        return reservationDao.findPendingByRestaurantPageCount(amountOnPage, restaurantId, currentTime);
     }

     @Override
     @Transactional
     public List<Reservation> findHistoryByRestaurant(int page, int amountOnPage, long restaurantId) {
         LocalDateTime currentTime = LocalDateTime.now();
         List<Reservation> reservations =  reservationDao.findHistoryByRestaurant(page, amountOnPage, restaurantId, currentTime);
          return reservations;
      }
 
     @Override
     @Transactional
      public int findHistoryByRestaurantPageCount(int amountOnPage, long restaurantId) {
         LocalDateTime currentTime = LocalDateTime.now();
         return reservationDao.findHistoryByRestaurantPageCount(amountOnPage, restaurantId, currentTime);
      }





    // UPDATE

    @Override
    @Transactional
    public boolean confirmReservation(long reservationId){  
        LOGGER.debug("inicio: confirmRerservation");
        
        Locale locale = LocaleContextHolder.getLocale();
        Reservation reservation = findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Restaurant restaurant = reservation.getRestaurant();
        User user = reservation.getUser();
    
        emailService.sendReservationConfirmationEmail(
                user.getEmail(),
                restaurant.getName(),
                reservation.getDate(),
                reservation.getQuantity()
        );
        reservation.setConfirmed(true);
        return true;
     }

    // DESTROY
    @Override

    @Transactional
    public boolean userCancelReservation(long reservationId) {
        Reservation reservation = findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Restaurant restaurant = reservation.getRestaurant();
        User owner = restaurant.getOwner();
        User user = reservation.getUser();

        emailService.sendUserReservationCancelEmail(
                user.getEmail(),
                owner.getEmail(),
                user.getName(),
                owner.getName(),
                restaurant.getName(),
                reservation.getDate(),
                reservation.getQuantity()
        );
                                  
        return reservationDao.cancelReservation(reservationId);
    }

    @Override
    @Transactional
    public boolean ownerCancelReservation(long reservationId, String message) {
        Reservation reservation = findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Restaurant restaurant = reservation.getRestaurant();
        User user = reservation.getUser();

        emailService.sendOwnerReservationCancelEmail(
                user.getEmail(),
                user.getName(),
                restaurant.getName(),
                reservation.getDate(),
                reservation.getQuantity(),
                message
        );
        return reservationDao.cancelReservation(reservationId);
    }

 }
