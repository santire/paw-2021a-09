package ar.edu.itba.paw.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.LikesService;
import ar.edu.itba.paw.service.RestaurantService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
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

    @Autowired
    private PawUserDetailsService PawUserDetailsService;


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
        if (search != null) {
            search = search.trim().replaceAll("[^a-zA-Z0-9 ()'´¨!]", "");
        }
        mav.addObject("userIsSearching", !search.isEmpty());
        mav.addObject("searchString", search);
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
    public ModelAndView registerForm(@ModelAttribute("userForm") final UserForm form,
            final BindingResult errors) {
       return new ModelAndView("register");
    }



    @RequestMapping(path ={"/register"}, method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("userForm") final UserForm form, 
            final BindingResult errors ) {
        // if there are errors it goes back to the register form without losing data
        // but letting the user know it has errors
        if(form != null && errors != null){
            if(!form.getPassword().equals(form.getRepeatPassword())) {
                errors.rejectValue("repeatPassword", 
                                    "userForm.repeatPassword",
                                    "Passwords do not match");
            }
            if (!form.getEmail().trim().isEmpty() && userService.findByEmail(form.getEmail()).isPresent()){
                errors.rejectValue("emailInUse", 
                                    "userForm.emailInUse",
                                    "Email is already in use");
            }
        }

        if (errors!=null && errors.hasErrors()) {
            return registerForm(form,errors);
        }

        try{
           userService.register(form.getUsername(), form.getPassword(), form.getFirstName(),
                    form.getLastName(), form.getEmail(), form.getPhone());
            return new ModelAndView("activate");
        } catch (Exception e) {
            LOGGER.warn("Something went wrong registering user: {}", e.getMessage());
            return registerForm(form, errors);
        }
    }


    @RequestMapping(value={"/activate"}, method = RequestMethod.GET)
    public ModelAndView activate(@RequestParam(name="token", required=true) final String token) {
        User user;
        try {
            user = userService.activateUserByToken(token);
            //TODO separate this by exceptions
        } catch(Exception e) {
            LOGGER.error("Couldn't activate user with token {}", token);
            return new ModelAndView("redirect:/login");
        } 

        UserDetails userDetails = PawUserDetailsService.loadUserByUsername(user.getEmail());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ModelAndView("redirect:/");
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
