package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.ReservationDao;
import ar.edu.itba.paw.persistence.UserDao;
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

    @Override
    public List<Reservation> findByUser(int userId) {
        return reservationDao.findByUser(userId);
    }

    @Override
    public List<Reservation> findByRestaurant(int restaurantId) {
        return reservationDao.findByRestaurant(restaurantId);
    }

    @Override
    public Optional<Reservation> findById(int id) {
        return reservationDao.findById(id);
    }

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

    @Override
    public boolean cancelReservation(int id) {
        return reservationDao.cancelReservation(id);
    }

    @Override
    public Optional<Reservation> modifyReservation(int reservationId, Date date, long quantity){
        return reservationDao.modifyReservation(reservationId, date, quantity);
    }
}
