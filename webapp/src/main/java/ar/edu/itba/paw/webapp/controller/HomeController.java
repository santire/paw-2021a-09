package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.model.*;

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
import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import ar.edu.itba.paw.model.exceptions.TokenDoesNotExistException;
import ar.edu.itba.paw.model.exceptions.TokenExpiredException;
import ar.edu.itba.paw.service.LikesService;
import ar.edu.itba.paw.service.RestaurantService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.forms.UserForm;



@Controller
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    private static final int AMOUNT_OF_RESTAURANTS = 6;
    private static final int AMOUNT_OF_POPULAR_RESTAURANTS = 10;
    private static final int POPULAR_MIN_RATING = 8;

    @Autowired
    private UserService userService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private PawUserDetailsService pawUserDetailsService;


    @RequestMapping("/")
    public ModelAndView helloWorld(@ModelAttribute("loggedUser") final User loggedUser, 
            @RequestParam(defaultValue = "1") Integer page) {
        final ModelAndView mav = new ModelAndView("home");

        if(page == null || page <1) {
            page=1;
        }

        List<Restaurant> popularRestaurants = restaurantService.getPopularRestaurants(AMOUNT_OF_POPULAR_RESTAURANTS, POPULAR_MIN_RATING);
        mav.addObject("popularRestaurants", popularRestaurants);
        LOGGER.debug("Amount of popular restaurants: {}", popularRestaurants.size());

        List<Restaurant> hotRestaurants = restaurantService.getHotRestaurants(7);
        mav.addObject("hotRestaurants", hotRestaurants);
        LOGGER.debug("Amount of hot restaurants: {}", hotRestaurants.size());

        if(loggedUser != null){
            List<Restaurant> likedRestaurants = likesService.getLikedRestaurants(loggedUser.getId());
            mav.addObject("likedRestaurants", likedRestaurants);
        }
        return mav;
    }






    @RequestMapping(path ={"/restaurants"}, method = RequestMethod.GET)
    public ModelAndView restaurants(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(required = false) String search, @RequestParam(required = false) int[] tags,
                                    @RequestParam(defaultValue = "1") int min, @RequestParam(defaultValue = "10000") int max,
                                    @RequestParam(defaultValue = "namedesc") String sortby)
                                    {

        final ModelAndView mav = new ModelAndView("restaurants");
        if (search != null) {
            search = search.trim().replaceAll("[^a-zA-Z0-9 ()'´¨!]", "");
        }else {
            search = "";
        }

        mav.addObject("minPrice", min);
        mav.addObject("maxPrice", max);
        mav.addObject("tags", Tags.allTags());

        List<Tags> tagsSelected = new ArrayList<>();
        List<Integer> tagsChecked = new ArrayList<>();
        if(tags!=null){
            for( int i : tags){
                if(Tags.valueOf(i) == null)
                    return new ModelAndView("redirect:/403");
                tagsSelected.add(Tags.valueOf(i));
                tagsChecked.add(i);
            }
        }
        mav.addObject("tagsChecked", tagsChecked);

        Sorting sort = Sorting.NAME;
        boolean desc = true;
        switch (sortby) {
            case ("nameasc"):
                sort = Sorting.NAME;
                desc = false;
                break;
            case ("populardesc"):
                sort = Sorting.RATING;
                desc = true;
                break;
            case ("popularasc"):
                sort = Sorting.RATING;
                desc = false;
                break;
            case ("reservationsdesc"):
                sort = Sorting.RESERVATIONS;
                desc = true;
                break;
            case ("reservationsasc"):
                sort = Sorting.RESERVATIONS;
                desc = false;
                break;
            case ("pricedesc"):
                sort = Sorting.PRICE;
                desc = true;
                break;
            case ("priceasc"):
                sort = Sorting.PRICE;
                desc = false;
                break;
        }

        // Catching invalid page value and setting it at max or min
        // depending on the overflow direction
        int maxPages = restaurantService.getRestaurantsFilteredByPageCount(AMOUNT_OF_RESTAURANTS, search, tagsSelected, min, max);
        if(page == null || page <1) {
            page=1;
        }else if (page > maxPages) {
            page = maxPages;
        }

        mav.addObject("userIsSearching", !search.isEmpty());
        mav.addObject("searchString", search);
        mav.addObject("maxPages", maxPages);

        mav.addObject("restaurants", restaurantService.getRestaurantsFilteredBy(page, AMOUNT_OF_RESTAURANTS, search, tagsSelected,min,max, sort, desc, 7));
        //mav.addObject("restaurants", restaurantService.getAllRestaurants(page, AMOUNT_OF_RESTAURANTS, search));
        mav.addObject("page", page);
        return mav;
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
            if(form.getPassword() == null || !form.getPassword().equals(form.getRepeatPassword())) {
                errors.rejectValue("repeatPassword", 
                                    "userForm.repeatPassword",
                                    "Passwords do not match");
            }
            if (form.getEmail() == null || !form.getEmail().trim().isEmpty() 
                    && userService.findByEmail(form.getEmail()).isPresent()){
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
        } catch (EmailInUseException e) {
            LOGGER.error("Email {} is already in use (this should have been caught by validator)", e.getEmail());
            return registerForm(form, errors);
        } catch(TokenCreationException e) {
            LOGGER.error("Could not generate token");
            return registerForm(form, errors).addObject("tokenError", true);
        }
    }


    @RequestMapping(value={"/activate"}, method = RequestMethod.GET)
    public ModelAndView activate(@RequestParam(name="token", required=true) final String token) {
        User user;
        try {
            user = userService.activateUserByToken(token);
        } catch(TokenExpiredException e) {
            LOGGER.error("token {} is expired", token);
            return new ModelAndView("redirect:/register").addObject("expiredToken", true);
        }catch(TokenDoesNotExistException e) {
            LOGGER.warn("token {} does not exist", token);
            return new ModelAndView("activate").addObject("invalidToken", true);
        } catch(Exception e) {
            // Unexpected error happened, showing register screen with generic error message
            return new ModelAndView("redirect:/register").addObject("tokenError", true);
        }

        UserDetails userDetails = pawUserDetailsService.loadUserByUsername(user.getEmail());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value={ "/login" }, method=RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) final String error) {
        final ModelAndView mav = new ModelAndView("login");
        mav.addObject("error", error!=null);

        return mav;
    }

    @RequestMapping("/403")
    public ModelAndView forbidden() {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("code", 403);

        return mav;
    }


}
