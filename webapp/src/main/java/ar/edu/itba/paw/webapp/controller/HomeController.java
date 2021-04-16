package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;


import ar.edu.itba.paw.service.RestaurantService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import org.springframework.validation.ObjectError;

import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ar.edu.itba.paw.service.UserService;

import ar.edu.itba.paw.webapp.forms.UserForm;



@Controller
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;



    @Autowired
    private RestaurantService restaurantService;


    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("home");
        List<Restaurant> popularRestaurants = restaurantService.getPopularRestaurants();
        mav.addObject("popularRestaurants", popularRestaurants);
        LOGGER.debug("Amount of popular restaurants: {}", popularRestaurants.size());
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


    @RequestMapping(path ={"/register"}, method = RequestMethod.GET)
    public ModelAndView registerForm(@ModelAttribute("userForm") final UserForm form) {
        return new ModelAndView("register");
    }



    @RequestMapping(path ={"/register"}, method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("userForm") final UserForm form, final BindingResult errors ) {
        // if there are errors it goes back to the register form without losing data
        // but letting the user know it has errors
        if (errors.hasErrors()) {
            return registerForm(form);
        }

        try{
           final User user = userService.register(form.getUsername(), form.getPassword(), form.getFirst_name(),
                    form.getLast_name(), form.getEmail(), form.getPhone());

            return new ModelAndView("redirect:/user/" + user.getId());
        } catch (Exception e) {

            errors.addError(new ObjectError("emailError", "email already in use"));
            return registerForm(form);
        }
    }

    @RequestMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) final String error) {
        final ModelAndView mav = new ModelAndView("login");

        if (error != null) {
            mav.addObject("error", true);
        }
        else{
            mav.addObject("error", false);
        }

        return mav;
    }







    @ModelAttribute
    public Optional<User> loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = userService.findByEmail((String) auth.getName());
        LOGGER.debug("Logged user is {}", user);
        return user;
    }

}
