package ar.edu.itba.paw.service;

import java.util.Map;

import ar.edu.itba.paw.model.Email;
import ar.edu.itba.paw.model.EmailTemplate;

public interface EmailService {

    public void sendEmail(Email mail, String plainText, Map<String, Object> templateModel, EmailTemplate template);
    // public void sendEmail(Email mail, String plainText);
}
