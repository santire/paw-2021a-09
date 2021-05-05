package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Email;
import ar.edu.itba.paw.service.EmailService;
import ar.edu.itba.paw.service.ReservationService;
import ar.edu.itba.paw.service.RestaurantService;
import ar.edu.itba.paw.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Locale;

@Controller
public class ReservationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);
    private static final int AMOUNT_OF_RESERVATIONS = 2;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageSource messageSource;


    @RequestMapping("/reservations")
    public ModelAndView userReservations(
            @ModelAttribute("loggedUser") final User loggedUser,
            @RequestParam(defaultValue="1") Integer page){

        final ModelAndView mav = new ModelAndView("myReservations");

        // Shouldn't get here unless logged in, but just in case
        if(loggedUser != null){
            long userId = loggedUser.getId();
            int maxPages = reservationService.findByUserPageCount(AMOUNT_OF_RESERVATIONS, userId);
            if(page == null || page <1) {
                page=1;
            }else if (page > maxPages) {
                page = maxPages;
            }
            mav.addObject("maxPages", maxPages);

            List<Reservation> reservations = reservationService.findByUser(page, AMOUNT_OF_RESERVATIONS, userId);
            mav.addObject("userHasReservations", !reservations.isEmpty());
            mav.addObject("reservations", reservations);
            /*mav.addObject("isOwner", false);*/
            return mav;
        }
        else return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = "/reservations/{reservationId}/cancel", method = RequestMethod.POST)
    public ModelAndView cancelReservation(@ModelAttribute("loggedUser") final User loggedUser,
                                          @PathVariable("reservationId") final int reservationId){
        if(loggedUser != null){
            reservationService.userCancelReservation(reservationId);
            return new ModelAndView("redirect:/reservations");
        }
        else return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = "/reservations/{reservationId}/modify", method = RequestMethod.POST)
    public ModelAndView modifyReservation(@ModelAttribute("loggedUser") final User loggedUser,
                                          @PathVariable("reservationId") final int reservationId,
                                          @RequestParam("quantity") final int quantity){
        SimpleDateFormat dateformat2 = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        String date = "2021-05-20 11:35:42";
        if(loggedUser != null){
            try{
                Date newDate = dateformat2.parse(date);
                reservationService.modifyReservation(reservationId, newDate, quantity);
                return new ModelAndView("redirect:/reservations");
            } catch (ParseException e){
                e.printStackTrace();
            }
        }
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = "/reservations/{restaurantId}/{reservationId}/cancel", method = RequestMethod.POST)
    public ModelAndView cancelReservationFromOwner(@ModelAttribute("loggedUser") final User loggedUser,
                                                   @PathVariable("restaurantId") final long restaurantId,
                                                   @PathVariable("reservationId") final int reservationId,
                                                   @RequestParam("cancellationMessage") final String cancellationMessage){
        if(loggedUser != null){
            Optional<Reservation> reservation = reservationService.findById(reservationId);
            if(reservation.isPresent()){
                Optional<User> userToCancel = userService.findById(reservation.get().getUserId());
                if(userToCancel.isPresent()){
                    Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
                    if(restaurant.isPresent()){
                        reservationService.ownerCancelReservation(reservationId, cancellationMessage);
                       
                        //emailService.sendCancellationEmail(userToCancel.get().getEmail(), restaurant.get(), cancellationMessage);
                        return new ModelAndView("redirect:/restaurant/" + restaurantId + "/manage");
                    }
                }
            }
            return new ModelAndView("redirect:/400");
        }
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = "/reservations/{restaurantId}/{reservationId}/reject", method = RequestMethod.POST)
    public ModelAndView rejectReservationFromOwner(@ModelAttribute("loggedUser") final User loggedUser,
                                                   @PathVariable("restaurantId") final long restaurantId,
                                                   @PathVariable("reservationId") final int reservationId){
        if(loggedUser != null){
            Optional<Reservation> reservation = reservationService.findById(reservationId);
            if(reservation.isPresent()){
                Optional<User> userToCancel = userService.findById(reservation.get().getUserId());
                if(userToCancel.isPresent()){
                    Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
                    if(restaurant.isPresent()){
                        Locale locale = LocaleContextHolder.getLocale();
                        reservationService.ownerCancelReservation(reservationId, messageSource.getMessage("mail.rejection",null,locale));
                        //emailService.sendRejectionEmail(userToCancel.get().getEmail(), restaurant.get());
                        return new ModelAndView("redirect:/restaurant/" + restaurantId + "/manage");
                    }
                }
            }
            return new ModelAndView("redirect:/400");
        }
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = "/reservations/{restaurantId}/{reservationId}/confirm", method = RequestMethod.POST)
    public ModelAndView confirmReservation(@ModelAttribute("loggedUser") final User loggedUser,
                                           @PathVariable("restaurantId") final long restaurantId,
                                           @PathVariable("reservationId") final int reservationId){
        if(loggedUser != null){
            Optional<Reservation> reservation = reservationService.findById(reservationId);
            if(reservation.isPresent()){
                Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
                if(restaurant.isPresent()){
                    if(restaurant.get().getUserId() == loggedUser.getId()){
                        reservationService.confirmReservation(reservationId);
                        /*emailService.sendConfirmationEmail(reservation.get());*/
                        return new ModelAndView("redirect:/restaurant/" + restaurantId + "/manage");
                    }
                    return new ModelAndView("redirect:/403");
                }
            }
            return new ModelAndView("redirect:/400");
        }
        return new ModelAndView("redirect:/login");
    }
}
