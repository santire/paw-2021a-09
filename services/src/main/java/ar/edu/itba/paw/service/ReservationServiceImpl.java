package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.ReservationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.MessageSource;



import java.util.Date;
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
    public Reservation addReservation(long userId, long restaurantId, Date date, long quantity) {
        User user = userService.findById(userId).orElseThrow(() -> new IllegalStateException("Reservation: User doesnt exist"));

        // User restaurantOwner = restaurantService.findRestaurantOwner(restaurantId).orElseThrow(() -> new IllegalStateException("Reservation: Restaurant doesnt exist"));

        //send email to restaurant
        // emailService.sendReservationEmail(restaurantOwner, user, date, quantity);

        //for now its autoconfirmed
        Locale locale = LocaleContextHolder.getLocale();
        Reservation reservation = reservationDao.addReservation(user.getId(),restaurantId,date,quantity);
        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(() -> new IllegalStateException("Reservation: Restaurant doesnt exist"));
        String emailContent="<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>Gourmetable</title><style>body {margin:0; padding:0; -webkit-text-size-adjust:none; -ms-text-size-adjust:none;} img{line-height:100%; outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;} a img{border: none;} #backgroundTable {margin:0; padding:0; width:100% !important; } a, a:link{color:#2A5DB0; text-decoration: underline;} table td {border-collapse:collapse;} span {color: inherit; border-bottom: none;} span:hover { background-color: transparent; }</style><style>.scalable-image img{max-width:100% !important;height:auto !important}.button a{transition:background-color .25s, border-color .25s}.button a:hover{background-color:#e1e1e1 !important;border-color:#0976a5 !important}@media only screen and (max-width: 400px){.preheader{font-size:12px !important;text-align:center !important}.header--white{text-align:center}.header--white .header__logo{display:block;margin:0 auto;width:118px !important;height:auto !important}.header--left .header__logo{display:block;width:118px !important;height:auto !important}}@media screen and (-webkit-device-pixel-ratio), screen and (-moz-device-pixel-ratio){.sub-story__image,.sub-story__content{display:block !important}.sub-story__image{float:left !important;width:200px}.sub-story__content{margin-top:30px !important;margin-left:200px !important}}@media only screen and (max-width: 550px){.sub-story__inner{padding-left:30px !important}.sub-story__image,.sub-story__content{margin:0 auto !important;float:none !important;text-align:center}.sub-story .button{padding-left:0 !important}}@media only screen and (max-width: 400px){.featured-story--top table,.featured-story--top td{text-align:left}.featured-story--top__heading td,.sub-story__heading td{font-size:18px !important}.featured-story--bottom:nth-child(2) .featured-story--bottom__inner{padding-top:10px !important}.featured-story--bottom__inner{padding-top:20px !important}.featured-story--bottom__heading td{font-size:28px !important;line-height:32px !important}.featured-story__copy td,.sub-story__copy td{font-size:14px !important;line-height:20px !important}.sub-story table,.sub-story td{text-align:center}.sub-story__hero img{width:100px !important;margin:0 auto}}@media only screen and (max-width: 400px){.footer td{font-size:12px !important;line-height:16px !important}}@media screen and (max-width:600px) {table[class=\"columns\"] {margin: 0 auto !important;float:none !important;padding:10px 0 !important;}td[class=\"left\"] {padding: 0px 0 !important;</style></head><body style=\"background: #e1e1e1;font-family:Arial, Helvetica, sans-serif; font-size:1em;\"><style type=\"text/css\">div.preheader { display: none !important; } </style><table id=\"backgroundTable\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background:#e1e1e1;\"><tr><td class=\"body\" align=\"center\" valign=\"top\" style=\"background:#e1e1e1;\" width=\"100%\"><table cellpadding=\"0\" cellspacing=\"0\"><tr><td width=\"640\"></td></tr><tr><td class=\"main\" width=\"640\" align=\"center\" style=\"padding: 0 10px;\"><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table cellspacing=\"0\" cellpadding=\"0\"><tr><td width=\"640\" align=\"left\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"header header--left\" style=\"padding: 20px 10px;\" align=\"center\"><a href=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/\" ><img class=\"header__logo\" src=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/resources/images/logo.png\" alt=\"Gourmetable\" style=\"display: block; border: 0;\" width=\"200\" height=\"80\"></a></td></tr></table></td></tr></table></td></tr></table><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table class=\"featured-story featured-story--top\" cellspacing=\"0\" cellpadding=\"0\"> <tr>  <td style=\"padding-bottom: 20px;\"> <table cellspacing=\"0\" cellpadding=\"0\">  <tr>   <td class=\"featured-story__inner\" style=\"background: #fff;\">  <table cellspacing=\"0\" cellpadding=\"0\">   <tr>   </tr>   <tr>    <td class=\"featured-story__content-inner\" style=\"padding: 32px 30px 45px;\">   <table cellspacing=\"0\" cellpadding=\"0\">    <tr>     <td class=\"featured-story__heading featured-story--top__heading\" style=\"background: #fff;\" width=\"640\" align=\"left\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 22px; color: #464646;\" width=\"400\" align=\"left\">     <a style=\"text-decoration: none; color: #464646;\">"
        +messageSource.getMessage("mail.newReservation.title",null,locale)+
        "</a>      </td>     </tr>    </table>     </td>    </tr>    <tr>     <td class=\"featured-story__copy\" style=\"background: #fff;\" width=\"640\" align=\"center\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; line-height: 22px; color: #555555; padding-top: 16px;\" align=\"left\">"
        +user.getFirstName()+",\n"
        +messageSource.getMessage("mail.newReservation.body",new Object[]{restaurant.getName(),quantity,date},locale)
        +"</td>     </tr>    </table>     </td>    </tr>    <tr></tr>   </table>    </td>   </tr>  </table>   </td>  </tr> </table>  </td> </tr></table></td></tr></table></td>        </tr>        <tr></custom></body></html>"
        ;
        //emailService.sendConfirmationEmail(reservation,LocaleContextHolder.getLocale());
        Email email = new Email();
        email.setMailTo(user.getEmail());
        email.setMailSubject(messageSource.getMessage("mail.newReservation.subject",null,locale));
        email.setMailContent(emailContent);
        emailService.sendEmail(email);
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
    Optional<Reservation> reservation = findById(id);
    Optional<Restaurant> restaurant = restaurantService.findById(reservation.get().getRestaurantId());
    Optional<User> user = userService.findById(reservation.get().getUserId());

    String emailContent="<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>Gourmetable</title><style>body {margin:0; padding:0; -webkit-text-size-adjust:none; -ms-text-size-adjust:none;} img{line-height:100%; outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;} a img{border: none;} #backgroundTable {margin:0; padding:0; width:100% !important; } a, a:link{color:#2A5DB0; text-decoration: underline;} table td {border-collapse:collapse;} span {color: inherit; border-bottom: none;} span:hover { background-color: transparent; }</style><style>.scalable-image img{max-width:100% !important;height:auto !important}.button a{transition:background-color .25s, border-color .25s}.button a:hover{background-color:#e1e1e1 !important;border-color:#0976a5 !important}@media only screen and (max-width: 400px){.preheader{font-size:12px !important;text-align:center !important}.header--white{text-align:center}.header--white .header__logo{display:block;margin:0 auto;width:118px !important;height:auto !important}.header--left .header__logo{display:block;width:118px !important;height:auto !important}}@media screen and (-webkit-device-pixel-ratio), screen and (-moz-device-pixel-ratio){.sub-story__image,.sub-story__content{display:block !important}.sub-story__image{float:left !important;width:200px}.sub-story__content{margin-top:30px !important;margin-left:200px !important}}@media only screen and (max-width: 550px){.sub-story__inner{padding-left:30px !important}.sub-story__image,.sub-story__content{margin:0 auto !important;float:none !important;text-align:center}.sub-story .button{padding-left:0 !important}}@media only screen and (max-width: 400px){.featured-story--top table,.featured-story--top td{text-align:left}.featured-story--top__heading td,.sub-story__heading td{font-size:18px !important}.featured-story--bottom:nth-child(2) .featured-story--bottom__inner{padding-top:10px !important}.featured-story--bottom__inner{padding-top:20px !important}.featured-story--bottom__heading td{font-size:28px !important;line-height:32px !important}.featured-story__copy td,.sub-story__copy td{font-size:14px !important;line-height:20px !important}.sub-story table,.sub-story td{text-align:center}.sub-story__hero img{width:100px !important;margin:0 auto}}@media only screen and (max-width: 400px){.footer td{font-size:12px !important;line-height:16px !important}}@media screen and (max-width:600px) {table[class=\"columns\"] {margin: 0 auto !important;float:none !important;padding:10px 0 !important;}td[class=\"left\"] {padding: 0px 0 !important;</style></head><body style=\"background: #e1e1e1;font-family:Arial, Helvetica, sans-serif; font-size:1em;\"><style type=\"text/css\">div.preheader { display: none !important; } </style><table id=\"backgroundTable\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background:#e1e1e1;\"><tr><td class=\"body\" align=\"center\" valign=\"top\" style=\"background:#e1e1e1;\" width=\"100%\"><table cellpadding=\"0\" cellspacing=\"0\"><tr><td width=\"640\"></td></tr><tr><td class=\"main\" width=\"640\" align=\"center\" style=\"padding: 0 10px;\"><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table cellspacing=\"0\" cellpadding=\"0\"><tr><td width=\"640\" align=\"left\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"header header--left\" style=\"padding: 20px 10px;\" align=\"center\"><a href=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/\" ><img class=\"header__logo\" src=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/resources/images/logo.png\" alt=\"Gourmetable\" style=\"display: block; border: 0;\" width=\"200\" height=\"80\"></a></td></tr></table></td></tr></table></td></tr></table><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table class=\"featured-story featured-story--top\" cellspacing=\"0\" cellpadding=\"0\"> <tr>  <td style=\"padding-bottom: 20px;\"> <table cellspacing=\"0\" cellpadding=\"0\">  <tr>   <td class=\"featured-story__inner\" style=\"background: #fff;\">  <table cellspacing=\"0\" cellpadding=\"0\">   <tr>   </tr>   <tr>    <td class=\"featured-story__content-inner\" style=\"padding: 32px 30px 45px;\">   <table cellspacing=\"0\" cellpadding=\"0\">    <tr>     <td class=\"featured-story__heading featured-story--top__heading\" style=\"background: #fff;\" width=\"640\" align=\"left\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 22px; color: #464646;\" width=\"400\" align=\"left\">     <a style=\"text-decoration: none; color: #464646;\">"
    +"Cancelacion Exitosa"+
    "</a>      </td>     </tr>    </table>     </td>    </tr>    <tr>     <td class=\"featured-story__copy\" style=\"background: #fff;\" width=\"640\" align=\"center\">    <table cellspacing=\"0\" cellpadding=\"0\">     <tr>      <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; line-height: 22px; color: #555555; padding-top: 16px;\" align=\"left\">"
    +"Su reserva para " +restaurant.get().getName()+" se ha cancelado con exito"
    +"</td>     </tr>    </table>     </td>    </tr>    <tr></tr>   </table>    </td>   </tr>  </table>   </td>  </tr> </table>  </td> </tr></table></td></tr></table></td>        </tr>        <tr></custom></body></html>"
    ;
    Email email = new Email();
    email.setMailTo(user.get().getEmail());
    email.setMailSubject("Reserva cancelada");
    email.setMailContent(emailContent);
    emailService.sendEmail(email);
                              
    return reservationDao.cancelReservation(id);

    }

}
