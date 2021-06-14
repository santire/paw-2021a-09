package ar.edu.itba.paw.service;


import ar.edu.itba.paw.model.Email;
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


@EnableAsync
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Async
    public void sendEmail(Email mail, String plainText){
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress("gourmetablewebapp@gmail.com", "Gourmetable"));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(plainText,mail.getMailContent());
            emailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            // e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // e.printStackTrace();
        }

    }
}
