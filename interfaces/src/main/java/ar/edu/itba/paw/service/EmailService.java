package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.User;

import java.util.Date;

public interface EmailService {

    public void sendEmail(String to);

    public void sendReservationEmail(String to, User user, Date date, long quantity);
    public void sendConfirmationEmail(String from, User user, Date date, long quantity);
    public void sendRegistrationEmail(String to);
}
