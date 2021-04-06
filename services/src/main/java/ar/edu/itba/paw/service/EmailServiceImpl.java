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
    public void sendEmail(String to) {
        Email email = new Email();
        email.setMailTo(to);
        email.setMailSubject("Page Subj");
        email.setMailContent("Content\n\nSS\n");
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
        email.setMailSubject("confirmed reservation!");
        email.setMailContent("your reservation for:\n" + restaurantOwner.getEmail() + " has been confirmed \n"
                + quantity + " persons, at " + date.toString() + "\n");
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
        email.setMailSubject("confirmed reservation!");
        email.setMailContent("your reservation for:\n" + restaurant.getName() + " has been confirmed \n"
                + reservation.getQuantity() + " persons, at " + reservation.getDate().toString() + "\n");

        sendEmail(email);
    }

    @Async
    @Override
    public void sendRegistrationEmail(String to) {
        Email email = new Email();
        email.setMailTo(to);
        email.setMailSubject("Registration Confirmed");
        email.setMailContent("Your registration at Gourmetable has been confirmed\n\n");
        sendEmail(email);
    }

}
