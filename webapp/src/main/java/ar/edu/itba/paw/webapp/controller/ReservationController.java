package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ReservationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private EmailService emailService;


    @RequestMapping("/reservations")
    public ModelAndView userReservations(@ModelAttribute("loggedUser") final User loggedUser){
        final ModelAndView mav = new ModelAndView("myReservations");
        if(loggedUser != null){
            long userId = loggedUser.getId();
            List<Reservation> reservations = reservationService.findByUser(userId);
            if(reservations.isEmpty()){
                mav.addObject("userHasReservations", false);
            }
            else{
                mav.addObject("userHasReservations", true);
            }
            mav.addObject("reservations", reservations);
            mav.addObject("isOwner", false);
            return mav;
        }
        else return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = "/reservations/{reservationId}/cancel", method = RequestMethod.POST)
    public ModelAndView cancelReservation(@ModelAttribute("loggedUser") final User loggedUser,
                                          @PathVariable("reservationId") final int reservationId){
        if(loggedUser != null){
            reservationService.cancelReservation(reservationId);
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
                        reservationService.cancelReservation(reservationId);
                        emailService.sendCancellationEmail(userToCancel.get().getEmail(), restaurant.get(), cancellationMessage);
                        return new ModelAndView("redirect:/restaurant/" + restaurantId + "/manage");
                    }
                }
            }
            return new ModelAndView("redirect:/400");
        }
        return new ModelAndView("redirect:/login");
    }
}