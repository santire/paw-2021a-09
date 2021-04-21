package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;


import ar.edu.itba.paw.service.LikesService;
import ar.edu.itba.paw.service.RestaurantService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;


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
    private LikesService likesService;

    @Autowired
    private RestaurantService restaurantService;


    @RequestMapping("/")
    public ModelAndView helloWorld(@ModelAttribute("loggedUser") final User loggedUser) {
        final ModelAndView mav = new ModelAndView("home");
        List<Restaurant> popularRestaurants = restaurantService.getPopularRestaurants();
        mav.addObject("popularRestaurants", popularRestaurants);
        LOGGER.debug("Amount of popular restaurants: {}", popularRestaurants.size());

        if(loggedUser != null){
            List<Restaurant> likedRestaurants = likesService.getLikedRestaurants(loggedUser.getId());
            mav.addObject("likedRestaurants", likedRestaurants);
        }
        return mav;
    }

    @RequestMapping("/restaurants")
    public ModelAndView restaurants(@RequestParam(required = false) String search) {
        final ModelAndView mav = new ModelAndView("restaurants");
        if (search == null || search.isEmpty()) {
            mav.addObject("userIsSearching", false);
            mav.addObject("restaurants", restaurantService.getAllRestaurants());
            return mav;
        }
        mav.addObject("userIsSearching", true);
        mav.addObject("searchString", search);
        // To delete after sprint
        if(search.startsWith("%")){
            mav.addObject("restaurants", new ArrayList<Restaurant>());
            return mav;
        }
        mav.addObject("restaurants", restaurantService.getAllRestaurants(search));
        return mav;
    }

    @RequestMapping("/restaurants/mine")
    public ModelAndView restaurants(@ModelAttribute("loggedUser") final User loggedUser) {
        if (loggedUser != null) {
            final ModelAndView mav = new ModelAndView("restaurants");
            List<Restaurant> userRestaurants = restaurantService.getRestaurantsFromOwner(loggedUser.getId());
            mav.addObject("restaurants", userRestaurants);
            return mav;

        }

        return new ModelAndView("redirect:/login");
    }


    @RequestMapping(path ={"/register"}, method = RequestMethod.GET)
    public ModelAndView registerForm(@ModelAttribute("userForm") final UserForm form,@RequestParam(value = "error", required = false) final String error) {
       ModelAndView mav = new ModelAndView("register");
       if(error != null){
           mav.addObject("error", error);
       }
        return mav;
    }



    @RequestMapping(path ={"/register"}, method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("userForm") final UserForm form, final BindingResult errors ) {
        // if there are errors it goes back to the register form without losing data
        // but letting the user know it has errors
        if(!form.getPassword().equals(form.getRepeatPassword())){
            return registerForm(form, "password");
        }
        if (errors.hasErrors()) {
            return registerForm(form,null);
        }

        try{
           final User user = userService.register(form.getUsername(), form.getPassword(), form.getFirstName(),
                    form.getLastName(), form.getEmail(), form.getPhone());

            return new ModelAndView("redirect:/login");
        } catch (Exception e) {

            return registerForm(form, "email");
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

    @RequestMapping("/403")
    public ModelAndView forbidden() {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("code", 403);

        return mav;
    }


/*
    @ModelAttribute
    public Optional<User> loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = userService.findByEmail((String) auth.getName());
        LOGGER.debug("Logged user is {}", user);
        return user;
    }

 */

}
