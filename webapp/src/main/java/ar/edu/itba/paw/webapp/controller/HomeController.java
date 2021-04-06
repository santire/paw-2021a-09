package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import javax.validation.Valid;

import ar.edu.itba.paw.service.RestaurantService;
import ar.edu.itba.paw.webapp.forms.RestaurantForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import ar.edu.itba.paw.service.UserService;

import ar.edu.itba.paw.webapp.forms.UserForm;




@Controller
public class HomeController {

    @Autowired
    private UserService userService;


    @Autowired
    private RestaurantService restaurantService;


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


    @RequestMapping(path ={"/registerRestaurant"}, method = RequestMethod.GET)
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
