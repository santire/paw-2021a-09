package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Email;
import ar.edu.itba.paw.model.EmailTemplate;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    @Async
    @Override
    public void sendEmail(Email mail, String plainText, Map<String, Object> templateModel, EmailTemplate emailTemplate) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process(emailTemplate.getName()+".html", thymeleafContext);

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress("gourmetablewebapp@gmail.com", "Gourmetable"));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(plainText, htmlBody);
            emailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            // Ignore
        } catch (UnsupportedEncodingException e) {
            // Ignore
        }

    }
}
