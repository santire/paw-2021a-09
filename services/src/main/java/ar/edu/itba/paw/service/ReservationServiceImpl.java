package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.ReservationNotFoundException;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.ReservationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import ar.edu.itba.paw.model.Email;


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

    // CREATE
    @Override
    @Transactional
    public Reservation addReservation(long userId, long restaurantId, LocalDateTime date, long quantity, String baseUrl) {
    Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
    User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
    User owner = restaurant.getOwner();
    Reservation reservation = reservationDao.addReservation(user, restaurant,date,quantity);
    Locale locale = LocaleContextHolder.getLocale();
    String url =baseUrl+restaurant.getId()+"/manage/pending";
  
    //owner Email
    String plainText = messageSource.getMessage("mail.newReservation.owner.plain",new Object[]{owner.getFirstName(),user.getName(),restaurant.getName()},locale)+"\n"+url+"\n";
    Email myemail = new Email();
    myemail.setMailTo(owner.getEmail());
    myemail.setMailSubject(messageSource.getMessage("mail.newReservation.owner.subject",null,locale));
    myemail.setButtonMailContent(messageSource.getMessage("mail.newReservation.owner.body",new Object[]{owner.getFirstName(),user.getName(),restaurant.getName()},locale),url,messageSource.getMessage("mail.newReservation.owner.button",null,locale));
    emailService.sendEmail(myemail,plainText);

    //customer Email
    myemail = new Email();
    myemail.setMailTo(user.getEmail());
    myemail.setMailSubject(messageSource.getMessage("mail.newReservation.customer.subject",null,locale));
    myemail.setBasicMailContent("","",messageSource.getMessage("mail.newReservation.customer.body",new Object[]{user.getName(),restaurant.getName()},locale));
    emailService.sendEmail(myemail,plainText);
    
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
    public List<Reservation> findByRestaurant(long restaurantId) {
        List<Reservation> reservations =  reservationDao.findByRestaurant(restaurantId);
        return reservations;
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



    // UPDATE

    @Override
    @Transactional
    public boolean confirmReservation(long reservationId){        
        
        Locale locale = LocaleContextHolder.getLocale();
        Reservation reservation = findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Restaurant restaurant = reservation.getRestaurant();
        User user = reservation.getUser();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    
        String body=messageSource.getMessage("mail.newReservation.body",new Object[]{restaurant.getName(),reservation.getQuantity(),reservation.getDate().format(formatter)},locale);
        Email email = new Email();
        email.setMailTo(user.getEmail());
        email.setMailSubject(messageSource.getMessage("mail.newReservation.subject",null,locale));
        email.setBasicMailContent(messageSource.getMessage("mail.newReservation.title",null,locale),user.getFirstName(),body);
        emailService.sendEmail(email,body);
        reservation.setConfirmed(true);
        return true;
     }

    // DESTROY
    @Override

    @Transactional
    public boolean userCancelReservation(long reservationId) {
        Locale locale = LocaleContextHolder.getLocale();
        Reservation reservation = findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Restaurant restaurant = reservation.getRestaurant();
        User owner = restaurant.getOwner();
        User user = reservation.getUser();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String body = messageSource.getMessage("mail.userCancelReservation.body",new Object[]{owner.getFirstName(),user.getName(),restaurant.getName(),reservation.getDate().format(formatter),reservation.getQuantity()},locale);
        Email email = new Email();
        email.setMailTo(owner.getEmail());
        email.setMailSubject(messageSource.getMessage("mail.userCancelReservation.subject",null,locale));
        email.setBasicMailContent(messageSource.getMessage("mail.userCancelReservation.title",null,locale),"",body);
        emailService.sendEmail(email,body);
                                  
        return reservationDao.cancelReservation(reservationId);
    }

    @Override
    @Transactional
    public boolean ownerCancelReservation(long reservationId, String message) {
        Locale locale = LocaleContextHolder.getLocale();
        Reservation reservation = findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Restaurant restaurant = reservation.getRestaurant();
        User user = reservation.getUser();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String body = messageSource.getMessage("mail.ownerCancelReservation.body",new Object[]{user.getName(),restaurant.getName(),reservation.getDate().format(formatter),reservation.getQuantity()},locale)+" "+messageSource.getMessage("mail.ownerCancelReservation.reason",null,locale)+": "+message;
        Email email = new Email();
        email.setMailTo(user.getEmail());
        email.setMailSubject(messageSource.getMessage("mail.ownerCancelReservation.subject",null,locale));
        email.setBasicMailContent(messageSource.getMessage("mail.ownerCancelReservation.title",null,locale),"",body);
        emailService.sendEmail(email,body);
                                  
        return reservationDao.cancelReservation(reservationId);
    }

 }
