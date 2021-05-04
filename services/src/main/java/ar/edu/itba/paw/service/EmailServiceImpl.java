package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.model.Email;
import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@EnableAsync
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @Async
    @Override
    public void sendEmail(Email mail) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress("gourmetablewebapp@gmail.com", "Gourmetable"));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(mail.getMailContent());
            emailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            // e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // e.printStackTrace();
        }
    }

    @Async
    @Override
    public void sendCancellationEmail(String to, Restaurant restaurant, String message) {
        Email email = new Email();
        email.setMailTo(to);
        email.setMailSubject("Your reservation has been cancelled");
        email.setMailContent("Your reservation for " + restaurant.getName() + " has been cancelled. " +
                "The restaurant also sent you this message:\n" + message);
        sendEmail(email);
    }

    @Async
    @Override
    public void sendRejectionEmail(String to, Restaurant restaurant) {
        Email email = new Email();
        email.setMailTo(to);
        email.setMailSubject("Your reservation has been rejected");
        email.setMailContent("Your reservation for " + restaurant.getName() + " has been rejected by the owner. ");
        sendEmail(email);
    }

    @Async
    @Override
    public void sendReservationEmail(User restaurantOwner, User user, Date date, long quantity) {
        Email email = new Email();
        email.setMailTo(restaurantOwner.getEmail());
        email.setMailSubject("New reservation");
        email.setMailContent("New reservation from:\n" + user.getEmail() + "\nfor " + quantity + " persons, at "
                + date.toString() + "\n");
        sendEmail(email);
    }

    @Async
    @Override
    public void sendConfirmationEmail(User restaurantOwner, User user, Date date, long quantity) {
        Email email = new Email();
        email.setMailTo(user.getEmail());
        email.setMailSubject("Reserva confirmada!");
        email.setMailContent("Su reserva para:\n" + restaurantOwner.getEmail() + " ha sido confirmada. \n"
                + "Mesa para " + quantity + " personas, a las " + date.toString() + "\n");
        sendEmail(email);
    }

    @Async
    @Override
    public void sendConfirmationEmail(Reservation reservation) {
        Email email = new Email();
        User user = userService.findById(reservation.getUserId()).orElseThrow(UserNotFoundException::new);
        Restaurant restaurant = restaurantService.findById(reservation.getRestaurantId())
                .orElseThrow(RestaurantNotFoundException::new);
        email.setMailTo(user.getEmail());
        email.setMailSubject("Reserva confirmada!");
        email.setMailContent("Su reserva para:\n" + restaurant.getName() + " ha sido confirmada. \n"
                + "Mesa para " + reservation.getQuantity() + " personas, a las " + reservation.getDate().toString() + "\n");

        sendEmail(email);
    }

    @Async
    @Override
    public void sendRegistrationEmail(String to, String url) {
        Email email = new Email();
        email.setMailTo(to);
        email.setMailSubject("Registration Confirmed");
        email.setMailContent("Your registration at Gourmetable has been confirmed\n"
                + "Click the following link to finish your registration: " + url +
                "\n");
        sendEmail(email);
    }

    @Async
    @Override
    public void sendResetPasswordEmail(String to, String url) {
        Email email = new Email();
        email.setMailTo(to);
        email.setMailSubject("Reset Password");
        email.setMailContent("You have requested a password reset, if this wasn't you ignore this email.\n"
                + "Click the following link to reset password: " + url +
                "\n");
        sendEmail(email);
    }

}
