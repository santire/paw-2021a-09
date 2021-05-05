package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Email;

public interface EmailService {

    public void sendEmail(Email mail);
    public void sendEmail(Email mail, String plainText);

}
