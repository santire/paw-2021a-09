package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.ReservationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    // CREATE
    @Override
    public Reservation addReservation(long userId, long restaurantId, Date date, long quantity) {
        User user = userService.findById(userId).orElseThrow(() -> new IllegalStateException("Reservation: User doesnt exist"));

        // User restaurantOwner = restaurantService.findRestaurantOwner(restaurantId).orElseThrow(() -> new IllegalStateException("Reservation: Restaurant doesnt exist"));


        //send email to restaurant
        // emailService.sendReservationEmail(restaurantOwner, user, date, quantity);

        //for now its autoconfirmed
        Reservation reservation = reservationDao.addReservation(user.getId(),restaurantId,date,quantity);
        emailService.sendConfirmationEmail(reservation);

        return reservation;
    }


    // READ
    @Override
    public Optional<Reservation> findById(int id) {
        return reservationDao.findById(id);
    }

    @Override
    public List<Reservation> findByUser(long userId) {
        List<Reservation> reservations = reservationDao.findByUser(userId);
        Optional<Restaurant> restaurant;
        for(Reservation reservation : reservations){
            restaurant = restaurantService.findById(reservation.getRestaurantId());
            if(restaurant.isPresent()){
                reservation.setRestaurant(restaurant.get());
            }
        }
        return reservations;
    }

    @Override
    public List<Reservation> findByUser(int page, int amountOnPage, long userId) {
        List<Reservation> reservations = reservationDao.findByUser(page, amountOnPage, userId);
        reservations.stream().forEach((r) -> {
            Optional<Restaurant> restaurant = restaurantService.findById(r.getRestaurantId());
            if(restaurant.isPresent()) {
                r.setRestaurant(restaurant.get());
            }
        });
        return reservations;
    }

    @Override
    public int findByUserPageCount(int amountOnPage, long userId) {
        return reservationDao.findByUserPageCount(amountOnPage, userId);
    }

    @Override
    public List<Reservation> findByRestaurant(long restaurantId) {
        List<Reservation> reservations =  reservationDao.findByRestaurant(restaurantId);
        Optional<Restaurant> restaurant;
        for(Reservation reservation : reservations){
            restaurant = restaurantService.findById(reservation.getRestaurantId());
            if(restaurant.isPresent()){
                reservation.setRestaurant(restaurant.get());
            }
        }
        return reservations;
    }
	  @Override
	  public List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId) {
        List<Reservation> reservations =  reservationDao.findByRestaurant(page, amountOnPage, restaurantId);
        reservations.stream().forEach(r -> {
            Optional<Restaurant> restaurant = restaurantService.findById(r.getRestaurantId());
            if(restaurant.isPresent()) {
                r.setRestaurant(restaurant.get());
            }
        });
	  	return reservations;
	  }

	  @Override
	  public int findByRestaurantPageCount(int amountOnPage, long restaurantId) {
	  	return reservationDao.findByRestaurantPageCount(amountOnPage, restaurantId);
	  }
    

    // UPDATE
    @Override
    public Optional<Reservation> modifyReservation(int reservationId, Date date, long quantity){
        return reservationDao.modifyReservation(reservationId, date, quantity);
    }

    // DESTROY
    @Override
    public boolean cancelReservation(int id) {
        return reservationDao.cancelReservation(id);
    }

}
