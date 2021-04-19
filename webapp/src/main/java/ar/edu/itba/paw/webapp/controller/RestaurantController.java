package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import javax.validation.Valid;


import ar.edu.itba.paw.service.MenuService;
import ar.edu.itba.paw.service.ReservationService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;

import ar.edu.itba.paw.service.*;

import ar.edu.itba.paw.webapp.forms.ReservationForm;
import ar.edu.itba.paw.webapp.forms.RestaurantForm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.edu.itba.paw.webapp.exceptions.RestaurantNotFoundException;

import java.util.*;

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
    public ModelAndView restaurant(@ModelAttribute("reservationForm") final ReservationForm form,
            @PathVariable("restaurantId") final long restaurantId) {
        final ModelAndView mav = new ModelAndView("restaurant");

        Optional<User> user = loggedUser();
        if(user.isPresent()){
            long userId = user.get().getId();
            mav.addObject("loggedUser", true);
            Optional<Rating> userRating = ratingService.getRating(userId, restaurantId);
            if(userRating.isPresent()){
                mav.addObject("rated", true);
                mav.addObject("userRatingToRestaurant", userRating.get().getRating());
                mav.addObject("userLikesRestaurant", likesService.userLikesRestaurant(userId, restaurantId));
            }
            else{ mav.addObject("rated", false); }
        }
        else{
            mav.addObject("loggedUser", false);
            mav.addObject("rated", false);
        }
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

        //TODO handled in webauthconfig
        //
        // Optional<User> user = loggedUser();
       // if(user.isPresent()){
            return new ModelAndView("registerRestaurant");
       // }
       // return new ModelAndView("redirect:/login");
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

        if(restaurantService.getRestaurantsFromOwner(user.getId()).size() == 1)
        updateAuthorities();

        return new ModelAndView("redirect:/restaurant/" + restaurant.getId());

        //Optional<User> user = loggedUser();
        //if(user.isPresent()){
         //   final Restaurant restaurant = restaurantService.registerRestaurant(form.getName(), form.getAddress(),
         //           form.getPhoneNumber(), 0, user.get().getId());
         //   return new ModelAndView("redirect:/restaurant/" + restaurant.getId());
        //}
        //return new ModelAndView("redirect:/login");
    }



    @RequestMapping(path = {"/restaurant/rate/set/{restaurantId}"}, method = RequestMethod.POST)
    public ModelAndView setRating(@PathVariable("restaurantId") final long restaurantId, @RequestParam("rating") int rating){
        Optional<User> user = loggedUser();
        if(user.isPresent()){
            long userId = user.get().getId();
            ratingService.rateRestaurant(userId, restaurantId, rating);
        }
        return new ModelAndView("redirect:/restaurant/" + restaurantId);
    }

    @RequestMapping(path = {"/restaurant/rate/update/{restaurantId}"}, method = RequestMethod.POST)
    public ModelAndView updateRating(@PathVariable("restaurantId") final long restaurantId, @RequestParam("rating") int rating){
        Optional<User> user = loggedUser();
        if(user.isPresent()){
            long userId = user.get().getId();
            ratingService.modifyRestaurantRating(userId, restaurantId, rating);
        }
        return new ModelAndView("redirect:/restaurant/" + restaurantId);
    }

    @RequestMapping(path = {"restaurant/like/{restaurantId}"}, method = RequestMethod.POST)
    public ModelAndView like(@PathVariable("restaurantId") final long restaurantId){
        Optional<User> user = loggedUser();
        if(user.isPresent()){
            long userId = user.get().getId();
            likesService.like(userId, restaurantId);
            return new ModelAndView("redirect:/restaurant/" + restaurantId);
        }
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = {"restaurant/dislike/{restaurantId}"}, method = RequestMethod.POST)
    public ModelAndView dislike(@PathVariable("restaurantId") final long restaurantId){
        Optional<User> user = loggedUser();
        if(user.isPresent()){
            long userId = user.get().getId();
            likesService.dislike(userId, restaurantId);
            return new ModelAndView("redirect:/restaurant/" + restaurantId);
        }
        return new ModelAndView("redirect:/login");
    }




    @RequestMapping(path = { "/restaurant/{restaurantId}/delete" })
    public ModelAndView deleteRestaurant(@PathVariable("restaurantId") final long restaurantId) {

        User user = loggedUser().orElseThrow(UserNotFoundException::new);
        List<Restaurant> restaurants = restaurantService.getRestaurantsFromOwner(user.getId());


            if(userService.isTheRestaurantOwner(user.getId(), restaurantId)){
                restaurantService.deleteRestaurantById(restaurantId);

                if(restaurantService.getRestaurantsFromOwner(user.getId()).isEmpty())
                    updateAuthorities();

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





    @ModelAttribute
    public void updateAuthorities() {
        User user = loggedUser().orElseThrow(UserNotFoundException::new);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if(!restaurantService.getRestaurantsFromOwner(user.getId()).isEmpty()){
            authorities.add(new SimpleGrantedAuthority("ROLE_RESTAURANTOWNER"));
        }
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), authorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }



}
