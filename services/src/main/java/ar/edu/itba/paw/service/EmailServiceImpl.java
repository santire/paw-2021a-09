package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Email;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;


@Service
public class EmailServiceImpl implements EmailService{


    @Autowired
    private JavaMailSender emailSender;

    private void sendEmail(Email mail) {
                MimeMessage mimeMessage = emailSender.createMimeMessage();
                        try {
                            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                            mimeMessageHelper.setSubject(mail.getMailSubject());
                            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom(), "Gourmetable"));
                            mimeMessageHelper.setTo(mail.getMailTo());
                            mimeMessageHelper.setText(mail.getMailContent());
                            emailSender.send(mimeMessageHelper.getMimeMessage());

                        } catch (MessagingException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
            }

    @Override
    public void sendEmail(String to){
        Email email = new Email();
                email.setMailTo(to);
                email.setMailSubject("Page Subj");
                email.setMailContent("Content\n\nSS\n");
                sendEmail(email);
    }

    @Override
    public void sendReservationEmail(String to, User user, Date date, long quantity){
        Email email = new Email();
        email.setMailTo(to);
        email.setMailSubject("New reservation");
        email.setMailContent("New reservation from:\n" + user.getEmail() + "\nfor "+ quantity + " persons, at " + date.toString() +"\n");
        sendEmail(email);
    }

    @Override
    public void sendConfirmationEmail(String from, User user, Date date, long quantity) {
        Email email = new Email();
        email.setMailTo(user.getEmail());
        email.setMailSubject("reserve confirmed!");
        email.setMailContent("your reservation for:\n" + from + " has been confirmed \n"+ quantity + " persons, at " + date.toString() +"\n");
        sendEmail(email);
    }

    @Override
    public void sendRegistrationEmail(String to){
        Email email = new Email();
        email.setMailTo(to);
        email.setMailSubject("Registration Confirmed");
        email.setMailContent("Your registration at Gourmetable has been confirmed\n\n");
        sendEmail(email);
    }







}
