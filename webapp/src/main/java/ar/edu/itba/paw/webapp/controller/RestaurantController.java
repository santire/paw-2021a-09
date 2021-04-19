package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import javax.validation.Valid;

import ar.edu.itba.paw.service.MenuService;
import ar.edu.itba.paw.service.ReservationService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.forms.ReservationForm;
import ar.edu.itba.paw.webapp.forms.RestaurantForm;
import ar.edu.itba.paw.service.RestaurantService;

import ar.edu.itba.paw.webapp.forms.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.RestaurantNotFoundException;

import java.util.Date;
import java.util.List;
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

    @RequestMapping(path = { "/restaurant/{restaurantId}" }, method = RequestMethod.GET)
    public ModelAndView restaurant(@ModelAttribute("reservationForm") final ReservationForm form,
            @PathVariable("restaurantId") final long restaurantId) {

        final ModelAndView mav = new ModelAndView("restaurant");
        mav.addObject("restaurant",
                restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new));
        mav.addObject("menu",
                menuService.findMenuByRestaurantId(restaurantId));
        return mav;
    }

    @RequestMapping(path = { "/restaurant/{restaurantId}" }, method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("reservationForm") final ReservationForm form,
            final BindingResult errors, @PathVariable("restaurantId") final long restaurantId,
            RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return restaurant(form, restaurantId);
        }
        // User user =
        // userService.findByEmail(form.getEmail()).orElse(userService.register(form.getEmail()));
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

    @RequestMapping(path = { "/register-restaurant" }, method = RequestMethod.GET)
    public ModelAndView registerRestaurant(@ModelAttribute("RestaurantForm") final RestaurantForm form) {
        return new ModelAndView("registerRestaurant");
    }

    @RequestMapping(path = { "/register-restaurant" }, method = RequestMethod.POST)
    public ModelAndView registerRestaurant(@Valid @ModelAttribute("RestaurantForm") final RestaurantForm form,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            return registerRestaurant(form);
        }

        User user = loggedUser().orElseThrow(UserNotFoundException::new);

        final Restaurant restaurant = restaurantService.registerRestaurant(form.getName(), form.getAddress(),
                form.getPhoneNumber(), 0, user.getId());

        return new ModelAndView("redirect:/restaurant/" + restaurant.getId());
    }




    @RequestMapping(path = { "/restaurant/{restaurantId}/delete" })
    public ModelAndView deleteRestaurant(@PathVariable("restaurantId") final long restaurantId) {

        User user = loggedUser().orElseThrow(UserNotFoundException::new);
        List<Restaurant> restaurants = restaurantService.getRestaurantsFromOwner(user.getId());


            if(userService.isTheRestaurantOwner(user.getId(), restaurantId)){
                restaurantService.deleteRestaurantById(restaurantId);
                return new ModelAndView("redirect:/user/edit");
            }
        return new ModelAndView("redirect:/403");

    }



    @RequestMapping(path ={"/restaurant/{restaurantId}/edit"}, method = RequestMethod.GET)
    public ModelAndView editRestaurant(@PathVariable("restaurantId") final long restaurantId, @ModelAttribute("updateRestaurantForm") final RestaurantForm form) {

        User user = loggedUser().orElseThrow(UserNotFoundException::new);
        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);


        if(userService.isTheRestaurantOwner(user.getId(), restaurantId)){
            final ModelAndView mav = new ModelAndView("editRestaurant");
            mav.addObject("restaurant", restaurant);

            return mav;
            }
        return new ModelAndView("redirect:/403");
    }


    @RequestMapping(path ={"/restaurant/{restaurantId}/edit"}, method = RequestMethod.POST, params = "edit-restaurant-name")
    public ModelAndView editRestaurantName(@Valid @ModelAttribute("updateRestaurantForm") final RestaurantForm form, final BindingResult errors, @PathVariable("restaurantId") final long restaurantId ) {
        if (errors.hasErrors()) {
            return editRestaurant(restaurantId, form);
        }
        restaurantService.updateName(restaurantId, form.getName());
        return new ModelAndView("redirect:/restaurant/" + restaurantId + "/edit");
    }

    @RequestMapping(path ={"/restaurant/{restaurantId}/edit"}, method = RequestMethod.POST, params = "edit-restaurant-address")
    public ModelAndView editRestaurantAddress(@Valid @ModelAttribute("updateRestaurantForm") final RestaurantForm form, final BindingResult errors,  @PathVariable("restaurantId") final long restaurantId ) {
        if (errors.hasErrors()) {
            return editRestaurant(restaurantId, form);
        }
        restaurantService.updateAddress(restaurantId, form.getAddress());
        return new ModelAndView("redirect:/restaurant/" + restaurantId + "/edit");
    }

    @RequestMapping(path ={"/restaurant/{restaurantId}/edit"}, method = RequestMethod.POST, params = "edit-restaurant-phone")
    public ModelAndView editRestaurantPhone(@Valid @ModelAttribute("updateRestaurantForm") final RestaurantForm form, final BindingResult errors,  @PathVariable("restaurantId") final long restaurantId ) {
        if (errors.hasErrors()) {
            return editRestaurant(restaurantId, form);
        }
        restaurantService.updatePhoneNumber(restaurantId, form.getPhoneNumber());
        return new ModelAndView("redirect:/restaurant/" + restaurantId + "/edit");
    }





    @ModelAttribute
    public Optional<User> loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = userService.findByEmail((String) auth.getName());
        LOGGER.debug("Logged user is {}", user);
        return user;
    }
}
