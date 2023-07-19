package ar.edu.itba.paw.service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

import ar.edu.itba.paw.model.Email;
import ar.edu.itba.paw.model.EmailTemplate;

public interface EmailService {
    static final int RESERVATION = 0;
    static final int REGISTER = 1;
    static final int PASSWORD_RESET = 2;
    static final int RESERVATION_CONFIRM = 3;
    static final int RESERVATION_OWNER_CANCEL = 4;
    static final int RESERVATION_USER_CANCEL = 5;
    static final int RESERVATION_PENDING = 6;

    public EmailTemplate emailTemplateModel(int type);
    public void sendEmail(Email email, Map<String, Object> args, String plainText, EmailTemplate emailTemplate);
    public void sendRegistrationEmail(String userEmail, String name, String urlWithToken);
    public void sendNewReservationOwnerEmail(String ownerEmail, String ownerName, String userName, String restaurantName, String url);    public void sendPasswordResetEmail(String userEmail, String name, String urlWithToken);
    public void sendNewReservationUserEmail(String userEmail, String name, String ownerName, String urlWithToken, String url);
    public void sendReservationConfirmationEmail(String userEmail, String restaurantName, LocalDateTime date, long quantity);
    public void sendOwnerReservationCancelEmail(String userEmail, String userName, String restaurantName, LocalDateTime date, long quantity, String message);

    public void sendUserReservationCancelEmail(String userEmail, String ownerEmail, String userName, String ownerName, String restaurantName, LocalDateTime date, long quantity);
}
