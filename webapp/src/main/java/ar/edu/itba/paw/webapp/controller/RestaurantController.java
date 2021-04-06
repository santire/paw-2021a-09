package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.User;

import javax.validation.Valid;

import ar.edu.itba.paw.service.ReservationService;
import ar.edu.itba.paw.webapp.forms.ReservationForm;

import ar.edu.itba.paw.service.RestaurantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.RestaurantNotFoundException;

import java.util.Date;
import java.util.Optional;

@Controller
public class RestaurantController {
    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RestaurantService restaurantService;

    @RequestMapping(path ={"/restaurant/{restaurantId}"}, method = RequestMethod.GET)
    public ModelAndView restaurant( @ModelAttribute("reservationForm") final ReservationForm form, @PathVariable("restaurantId") final long restaurantId ) {

        final ModelAndView mav = new ModelAndView("restaurant");
        mav.addObject("restaurant", restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new));
        return mav;
    }

    @RequestMapping(path ={"/restaurant/{restaurantId}"}, method = RequestMethod.POST)
    public ModelAndView register( @Valid @ModelAttribute("reservationForm") final ReservationForm form, final BindingResult errors, @PathVariable("restaurantId") final long restaurantId ) {

        if (errors.hasErrors()) {
            return restaurant(form, restaurantId);
        }
        // User user = userService.findByEmail(form.getEmail()).orElse(userService.register(form.getEmail()));
        User user;
        Optional<User> maybeUser = userService.findByEmail(form.getEmail());
        if (maybeUser.isPresent()) {
            user = maybeUser.get();
        } else {
            user = userService.register(form.getEmail());
        }
        Date date = new Date();
        date.setHours(form.getDate());
        date.setMinutes(0);
        date.setSeconds(0);
        reservationService.addReservation(user.getId(),restaurantId,date,Long.parseLong(form.getQuantity()));
        return new ModelAndView("redirect:/");
    }


}
