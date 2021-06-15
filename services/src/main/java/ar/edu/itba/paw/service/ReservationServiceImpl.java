package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
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
import java.util.*;

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
    public Reservation addReservation(long userId, long restaurantId, LocalDateTime date, long quantity) {
    Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
    User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
    User owner = restaurant.getOwner();
    
    Reservation reservation = reservationDao.addReservation(user, restaurant,date,quantity);
        
    Locale locale = LocaleContextHolder.getLocale();
    String url = "http://pawserver.it.itba.edu.ar/paw-2021a-09/restaurant/"+restaurant.getId()+"/manage/pending";
  
    String plainText = messageSource.getMessage("mail.newReservation.owner.plain",new Object[]{owner.getFirstName(),user.getName(),restaurant.getName()},locale)+"\n"+url+"\n";
    String emailContent="<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>Gourmetable</title><style>body {margin:0; padding:0; -webkit-text-size-adjust:none; -ms-text-size-adjust:none;} img{line-height:100%; outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;} a img{border: none;} #backgroundTable {margin:0; padding:0; width:100% !important; } a, a:link{color:#2A5DB0; text-decoration: underline;} table td {border-collapse:collapse;} span {color: inherit; border-bottom: none;} span:hover { background-color: transparent; }</style><style>.scalable-image img{max-width:100% !important;height:auto !important}.button a{transition:background-color .25s, border-color .25s}.button a:hover{background-color:#e1e1e1 !important;border-color:#0976a5 !important}@media only screen and (max-width: 400px){.preheader{font-size:12px !important;text-align:center !important}.header--white{text-align:center}.header--white .header__logo{display:block;margin:0 auto;width:118px !important;height:auto !important}.header--left .header__logo{display:block;width:118px !important;height:auto !important}}@media screen and (-webkit-device-pixel-ratio), screen and (-moz-device-pixel-ratio){.sub-story__image,.sub-story__content{display:block !important}.sub-story__image{float:left !important;width:200px}.sub-story__content{margin-top:30px !important;margin-left:200px !important}}@media only screen and (max-width: 550px){.sub-story__inner{padding-left:30px !important}.sub-story__image,.sub-story__content{margin:0 auto !important;float:none !important;text-align:center}.sub-story .button{padding-left:0 !important}}@media only screen and (max-width: 400px){.featured-story--top table,.featured-story--top td{text-align:left}.featured-story--top__heading td,.sub-story__heading td{font-size:18px !important}.featured-story--bottom:nth-child(2) .featured-story--bottom__inner{padding-top:10px !important}.featured-story--bottom__inner{padding-top:20px !important}.featured-story--bottom__heading td{font-size:28px !important;line-height:32px !important}.featured-story__copy td,.sub-story__copy td{font-size:14px !important;line-height:20px !important}.sub-story table,.sub-story td{text-align:center}.sub-story__hero img{width:100px !important;margin:0 auto}}@media only screen and (max-width: 400px){.footer td{font-size:12px !important;line-height:16px !important}}@media screen and (max-width:600px) {table[class=\"columns\"] {margin: 0 auto !important;float:none !important;padding:10px 0 !important;}td[class=\"left\"] {padding: 0px 0 !important;</style></head><body style=\"background: #e1e1e1;font-family:Arial, Helvetica, sans-serif; font-size:1em;\"><style type=\"text/css\">div.preheader { display: none !important; } </style><table id=\"backgroundTable\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background:#e1e1e1;\"><tr><td class=\"body\" align=\"center\" valign=\"top\" style=\"background:#e1e1e1;\" width=\"100%\"><table cellpadding=\"0\" cellspacing=\"0\"><tr><td width=\"640\"></td></tr><tr><td class=\"main\" width=\"640\" align=\"center\" style=\"padding: 0 10px;\"><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table cellspacing=\"0\" cellpadding=\"0\"><tr><td width=\"640\" align=\"left\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"header header--left\" style=\"padding: 20px 10px;\" align=\"center\"><a href=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/\" ><img class=\"header__logo\" src=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/resources/images/logo.png\" alt=\"Gourmetable\" style=\"display: block; border: 0;\" width=\"128\" height=\"128\"></a></td></tr></table></td></tr></table></td></tr></table><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table class=\"featured-story featured-story--top\" cellspacing=\"0\" cellpadding=\"0\"> <tr>  <td style=\"padding-bottom: 20px;\"> <table cellspacing=\"0\" cellpadding=\"0\">  <tr>   <td class=\"featured-story__inner\" style=\"background: #fff;\">  <table cellspacing=\"0\" cellpadding=\"0\">   <tr>   </tr>   <tr>    <td class=\"featured-story__content-inner\" style=\"padding: 32px 30px 45px;\">   <table cellspacing=\"0\" cellpadding=\"0\">    <tr>     <td class=\"featured-story__heading featured-story--top__heading\" style=\"background: #fff;\" width=\"640\" align=\"left\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 22px; color: #464646;\" width=\"400\" align=\"left\">     <a style=\"text-decoration: none; color: #464646;\">"
    +"</a>      </td>     </tr>    </table>     </td>    </tr>    <tr>     <td class=\"featured-story__copy\" style=\"background: #fff;\" width=\"640\" align=\"center\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; line-height: 22px; color: #555555; padding-top: 16px;\" align=\"left\">"
    +messageSource.getMessage("mail.newReservation.owner.body",new Object[]{owner.getFirstName(),user.getName(),restaurant.getName()},locale)
    +"</td>     </tr>    </table>     </td>    </tr>    "
    +"<td class=\"button\" style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; padding-top: 26px;\" width=\"640\" align=\"left\">    <a href=\""
    +url+"\""
    +"style=\"background: #0c99d5; color: #fff; text-decoration: none; border: 14px solid #0c99d5; border-left-width: 50px; border-right-width: 50px; text-transform: uppercase; display: inline-block;\">"
    +messageSource.getMessage("mail.newReservation.owner.button",null,locale)
     +"<tr></tr>   </table>    </td>   </tr>  </table>   </td>  </tr> </table>  </td> </tr></table></td></tr></table></td>        </tr>        <tr></custom></body></html>"
    ;
    Email myemail = new Email();
    myemail.setMailTo(owner.getEmail());
    myemail.setMailSubject(messageSource.getMessage("mail.newReservation.owner.subject",null,locale));
    myemail.setMailContent(emailContent);
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
    @Deprecated
    public List<Reservation> findByUser(long userId) {
    // List<Reservation> reservations = reservationDao.findByUser(user);
    // Optional<Restaurant> restaurant;
        // for(Reservation reservation : reservations){
            // restaurant = reservation.getRestaurant();
            // if(restaurant.isPresent()){
             // reservation.setRestaurant(restaurant);
             // }
         // }
         // return reservations;
        return Collections.emptyList();
     }

     @Override
     @Transactional
    public List<Reservation> findByUser(int page, int amountOnPage, long userId) {
         LocalDateTime currentTime = LocalDateTime.now();
         List<Reservation> reservations = reservationDao.findByUser(page, amountOnPage, userId, currentTime);
         // reservations.stream().forEach((r) -> {
         //     Restaurant restaurant = r.getRestaurantId();
         //     if(restaurant.isPresent()) {
         //         r.setRestaurant(restaurant);
         //     }
         // });
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
     public List<Reservation> findByUserAndRestaurantHistory(long userId, long restaurantId) {
         LocalDateTime currentTime = LocalDateTime.now();
         List<Reservation> reservations = reservationDao.findByUserAndRestaurantHistory(userId, restaurantId, currentTime);
         return reservations;
     }

     @Override
     @Transactional
     public List<Reservation> findByUserHistory(int page, int amountOnPage, long userId) {
         LocalDateTime currentTime = LocalDateTime.now();
         List<Reservation> reservations = reservationDao.findByUserHistory(page, amountOnPage, userId, currentTime);
         // reservations.stream().forEach((r) -> {
         //     Restaurant restaurant = r.getRestaurantId();
         //     if(restaurant.isPresent()) {
         //         r.setRestaurant(restaurant);
         //     }
         // });
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
        // Optional<Restaurant> restaurant;
        // Optional<User> user;
        // for(Reservation reservation : reservations){
            // restaurant = restaurantService.findById(reservation.getRestaurantId());
            // user = userService.findById(reservation.getId());
            // if(restaurant.isPresent()){
                // reservation.setRestaurant(restaurant.get());
                // }
            // if(user.isPresent()){
                 // reservation.setUser(user.get());
             // }
     // }
        return reservations;
     }
	  @Override
    @Transactional
	  public List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId) {
        List<Reservation> reservations =  reservationDao.findByRestaurant(page, amountOnPage, restaurantId);
        // reservations.stream().forEach(r -> {
             // Optional<Restaurant> restaurant = r.getRestaurant();
             // Optional<User> user = userService.findById(r.getUserId());
             // if(restaurant.isPresent()) {
             // r.setRestaurant(restaurant.get());
            // }
            // if(user.isPresent()){
                 // r.setUser(user.get());
             // }
         // });
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
        // reservations.stream().forEach(r -> {
        // Optional<Restaurant> restaurant = r.getRestaurant();
            // Optional<User> user = userService.findById(r.getUserId());
            // if(restaurant.isPresent()) {
             // r.setRestaurant(restaurant.get());
             // }
            // if(user.isPresent()){
                 // r.setUser(user.get());
             // }
         // });
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
        // reservations.stream().forEach(r -> {
            // Optional<Restaurant> restaurant = r.getRestaurant();
            // Optional<User> user = userService.findById(r.getUserId());
            // if(restaurant.isPresent()) {
                // r.setRestaurant(restaurant.get());
             // }
            // if(user.isPresent()){
                // r.setUser(user.get());
             // }
         // });
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
    @Deprecated
    public Optional<Reservation> modifyReservation(int reservation, LocalDateTime date, long quantity){
        // return reservationDao.addReservation(reservation.getUser(),reservation.getRestaurant(), date, quantity);
        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean confirmReservation(Reservation reservation){        
        
        Restaurant restaurant = reservation.getRestaurant();
        User user = reservation.getUser();
        Locale locale = LocaleContextHolder.getLocale();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String body=messageSource.getMessage("mail.newReservation.body",new Object[]{restaurant.getName(),reservation.getQuantity(),reservation.getDate().format(formatter)},locale);
        String richText="<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>Gourmetable</title><style>body {margin:0; padding:0; -webkit-text-size-adjust:none; -ms-text-size-adjust:none;} img{line-height:100%; outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;} a img{border: none;} #backgroundTable {margin:0; padding:0; width:100% !important; } a, a:link{color:#2A5DB0; text-decoration: underline;} table td {border-collapse:collapse;} span {color: inherit; border-bottom: none;} span:hover { background-color: transparent; }</style><style>.scalable-image img{max-width:100% !important;height:auto !important}.button a{transition:background-color .25s, border-color .25s}.button a:hover{background-color:#e1e1e1 !important;border-color:#0976a5 !important}@media only screen and (max-width: 400px){.preheader{font-size:12px !important;text-align:center !important}.header--white{text-align:center}.header--white .header__logo{display:block;margin:0 auto;width:118px !important;height:auto !important}.header--left .header__logo{display:block;width:118px !important;height:auto !important}}@media screen and (-webkit-device-pixel-ratio), screen and (-moz-device-pixel-ratio){.sub-story__image,.sub-story__content{display:block !important}.sub-story__image{float:left !important;width:200px}.sub-story__content{margin-top:30px !important;margin-left:200px !important}}@media only screen and (max-width: 550px){.sub-story__inner{padding-left:30px !important}.sub-story__image,.sub-story__content{margin:0 auto !important;float:none !important;text-align:center}.sub-story .button{padding-left:0 !important}}@media only screen and (max-width: 400px){.featured-story--top table,.featured-story--top td{text-align:left}.featured-story--top__heading td,.sub-story__heading td{font-size:18px !important}.featured-story--bottom:nth-child(2) .featured-story--bottom__inner{padding-top:10px !important}.featured-story--bottom__inner{padding-top:20px !important}.featured-story--bottom__heading td{font-size:28px !important;line-height:32px !important}.featured-story__copy td,.sub-story__copy td{font-size:14px !important;line-height:20px !important}.sub-story table,.sub-story td{text-align:center}.sub-story__hero img{width:100px !important;margin:0 auto}}@media only screen and (max-width: 400px){.footer td{font-size:12px !important;line-height:16px !important}}@media screen and (max-width:600px) {table[class=\"columns\"] {margin: 0 auto !important;float:none !important;padding:10px 0 !important;}td[class=\"left\"] {padding: 0px 0 !important;</style></head><body style=\"background: #e1e1e1;font-family:Arial, Helvetica, sans-serif; font-size:1em;\"><style type=\"text/css\">div.preheader { display: none !important; } </style><table id=\"backgroundTable\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background:#e1e1e1;\"><tr><td class=\"body\" align=\"center\" valign=\"top\" style=\"background:#e1e1e1;\" width=\"100%\"><table cellpadding=\"0\" cellspacing=\"0\"><tr><td width=\"640\"></td></tr><tr><td class=\"main\" width=\"640\" align=\"center\" style=\"padding: 0 10px;\"><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table cellspacing=\"0\" cellpadding=\"0\"><tr><td width=\"640\" align=\"left\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"header header--left\" style=\"padding: 20px 10px;\" align=\"center\"><a href=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/\" ><img class=\"header__logo\" src=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/resources/images/logo.png\" alt=\"Gourmetable\" style=\"display: block; border: 0;\" width=\"128\" height=\"128\"></a></td></tr></table></td></tr></table></td></tr></table><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table class=\"featured-story featured-story--top\" cellspacing=\"0\" cellpadding=\"0\"> <tr>  <td style=\"padding-bottom: 20px;\"> <table cellspacing=\"0\" cellpadding=\"0\">  <tr>   <td class=\"featured-story__inner\" style=\"background: #fff;\">  <table cellspacing=\"0\" cellpadding=\"0\">   <tr>   </tr>   <tr>    <td class=\"featured-story__content-inner\" style=\"padding: 32px 30px 45px;\">   <table cellspacing=\"0\" cellpadding=\"0\">    <tr>     <td class=\"featured-story__heading featured-story--top__heading\" style=\"background: #fff;\" width=\"640\" align=\"left\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 22px; color: #464646;\" width=\"400\" align=\"left\">     <a style=\"text-decoration: none; color: #464646;\">"
        +messageSource.getMessage("mail.newReservation.title",null,locale)+
        "</a>      </td>     </tr>    </table>     </td>    </tr>    <tr>     <td class=\"featured-story__copy\" style=\"background: #fff;\" width=\"640\" align=\"center\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; line-height: 22px; color: #555555; padding-top: 16px;\" align=\"left\">"
        +user.getFirstName()+",\n"
        +body
        +"</td>     </tr>    </table>     </td>    </tr>    <tr></tr>   </table>    </td>   </tr>  </table>   </td>  </tr> </table>  </td> </tr></table></td></tr></table></td>        </tr>        <tr></custom></body></html>"
        ;
        Email email = new Email();
        email.setMailTo(user.getEmail());
        email.setMailSubject(messageSource.getMessage("mail.newReservation.subject",null,locale));
        email.setMailContent(richText);
        emailService.sendEmail(email,body);
        reservation.setConfirmed(true);
        return true;

        // return reservationDao.confirmReservation(reservation.getId);
     }

    // DESTROY
    @Override
    // public boolean userCancelReservation(int id) {
    // Locale locale = LocaleContextHolder.getLocale();
    // Optional<Reservation> reservation = findById(id);
    // Optional<Restaurant> restaurant = restaurantService.findById(reservation.get().getRestaurantId());
    // Optional<User> owner = userService.findById(restaurant.get().getUserId());
    // Optional<User> user = userService.findById(reservation.get().getUserId());

    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    // String body = messageSource.getMessage("mail.userCancelReservation.body",new Object[]{owner.get().getFirstName(),user.get().getName(),restaurant.get().getName(),reservation.get().getDate().format(formatter),reservation.get().getQuantity()},locale);
    // String richText="<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>Gourmetable</title><style>body {margin:0; padding:0; -webkit-text-size-adjust:none; -ms-text-size-adjust:none;} img{line-height:100%; outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;} a img{border: none;} #backgroundTable {margin:0; padding:0; width:100% !important; } a, a:link{color:#2A5DB0; text-decoration: underline;} table td {border-collapse:collapse;} span {color: inherit; border-bottom: none;} span:hover { background-color: transparent; }</style><style>.scalable-image img{max-width:100% !important;height:auto !important}.button a{transition:background-color .25s, border-color .25s}.button a:hover{background-color:#e1e1e1 !important;border-color:#0976a5 !important}@media only screen and (max-width: 400px){.preheader{font-size:12px !important;text-align:center !important}.header--white{text-align:center}.header--white .header__logo{display:block;margin:0 auto;width:118px !important;height:auto !important}.header--left .header__logo{display:block;width:118px !important;height:auto !important}}@media screen and (-webkit-device-pixel-ratio), screen and (-moz-device-pixel-ratio){.sub-story__image,.sub-story__content{display:block !important}.sub-story__image{float:left !important;width:200px}.sub-story__content{margin-top:30px !important;margin-left:200px !important}}@media only screen and (max-width: 550px){.sub-story__inner{padding-left:30px !important}.sub-story__image,.sub-story__content{margin:0 auto !important;float:none !important;text-align:center}.sub-story .button{padding-left:0 !important}}@media only screen and (max-width: 400px){.featured-story--top table,.featured-story--top td{text-align:left}.featured-story--top__heading td,.sub-story__heading td{font-size:18px !important}.featured-story--bottom:nth-child(2) .featured-story--bottom__inner{padding-top:10px !important}.featured-story--bottom__inner{padding-top:20px !important}.featured-story--bottom__heading td{font-size:28px !important;line-height:32px !important}.featured-story__copy td,.sub-story__copy td{font-size:14px !important;line-height:20px !important}.sub-story table,.sub-story td{text-align:center}.sub-story__hero img{width:100px !important;margin:0 auto}}@media only screen and (max-width: 400px){.footer td{font-size:12px !important;line-height:16px !important}}@media screen and (max-width:600px) {table[class=\"columns\"] {margin: 0 auto !important;float:none !important;padding:10px 0 !important;}td[class=\"left\"] {padding: 0px 0 !important;</style></head><body style=\"background: #e1e1e1;font-family:Arial, Helvetica, sans-serif; font-size:1em;\"><style type=\"text/css\">div.preheader { display: none !important; } </style><table id=\"backgroundTable\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background:#e1e1e1;\"><tr><td class=\"body\" align=\"center\" valign=\"top\" style=\"background:#e1e1e1;\" width=\"100%\"><table cellpadding=\"0\" cellspacing=\"0\"><tr><td width=\"640\"></td></tr><tr><td class=\"main\" width=\"640\" align=\"center\" style=\"padding: 0 10px;\"><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table cellspacing=\"0\" cellpadding=\"0\"><tr><td width=\"640\" align=\"left\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"header header--left\" style=\"padding: 20px 10px;\" align=\"center\"><a href=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/\" ><img class=\"header__logo\" src=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/resources/images/logo.png\" alt=\"Gourmetable\" style=\"display: block; border: 0;\" width=\"128\" height=\"128\"></a></td></tr></table></td></tr></table></td></tr></table><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table class=\"featured-story featured-story--top\" cellspacing=\"0\" cellpadding=\"0\"> <tr>  <td style=\"padding-bottom: 20px;\"> <table cellspacing=\"0\" cellpadding=\"0\">  <tr>   <td class=\"featured-story__inner\" style=\"background: #fff;\">  <table cellspacing=\"0\" cellpadding=\"0\">   <tr>   </tr>   <tr>    <td class=\"featured-story__content-inner\" style=\"padding: 32px 30px 45px;\">   <table cellspacing=\"0\" cellpadding=\"0\">    <tr>     <td class=\"featured-story__heading featured-story--top__heading\" style=\"background: #fff;\" width=\"640\" align=\"left\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 22px; color: #464646;\" width=\"400\" align=\"left\">     <a style=\"text-decoration: none; color: #464646;\">"
    // +messageSource.getMessage("mail.userCancelReservation.title",null,locale)
    // +"</a>      </td>     </tr>    </table>     </td>    </tr>    <tr>     <td class=\"featured-story__copy\" style=\"background: #fff;\" width=\"640\" align=\"center\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; line-height: 22px; color: #555555; padding-top: 16px;\" align=\"left\">"
    // +body
    // +"</td>     </tr>    </table>     </td>    </tr>    <tr></tr>   </table>    </td>   </tr>  </table>   </td>  </tr> </table>  </td> </tr></table></td></tr></table></td>        </tr>        <tr></custom></body></html>"
    // ;
    // Email email = new Email();
    // email.setMailTo(owner.get().getEmail());
    // email.setMailSubject(messageSource.getMessage("mail.userCancelReservation.subject",null,locale));
    // email.setMailContent(richText);
    // emailService.sendEmail(email,body);
                              
    // return reservationDao.cancelReservation(id);

    @Transactional
    public boolean userCancelReservation(long reservationId) {
        Locale locale = LocaleContextHolder.getLocale();
        Reservation reservation = findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found"));
        Restaurant restaurant = reservation.getRestaurant();
        User owner = restaurant.getOwner();
        User user = reservation.getUser();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String body = messageSource.getMessage("mail.userCancelReservation.body",new Object[]{owner.getFirstName(),user.getName(),restaurant.getName(),reservation.getDate().format(formatter),reservation.getQuantity()},locale);
        String richText="<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>Gourmetable</title><style>body {margin:0; padding:0; -webkit-text-size-adjust:none; -ms-text-size-adjust:none;} img{line-height:100%; outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;} a img{border: none;} #backgroundTable {margin:0; padding:0; width:100% !important; } a, a:link{color:#2A5DB0; text-decoration: underline;} table td {border-collapse:collapse;} span {color: inherit; border-bottom: none;} span:hover { background-color: transparent; }</style><style>.scalable-image img{max-width:100% !important;height:auto !important}.button a{transition:background-color .25s, border-color .25s}.button a:hover{background-color:#e1e1e1 !important;border-color:#0976a5 !important}@media only screen and (max-width: 400px){.preheader{font-size:12px !important;text-align:center !important}.header--white{text-align:center}.header--white .header__logo{display:block;margin:0 auto;width:118px !important;height:auto !important}.header--left .header__logo{display:block;width:118px !important;height:auto !important}}@media screen and (-webkit-device-pixel-ratio), screen and (-moz-device-pixel-ratio){.sub-story__image,.sub-story__content{display:block !important}.sub-story__image{float:left !important;width:200px}.sub-story__content{margin-top:30px !important;margin-left:200px !important}}@media only screen and (max-width: 550px){.sub-story__inner{padding-left:30px !important}.sub-story__image,.sub-story__content{margin:0 auto !important;float:none !important;text-align:center}.sub-story .button{padding-left:0 !important}}@media only screen and (max-width: 400px){.featured-story--top table,.featured-story--top td{text-align:left}.featured-story--top__heading td,.sub-story__heading td{font-size:18px !important}.featured-story--bottom:nth-child(2) .featured-story--bottom__inner{padding-top:10px !important}.featured-story--bottom__inner{padding-top:20px !important}.featured-story--bottom__heading td{font-size:28px !important;line-height:32px !important}.featured-story__copy td,.sub-story__copy td{font-size:14px !important;line-height:20px !important}.sub-story table,.sub-story td{text-align:center}.sub-story__hero img{width:100px !important;margin:0 auto}}@media only screen and (max-width: 400px){.footer td{font-size:12px !important;line-height:16px !important}}@media screen and (max-width:600px) {table[class=\"columns\"] {margin: 0 auto !important;float:none !important;padding:10px 0 !important;}td[class=\"left\"] {padding: 0px 0 !important;</style></head><body style=\"background: #e1e1e1;font-family:Arial, Helvetica, sans-serif; font-size:1em;\"><style type=\"text/css\">div.preheader { display: none !important; } </style><table id=\"backgroundTable\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background:#e1e1e1;\"><tr><td class=\"body\" align=\"center\" valign=\"top\" style=\"background:#e1e1e1;\" width=\"100%\"><table cellpadding=\"0\" cellspacing=\"0\"><tr><td width=\"640\"></td></tr><tr><td class=\"main\" width=\"640\" align=\"center\" style=\"padding: 0 10px;\"><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table cellspacing=\"0\" cellpadding=\"0\"><tr><td width=\"640\" align=\"left\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"header header--left\" style=\"padding: 20px 10px;\" align=\"center\"><a href=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/\" ><img class=\"header__logo\" src=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/resources/images/logo.png\" alt=\"Gourmetable\" style=\"display: block; border: 0;\" width=\"128\" height=\"128\"></a></td></tr></table></td></tr></table></td></tr></table><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table class=\"featured-story featured-story--top\" cellspacing=\"0\" cellpadding=\"0\"> <tr>  <td style=\"padding-bottom: 20px;\"> <table cellspacing=\"0\" cellpadding=\"0\">  <tr>   <td class=\"featured-story__inner\" style=\"background: #fff;\">  <table cellspacing=\"0\" cellpadding=\"0\">   <tr>   </tr>   <tr>    <td class=\"featured-story__content-inner\" style=\"padding: 32px 30px 45px;\">   <table cellspacing=\"0\" cellpadding=\"0\">    <tr>     <td class=\"featured-story__heading featured-story--top__heading\" style=\"background: #fff;\" width=\"640\" align=\"left\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 22px; color: #464646;\" width=\"400\" align=\"left\">     <a style=\"text-decoration: none; color: #464646;\">"
        +messageSource.getMessage("mail.userCancelReservation.title",null,locale)
        +"</a>      </td>     </tr>    </table>     </td>    </tr>    <tr>     <td class=\"featured-story__copy\" style=\"background: #fff;\" width=\"640\" align=\"center\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; line-height: 22px; color: #555555; padding-top: 16px;\" align=\"left\">"
        +body
        +"</td>     </tr>    </table>     </td>    </tr>    <tr></tr>   </table>    </td>   </tr>  </table>   </td>  </tr> </table>  </td> </tr></table></td></tr></table></td>        </tr>        <tr></custom></body></html>"
        ;
        Email email = new Email();
        email.setMailTo(owner.getEmail());
        email.setMailSubject(messageSource.getMessage("mail.userCancelReservation.subject",null,locale));
        email.setMailContent(richText);
        emailService.sendEmail(email,body);
                                  
        return reservationDao.cancelReservation(reservationId);
    }

    @Override
    @Transactional
    public boolean ownerCancelReservation(long reservationId, String message) {
        Locale locale = LocaleContextHolder.getLocale();
        Reservation reservation = findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found"));
        Restaurant restaurant = reservation.getRestaurant();
        User user = reservation.getUser();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String body = messageSource.getMessage("mail.ownerCancelReservation.body",new Object[]{user.getName(),restaurant.getName(),reservation.getDate().format(formatter),reservation.getQuantity()},locale)+" "+messageSource.getMessage("mail.ownerCancelReservation.reason",null,locale)+": "+message;
        String richText="<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>Gourmetable</title><style>body {margin:0; padding:0; -webkit-text-size-adjust:none; -ms-text-size-adjust:none;} img{line-height:100%; outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;} a img{border: none;} #backgroundTable {margin:0; padding:0; width:100% !important; } a, a:link{color:#2A5DB0; text-decoration: underline;} table td {border-collapse:collapse;} span {color: inherit; border-bottom: none;} span:hover { background-color: transparent; }</style><style>.scalable-image img{max-width:100% !important;height:auto !important}.button a{transition:background-color .25s, border-color .25s}.button a:hover{background-color:#e1e1e1 !important;border-color:#0976a5 !important}@media only screen and (max-width: 400px){.preheader{font-size:12px !important;text-align:center !important}.header--white{text-align:center}.header--white .header__logo{display:block;margin:0 auto;width:118px !important;height:auto !important}.header--left .header__logo{display:block;width:118px !important;height:auto !important}}@media screen and (-webkit-device-pixel-ratio), screen and (-moz-device-pixel-ratio){.sub-story__image,.sub-story__content{display:block !important}.sub-story__image{float:left !important;width:200px}.sub-story__content{margin-top:30px !important;margin-left:200px !important}}@media only screen and (max-width: 550px){.sub-story__inner{padding-left:30px !important}.sub-story__image,.sub-story__content{margin:0 auto !important;float:none !important;text-align:center}.sub-story .button{padding-left:0 !important}}@media only screen and (max-width: 400px){.featured-story--top table,.featured-story--top td{text-align:left}.featured-story--top__heading td,.sub-story__heading td{font-size:18px !important}.featured-story--bottom:nth-child(2) .featured-story--bottom__inner{padding-top:10px !important}.featured-story--bottom__inner{padding-top:20px !important}.featured-story--bottom__heading td{font-size:28px !important;line-height:32px !important}.featured-story__copy td,.sub-story__copy td{font-size:14px !important;line-height:20px !important}.sub-story table,.sub-story td{text-align:center}.sub-story__hero img{width:100px !important;margin:0 auto}}@media only screen and (max-width: 400px){.footer td{font-size:12px !important;line-height:16px !important}}@media screen and (max-width:600px) {table[class=\"columns\"] {margin: 0 auto !important;float:none !important;padding:10px 0 !important;}td[class=\"left\"] {padding: 0px 0 !important;</style></head><body style=\"background: #e1e1e1;font-family:Arial, Helvetica, sans-serif; font-size:1em;\"><style type=\"text/css\">div.preheader { display: none !important; } </style><table id=\"backgroundTable\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background:#e1e1e1;\"><tr><td class=\"body\" align=\"center\" valign=\"top\" style=\"background:#e1e1e1;\" width=\"100%\"><table cellpadding=\"0\" cellspacing=\"0\"><tr><td width=\"640\"></td></tr><tr><td class=\"main\" width=\"640\" align=\"center\" style=\"padding: 0 10px;\"><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table cellspacing=\"0\" cellpadding=\"0\"><tr><td width=\"640\" align=\"left\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"header header--left\" style=\"padding: 20px 10px;\" align=\"center\"><a href=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/\" ><img class=\"header__logo\" src=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/resources/images/logo.png\" alt=\"Gourmetable\" style=\"display: block; border: 0;\" width=\"128\" height=\"128\"></a></td></tr></table></td></tr></table></td></tr></table><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table class=\"featured-story featured-story--top\" cellspacing=\"0\" cellpadding=\"0\"> <tr>  <td style=\"padding-bottom: 20px;\"> <table cellspacing=\"0\" cellpadding=\"0\">  <tr>   <td class=\"featured-story__inner\" style=\"background: #fff;\">  <table cellspacing=\"0\" cellpadding=\"0\">   <tr>   </tr>   <tr>    <td class=\"featured-story__content-inner\" style=\"padding: 32px 30px 45px;\">   <table cellspacing=\"0\" cellpadding=\"0\">    <tr>     <td class=\"featured-story__heading featured-story--top__heading\" style=\"background: #fff;\" width=\"640\" align=\"left\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 22px; color: #464646;\" width=\"400\" align=\"left\">     <a style=\"text-decoration: none; color: #464646;\">"
        +messageSource.getMessage("mail.ownerCancelReservation.title",null,locale)
        +"</a>      </td>     </tr>    </table>     </td>    </tr>    <tr>     <td class=\"featured-story__copy\" style=\"background: #fff;\" width=\"640\" align=\"center\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; line-height: 22px; color: #555555; padding-top: 16px;\" align=\"left\">"
        +body
        +"</td>     </tr>    </table>     </td>    </tr>    <tr></tr>   </table>    </td>   </tr>  </table>   </td>  </tr> </table>  </td> </tr></table></td></tr></table></td>        </tr>        <tr></custom></body></html>"
        ;
        Email email = new Email();
        email.setMailTo(user.getEmail());
        email.setMailSubject(messageSource.getMessage("mail.ownerCancelReservation.subject",null,locale));
        email.setMailContent(richText);
        emailService.sendEmail(email,body);
                                  
        return reservationDao.cancelReservation(reservationId);
    }




 }
