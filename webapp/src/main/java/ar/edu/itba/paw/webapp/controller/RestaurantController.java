package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import javax.validation.Valid;

import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.forms.ReservationForm;
import ar.edu.itba.paw.webapp.forms.RestaurantForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.edu.itba.paw.webapp.exceptions.RestaurantNotFoundException;

import java.util.Date;
import java.util.Optional;

@Controller
public class RestaurantController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private LikesService likesService;

    @RequestMapping(path = { "/restaurant/{restaurantId}" }, method = RequestMethod.GET)
    public ModelAndView restaurant(@ModelAttribute("loggedUser") final User loggedUser, @ModelAttribute("reservationForm") final ReservationForm form,
            @PathVariable("restaurantId") final long restaurantId) {
        final ModelAndView mav = new ModelAndView("restaurant");

        if(loggedUser != null){
            Optional<Rating> userRating = ratingService.getRating(loggedUser.getId(), restaurantId);
            if(userRating.isPresent()){
                mav.addObject("rated", true);
                mav.addObject("userRatingToRestaurant", userRating.get().getRating());
                mav.addObject("userLikesRestaurant", likesService.userLikesRestaurant(loggedUser.getId(), restaurantId));
            }
        }

        mav.addObject("restaurant",
                restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new));
        return mav;
    }

    @RequestMapping(path = { "/restaurant/{restaurantId}" }, method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute("loggedUser") final User loggedUser, @Valid @ModelAttribute("reservationForm") final ReservationForm form,
            final BindingResult errors, @PathVariable("restaurantId") final long restaurantId,
            RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return restaurant(loggedUser, form, restaurantId);
        }

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
        reservationService.addReservation(user.getId(), restaurantId, date, Long.parseLong(form.getQuantity()));
        redirectAttributes.addFlashAttribute("madeReservation", true);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(path = { "/register/restaurant" }, method = RequestMethod.GET)
    public ModelAndView registerRestaurant(@ModelAttribute("loggedUser") final User loggedUser, @ModelAttribute("RestaurantForm") final RestaurantForm form) {
        if(loggedUser != null){
            return new ModelAndView("registerRestaurant");
        }
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = { "/register/restaurant" }, method = RequestMethod.POST)
    public ModelAndView registerRestaurant(@ModelAttribute("loggedUser") final User loggedUser, @Valid @ModelAttribute("RestaurantForm") final RestaurantForm form,
                                           final BindingResult errors) {
        if (errors.hasErrors()) {
            return registerRestaurant(loggedUser, form);
        }

        if(loggedUser != null){
            final Restaurant restaurant = restaurantService.registerRestaurant(form.getName(), form.getAddress(),
                    form.getPhoneNumber(), 0, loggedUser.getId());
            return new ModelAndView("redirect:/restaurant/" + restaurant.getId());
        }
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = {"/restaurant/{restaurantId}/rate"}, method = RequestMethod.POST)
    public ModelAndView setRating(@ModelAttribute("loggedUser") final User loggedUser, @PathVariable("restaurantId") final long restaurantId, @RequestParam("rating") int rating){
        if(loggedUser != null){
            long userId = loggedUser.getId();
            ratingService.rateRestaurant(userId, restaurantId, rating);
        }
        return new ModelAndView("redirect:/restaurant/" + restaurantId);
    }

    @RequestMapping(path = {"/restaurant/{restaurantId}/rate"}, method = RequestMethod.PUT)
    public ModelAndView updateRating(@ModelAttribute("loggedUser") final User loggedUser, @PathVariable("restaurantId") final long restaurantId, @RequestParam("rating") int rating){
        if(loggedUser != null){
            long userId = loggedUser.getId();
            ratingService.modifyRestaurantRating(userId, restaurantId, rating);
        }
        return new ModelAndView("redirect:/restaurant/" + restaurantId);
    }

    @RequestMapping(path = {"restaurant/{restaurantId}/like"}, method = RequestMethod.POST)
    public ModelAndView like(@ModelAttribute("loggedUser") final User loggedUser, @PathVariable("restaurantId") final long restaurantId){
        if(loggedUser != null){
            long userId = loggedUser.getId();
            likesService.like(userId, restaurantId);
            return new ModelAndView("redirect:/restaurant/" + restaurantId);
        }
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = {"restaurant/{restaurantId}/dislike"}, method = RequestMethod.POST)
    public ModelAndView dislike(@ModelAttribute("loggedUser") final User loggedUser, @PathVariable("restaurantId") final long restaurantId){
        if(loggedUser != null){
            long userId = loggedUser.getId();
            likesService.dislike(userId, restaurantId);
            return new ModelAndView("redirect:/restaurant/" + restaurantId);
        }
        return new ModelAndView("redirect:/login");
    }

}
