package ar.edu.itba.paw.service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

import ar.edu.itba.paw.model.Email;
import ar.edu.itba.paw.model.EmailTemplate;

public interface EmailService {
    int RESERVATION = 0;
    int REGISTER = 1;
    int PASSWORD_RESET = 2;
    int RESERVATION_CONFIRM = 3;
    int RESERVATION_OWNER_CANCEL = 4;
    int RESERVATION_USER_CANCEL = 5;
    int RESERVATION_PENDING = 6;


    void sendRegistrationEmail(String userEmail, String name, String urlWithToken);
    void sendNewReservationOwnerEmail(String ownerEmail, String ownerName, String userName, String restaurantName, String url);
    void sendPasswordResetEmail(String userEmail, String name, String urlWithToken);
    void sendNewReservationUserEmail(String userEmail, String name, String ownerName, String urlWithToken, String url);
    void sendReservationConfirmationEmail(String userEmail, String restaurantName, LocalDateTime date, long quantity);
    void sendOwnerReservationCancelEmail(String userEmail, String userName, String restaurantName, LocalDateTime date, long quantity, String message);

    void sendUserReservationCancelEmail(String userEmail, String ownerEmail, String userName, String ownerName, String restaurantName, LocalDateTime date, long quantity);
}
