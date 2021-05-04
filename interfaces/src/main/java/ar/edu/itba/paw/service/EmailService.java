package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Email;
import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.util.Date;

public interface EmailService {

    public void sendCancellationEmail(String to, Restaurant restaurant, String message);
    public void sendRejectionEmail(String to, Restaurant restaurant);
    public void sendEmail(Email mail);

    public void sendReservationEmail(User restaurantOwner, User user, Date date, long quantity);
    public void sendConfirmationEmail(User restaurantOwner, User user, Date date, long quantity);
    public void sendConfirmationEmail(Reservation reservation);

    public void sendRegistrationEmail(String to, String url);
    public void sendResetPasswordEmail(String to, String url);
}
