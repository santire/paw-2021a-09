package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import javax.validation.Valid;

import ar.edu.itba.paw.service.MenuService;
import ar.edu.itba.paw.service.ReservationService;
import ar.edu.itba.paw.webapp.forms.ReservationForm;

import ar.edu.itba.paw.service.RestaurantService;
import ar.edu.itba.paw.webapp.forms.RestaurantForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.webapp.forms.UserForm;

import java.util.Date;
import java.util.Optional;

@Controller
public class HelloWorldController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("home");
        mav.addObject("popularRestaurants", restaurantService.getPopularRestaurants());
        return mav;
    }

    @RequestMapping("/restaurants")
    public ModelAndView restaurants(@RequestParam(required = false) String search) {
        final ModelAndView mav = new ModelAndView("restaurants");
        if (search == null) {
            search = "";
        }
        mav.addObject("restaurants", restaurantService.getAllRestaurants(search));

        return mav;
    }

    @RequestMapping(path = { "/register" }, method = RequestMethod.GET)
    public ModelAndView registerForm(@ModelAttribute("userForm") final UserForm form) {
        return new ModelAndView("register");
    }

    @RequestMapping(path = { "/register" }, method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("userForm") final UserForm form, final BindingResult errors) {
        // if there are errors it goes back to the register form without losing data
        // but letting the user know it has errors
        if (errors.hasErrors()) {
            return registerForm(form);
        }
        final User user = userService.register(form.getUsername(), form.getPassword(), form.getFirst_name(),
                form.getLast_name(), form.getEmail(), form.getPhone());
        return new ModelAndView("redirect:/user/" + user.getId());
    }

    @RequestMapping("/user/{userId}")
    public ModelAndView user(@PathVariable("userId") final long id) {
        final ModelAndView mav = new ModelAndView("home");
        mav.addObject("user", userService.findById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(path = { "/restaurant/{restaurantId}" }, method = RequestMethod.GET)
    public ModelAndView restaurant(@ModelAttribute("reservationForm") final ReservationForm form,
            @PathVariable("restaurantId") final long restaurantId) {

        final ModelAndView mav = new ModelAndView("restaurant");
        mav.addObject("restaurant",
                restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new));
        mav.addObject("menu", menuService.findMenuByRestaurantId(restaurantId));
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

    @RequestMapping(path = { "/registerRestaurant" }, method = RequestMethod.GET)
    public ModelAndView registerRestaurant(@ModelAttribute("RestaurantForm") final RestaurantForm form) {
        return new ModelAndView("registerRestaurant");
    }

    @RequestMapping(path = { "/registerRestaurant" }, method = RequestMethod.POST)
    public ModelAndView registerRestaurant(@Valid @ModelAttribute("RestaurantForm") final RestaurantForm form,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            return registerRestaurant(form);
        }

        restaurantService.registerRestaurant(form.getName(), form.getAddress(), form.getPhoneNumber(), 0, 1);
        final Restaurant restaurant = restaurantService.registerRestaurant(form.getName(), form.getAddress(),
                form.getPhoneNumber(), 0, 1);

        return new ModelAndView("redirect:/restaurant/" + restaurant.getId());
    }

}
