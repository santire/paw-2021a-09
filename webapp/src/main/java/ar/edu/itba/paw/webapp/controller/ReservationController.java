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



import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    private MessageSource messageSource;


    @RequestMapping(path = { "/reservations" }, method = RequestMethod.GET)
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
            return mav;
         }
         else return new ModelAndView("redirect:/login");
     }

    @RequestMapping(path = { "/reservations/history" }, method = RequestMethod.GET)
    public ModelAndView userReservationsHistory(
            @ModelAttribute("loggedUser") final User loggedUser,
            @RequestParam(defaultValue="1") Integer page){

        final ModelAndView mav = new ModelAndView("myReservationsHistory");

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


    @RequestMapping(path = "/reservations/{restaurantId}/{reservationId}/cancel", method = RequestMethod.POST)
    public ModelAndView cancelReservationFromOwner(@ModelAttribute("loggedUser") final User loggedUser,
                                                   @PathVariable("restaurantId") final long restaurantId,
                                                   @PathVariable("reservationId") final int reservationId,
                                                   @RequestParam("cancellationMessage") final String cancellationMessage){
        if(loggedUser != null){
            Optional<Reservation> maybeReservation = reservationService.findById(reservationId);
            if(maybeReservation.isPresent()){
                // Reservation reservation = maybeReservation.get();

                // Optional<User> userToCancel = userService.findById(reservation.get().getUserId());
                // User userToCancel = reservation.getUser();
                // Restaurant restaurant = reservation.getRestaurant();
                reservationService.ownerCancelReservation(reservationId, cancellationMessage);
                return new ModelAndView("redirect:/restaurant/" + restaurantId + "/manage/confirmed");

                // if(userToCancel.isPresent()){
                    // Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
                    // if(restaurant.isPresent()){
                    // }
                // }
             }
             return new ModelAndView("redirect:/403");
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
                Locale locale = LocaleContextHolder.getLocale();
                reservationService.ownerCancelReservation(reservationId, messageSource.getMessage("mail.rejection",null,locale));
                return new ModelAndView("redirect:/restaurant/" + restaurantId + "/manage/pending");
                // Optional<User> userToCancel = userService.findById(reservation.get().getUserId());
                // if(userToCancel.isPresent()){
                    // Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
                    // if(restaurant.isPresent()){
                    // }
                // }
             }
            return new ModelAndView("redirect:/403");
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
                 // Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
                    Restaurant restaurant = reservation.get().getRestaurant();
                    if (restaurant.getOwner().getId() == loggedUser.getId()) {
                     reservationService.confirmReservation(reservation.get());
                     return new ModelAndView("redirect:/restaurant/" + restaurantId + "/manage/pending");
                    }
                 // if(restaurant.isPresent()){
                     // if(restaurant.get().getUserId() == loggedUser.getId()){
                     // }
                     // return new ModelAndView("redirect:/403");
                 // }
             }
             return new ModelAndView("redirect:/403");
         }
         return new ModelAndView("redirect:/login");
     }
 }
