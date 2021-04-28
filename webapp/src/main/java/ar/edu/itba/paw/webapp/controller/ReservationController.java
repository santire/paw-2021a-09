package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.ReservationService;
import ar.edu.itba.paw.service.RestaurantService;
import ar.edu.itba.paw.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ReservationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RestaurantService restaurantService;

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
}
