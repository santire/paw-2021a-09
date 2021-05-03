package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Email;
import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.util.Date;
import java.util.Locale;


public interface EmailService {

    //public void sendCancellationEmail(String to, Restaurant restaurant, String message);
    public void sendEmail(Email mail);

    //public void sendReservationEmail(User restaurantOwner, User user, Date date, long quantity);
    //public void sendConfirmationEmail(User restaurantOwner, User user, Date date, long quantity);
    //public void sendConfirmationEmail(Reservation reservation, Locale locale);
    //public void sendRegistrationEmail(String to, String url);
}
