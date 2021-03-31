package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;


@Service
public class EmailServiceImpl implements EmailService{


    @Autowired
    private JavaMailSender emailSender;

    private void sendEmail(Email mail) {
                MimeMessage mimeMessage = emailSender.createMimeMessage();
                        try {
                            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                            mimeMessageHelper.setSubject(mail.getMailSubject());
                            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom(), "Page"));
                            mimeMessageHelper.setTo(mail.getMailTo());
                            mimeMessageHelper.setText(mail.getMailContent());
                            emailSender.send(mimeMessageHelper.getMimeMessage());

                        } catch (MessagingException e) {
                          //  e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                          //  e.printStackTrace();
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


}