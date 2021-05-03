package ar.edu.itba.paw.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;


import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Locale;


@EnableAsync
@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);


    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MessageSource messageSource;

    @Async
    @Override
    public void sendEmail(Email mail) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress("gourmetablewebapp@gmail.com", "Gourmetable"));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText("this is plain",mail.getMailContent());
            emailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            // e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // e.printStackTrace();
        }
    }

/*    @Async
    @Override
    public void sendEmail(String to) {
        Email email = new Email();
        email.setMailTo(to);
        email.setMailSubject("Page Subj");
        email.setMailContent("Content\n\nSS\n");
        sendEmail(email);
    }*/

    /*
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
    public void sendConfirmationEmail(Reservation reservation, Locale locale) {
        LOGGER.debug("contenido locale: "+locale.toString());
        locale.getLanguage();
        Email email = new Email();
        User user = userService.findById(reservation.getUserId()).orElseThrow(UserNotFoundException::new);
        Restaurant restaurant = restaurantService.findById(reservation.getRestaurantId())
                .orElseThrow(RestaurantNotFoundException::new);
        email.setMailTo(user.getEmail());
        email.setMailSubject("Reserva confirmada!");
        String test = "<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>Gourmetable</title><style>body {margin:0; padding:0; -webkit-text-size-adjust:none; -ms-text-size-adjust:none;} img{line-height:100%; outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;} a img{border: none;} #backgroundTable {margin:0; padding:0; width:100% !important; } a, a:link{color:#2A5DB0; text-decoration: underline;} table td {border-collapse:collapse;} span {color: inherit; border-bottom: none;} span:hover { background-color: transparent; }</style><style>.scalable-image img{max-width:100% !important;height:auto !important}.button a{transition:background-color .25s, border-color .25s}.button a:hover{background-color:#e1e1e1 !important;border-color:#0976a5 !important}@media only screen and (max-width: 400px){.preheader{font-size:12px !important;text-align:center !important}.header--white{text-align:center}.header--white .header__logo{display:block;margin:0 auto;width:118px !important;height:auto !important}.header--left .header__logo{display:block;width:118px !important;height:auto !important}}@media screen and (-webkit-device-pixel-ratio), screen and (-moz-device-pixel-ratio){.sub-story__image,.sub-story__content{display:block !important}.sub-story__image{float:left !important;width:200px}.sub-story__content{margin-top:30px !important;margin-left:200px !important}}@media only screen and (max-width: 550px){.sub-story__inner{padding-left:30px !important}.sub-story__image,.sub-story__content{margin:0 auto !important;float:none !important;text-align:center}.sub-story .button{padding-left:0 !important}}@media only screen and (max-width: 400px){.featured-story--top table,.featured-story--top td{text-align:left}.featured-story--top__heading td,.sub-story__heading td{font-size:18px !important}.featured-story--bottom:nth-child(2) .featured-story--bottom__inner{padding-top:10px !important}.featured-story--bottom__inner{padding-top:20px !important}.featured-story--bottom__heading td{font-size:28px !important;line-height:32px !important}.featured-story__copy td,.sub-story__copy td{font-size:14px !important;line-height:20px !important}.sub-story table,.sub-story td{text-align:center}.sub-story__hero img{width:100px !important;margin:0 auto}}@media only screen and (max-width: 400px){.footer td{font-size:12px !important;line-height:16px !important}}@media screen and (max-width:600px) {table[class=\"columns\"] {margin: 0 auto !important;float:none !important;padding:10px 0 !important;}td[class=\"left\"] {padding: 0px 0 !important;</style></head><body style=\"background: #e1e1e1;font-family:Arial, Helvetica, sans-serif; font-size:1em;\"><style type=\"text/css\">div.preheader { display: none !important; } </style><table id=\"backgroundTable\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background:#e1e1e1;\"><tr><td class=\"body\" align=\"center\" valign=\"top\" style=\"background:#e1e1e1;\" width=\"100%\"><table cellpadding=\"0\" cellspacing=\"0\"><tr><td width=\"640\"></td></tr><tr><td class=\"main\" width=\"640\" align=\"center\" style=\"padding: 0 10px;\"><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table cellspacing=\"0\" cellpadding=\"0\"><tr><td width=\"640\" align=\"left\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"header header--left\" style=\"padding: 20px 10px;\" align=\"center\"><a href=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/\" ><img class=\"header__logo\" src=\"http://pawserver.it.itba.edu.ar/paw-2021a-09/resources/images/logo.svg\" alt=\"Gourmetable\" style=\"display: block; border: 0;\" width=\"200\" height=\"80\"></a></td></tr></table></td></tr></table></td></tr></table><table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\"><table class=\"featured-story featured-story--top\" cellspacing=\"0\" cellpadding=\"0\"> <tr>  <td style=\"padding-bottom: 20px;\">   <table cellspacing=\"0\" cellpadding=\"0\">    <tr>     <td class=\"featured-story__inner\" style=\"background: #fff;\">      <table cellspacing=\"0\" cellpadding=\"0\">       <tr>       </tr>       <tr>        <td class=\"featured-story__content-inner\" style=\"padding: 32px 30px 45px;\">         <table cellspacing=\"0\" cellpadding=\"0\">          <tr>           <td class=\"featured-story__heading featured-story--top__heading\" style=\"background: #fff;\" width=\"640\" align=\"left\">            <table cellspacing=\"0\" cellpadding=\"0\">             <tr>              <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 22px; color: #464646;\" width=\"400\" align=\"left\">               <a href=\"https://click.e.mozilla.org/?qs=4efe345e8852f56cde6916bcb807227bf64dfe9972687c7afed3251bf31f6e1a8aa807abb470382b057fd0255d31d21c24ed6f000cb22d89\"  style=\"text-decoration: none; color: #464646;\">Enjoy the silence</a>              </td>             </tr>            </table>           </td>          </tr>          <tr>           <td class=\"featured-story__copy\" style=\"background: #fff;\" width=\"640\" align=\"center\">            <table cellspacing=\"0\" cellpadding=\"0\">             <tr>              <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; line-height: 22px; color: #555555; padding-top: 16px;\" align=\"left\">                Websites that autoplay video can be super annoying. You didn't select the video to play &mdash; it was started for you. <i>Hurumph!</i> Firefox will now stop that from happening, putting you in control. If you'd like to hear or see a video, just click on the play button to watch it.<br><br>                Firefox with  the way online video should be.              </td>             </tr>            </table>           </td>          </tr>          <tr>                 <td class=\"button\" style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; padding-top: 26px;\" width=\"640\" align=\"left\">                  <a href=\"https://click.e.mozilla.org/?qs=4efe345e8852f56c999be06eb9393c839773a620030ba42401ec5054f3e0502235fcd9fe9cb5ac8a277444735e0661ebaa26cab1d07aad32\"  style=\"background: #0c99d5; color: #fff; text-decoration: none; border: 14px solid #0c99d5; border-left-width: 50px; border-right-width: 50px; text-transform: uppercase; display: inline-block;\">                   Active your account                  </a>           </td>                </tr>         </table>        </td>       </tr>      </table>     </td>    </tr>   </table>  </td> </tr></table></td></tr></table></td>                    </tr>                    <tr>                     <td class=\"footer\" width=\"640\" align=\"center\" style=\"padding-top: 10px;\">                      <table cellspacing=\"0\" cellpadding=\"0\">                       <tr>                        <td align=\"center\" style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 14px; line-height: 18px; color: #738597; padding: 0 20px 40px;\">                                      <br>      <br></custom></body></html>";
        email.setMailContent(test);
        
        //email.setMailContent("Su reserva para:\n" + restaurant.getName() + " ha sido confirmada. \n"
        //        + "Mesa para " + reservation.getQuantity() + " personas, a las " + reservation.getDate().toString() + "\n");

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

    */
}
