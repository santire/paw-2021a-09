package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Email;
import ar.edu.itba.paw.model.EmailTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;
    @Autowired
    private MessageSource messageSource;

    final String EMAIL_ADDRESS = "gourmetablewebapp@gmail.com";
    final String PERSONAL = "Gourmetable";

    @Async
    @Override
    public void sendPasswordResetEmail(String userEmail, String name, String urlWithToken){
        Locale locale = LocaleContextHolder.getLocale();
        Map<String, Object> args = new HashMap<>();
        EmailTemplate emailTemplate = EmailTemplate.BUTTON;
        String plainText = messageSource.getMessage("mail.forgot.plain",new Object[]{name},locale)+"\n"+urlWithToken+"\n";
        args.put("titleMessage", "");
        args.put("bodyMessage",messageSource.getMessage("mail.forgot.body",new Object[]{name},locale));
        args.put("buttonMessage",messageSource.getMessage("mail.forgot.button",null,locale));
        args.put("link", urlWithToken);
        
        Email mail = new Email();
        mail.setMailTo(userEmail);
        mail.setMailSubject(messageSource.getMessage("mail.forgot.subject",null,locale));

        sendEmail(mail, args, plainText, emailTemplate);
    }

    @Async
    @Override
    public void sendRegistrationEmail(String userEmail, String name, String urlWithToken){
        Locale locale = LocaleContextHolder.getLocale();
        Map<String, Object> args = new HashMap<>();
        EmailTemplate emailTemplate = EmailTemplate.BUTTON;
        String plainText = messageSource.getMessage("mail.register.plain",new Object[]{name},locale)+"\n"+urlWithToken+"\n";
        args.put("titleMessage", "");
        args.put("bodyMessage",messageSource.getMessage("mail.register.body",new Object[]{name},locale));
        args.put("buttonMessage",messageSource.getMessage("mail.register.button",null,locale));
        args.put("link", urlWithToken);

        Email mail = new Email();
        mail.setMailTo(userEmail);
        mail.setMailSubject(messageSource.getMessage("mail.register.subject",null,locale));

        sendEmail(mail, args, plainText, emailTemplate);
    }

    @Async
    @Override
    public void sendUserReservationCancelEmail(String userEmail, String ownerEmail, String userName, String ownerName, String restaurantName, LocalDateTime date, long quantity){
        Locale locale = LocaleContextHolder.getLocale();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

        Map<String, Object> args = new HashMap<>();
        EmailTemplate emailTemplate = EmailTemplate.BASIC;

        String body = messageSource.getMessage("mail.userCancelReservation.body",new Object[]{ownerName,userName, restaurantName, date.format(formatter), quantity},locale);
        args.put("titleMessage", messageSource.getMessage("mail.userCancelReservation.title",null,locale));
        args.put("bodyMessage", body);


        Email mail = new Email();
        mail.setMailTo(ownerEmail);
        mail.setMailSubject(messageSource.getMessage("mail.userCancelReservation.subject",null,locale));

        sendEmail(mail, args, body, emailTemplate);
    }

    @Async
    @Override
    public void sendOwnerReservationCancelEmail(String userEmail, String userName, String restaurantName, LocalDateTime date, long quantity, String message){
        Locale locale = LocaleContextHolder.getLocale();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

        Map<String, Object> args = new HashMap<>();
        EmailTemplate emailTemplate = EmailTemplate.BASIC;

        String body = messageSource.getMessage("mail.ownerCancelReservation.body",new Object[]{userName, restaurantName, date.format(formatter), quantity},locale)+" "+messageSource.getMessage("mail.ownerCancelReservation.reason",null,locale)+": "+ message;
        args.put("titleMessage", messageSource.getMessage("mail.ownerCancelReservation.title",null,locale));
        args.put("bodyMessage", body);


        Email mail = new Email();
        mail.setMailTo(userEmail);
        mail.setMailSubject(messageSource.getMessage("mail.ownerCancelReservation.subject",null,locale));

        sendEmail(mail, args, body, emailTemplate);
    }

    @Async
    @Override
    public void sendReservationConfirmationEmail(String userEmail, String restaurantName, LocalDateTime date, long quantity){
        Locale locale = LocaleContextHolder.getLocale();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

        Map<String, Object> args = new HashMap<>();
        EmailTemplate emailTemplate = EmailTemplate.BASIC;

        String body=messageSource.getMessage("mail.newReservation.body",new Object[]{restaurantName, quantity, date.format(formatter)},locale);
        args.put("titleMessage", messageSource.getMessage("mail.newReservation.title",null,locale));
        args.put("bodyMessage", body);

        Email mail = new Email();
        mail.setMailTo(userEmail);
        mail.setMailSubject(messageSource.getMessage("mail.newReservation.subject",null,locale));

        sendEmail(mail, args, body, emailTemplate);
    }

    @Async
    @Override
    public void sendNewReservationUserEmail(String userEmail, String userName, String restaurantName, String url){
        Locale locale = LocaleContextHolder.getLocale();
        Map<String, Object> args = new HashMap<>();
        EmailTemplate emailTemplate = EmailTemplate.BUTTON;
        String plainText = messageSource.getMessage("mail.newReservation.customer.plain",new Object[]{userName,restaurantName},locale)+"\n"+url+"\n";
        args.put("titleMessage", "");
        args.put("bodyMessage", messageSource.getMessage("mail.newReservation.customer.body",new Object[]{userName,restaurantName},locale));
        args.put("buttonMessage", messageSource.getMessage("mail.newReservation.customer.button",null,locale));
        args.put("link", url);
        Email mail = new Email();
        mail.setMailTo(userEmail);
        mail.setMailSubject(messageSource.getMessage("mail.newReservation.customer.subject",null,locale));

        sendEmail(mail, args, plainText, emailTemplate);
    }

    @Async
    @Override
    public void sendNewReservationOwnerEmail(String ownerEmail, String ownerName, String userName, String restaurantName, String url){
        Locale locale = LocaleContextHolder.getLocale();
        Map<String, Object> args = new HashMap<>();
        EmailTemplate emailTemplate = EmailTemplate.BUTTON;
        String plainText = messageSource.getMessage("mail.newReservation.owner.plain",new Object[]{ownerName,userName,restaurantName},locale)+"\n"+url+"\n";
        args.put("titleMessage", "");
        args.put("bodyMessage", messageSource.getMessage("mail.newReservation.owner.body",new Object[]{ownerName,userName,restaurantName},locale));
        args.put("buttonMessage", messageSource.getMessage("mail.newReservation.owner.button",null,locale));
        args.put("link", url);

        Email mail = new Email();
        mail.setMailTo(ownerEmail);
        mail.setMailSubject(messageSource.getMessage("mail.newReservation.owner.subject",null,locale));

        sendEmail(mail, args, plainText, emailTemplate);
    }

    private void sendEmail(Email email, Map<String, Object> args, String plainText, EmailTemplate emailTemplate){
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(args);
        String htmlBody = thymeleafTemplateEngine.process(emailTemplate.getName()+".html", thymeleafContext);

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(email.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress(EMAIL_ADDRESS, PERSONAL));
            mimeMessageHelper.setTo(email.getMailTo());
            mimeMessageHelper.setText(plainText, htmlBody);
            emailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException | UnsupportedEncodingException e) {
            // Ignore
        }
    }

}
