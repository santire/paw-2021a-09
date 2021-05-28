package ar.edu.itba.paw.webapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.Tags;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.service.ReservationService;
import ar.edu.itba.paw.service.RestaurantService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.forms.RestaurantForm;

@Controller
public class RestaurantManagmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantManagmentController.class);
    private static final int AMOUNT_OF_RESERVATIONS = 2;

    @Autowired
    private UserService userService;

    // @Autowired
    // private ReservationService reservationService;

    @Autowired
    private RestaurantService restaurantService;


    // Edit restaurant
    @RequestMapping(path ={"/restaurant/{restaurantId}/edit"}, method = RequestMethod.GET)
    public ModelAndView editRestaurant(
            @ModelAttribute("loggedUser") final User loggedUser, 
            @PathVariable("restaurantId") final long restaurantId, 
            @ModelAttribute("RestaurantForm") final RestaurantForm form) {

            if (loggedUser != null) {
                Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
                if(userService.isTheRestaurantOwner(loggedUser.getId(), restaurantId)){
                    final ModelAndView mav = new ModelAndView("editRestaurant");
                    if(form.getName() != null && !form.getName().isEmpty()) {
                        restaurant.setName(form.getName());
                    }
                    if(form.getAddress() != null && !form.getAddress().isEmpty()) {
                        restaurant.setAddress(form.getAddress());
                    }
                    if(form.getPhoneNumber() != null && !form.getPhoneNumber().isEmpty()) {
                        restaurant.setPhoneNumber(form.getPhoneNumber());
                    }
                    mav.addObject("restaurant", restaurant);

                    mav.addObject("tags", Tags.allTags());
                    // List<Integer> tagsChecked = new ArrayList<>();
                    List<Integer> tagsChecked = restaurant.getTags().stream().map(t -> t.getValue()).collect(Collectors.toList());
                    mav.addObject("tagsChecked", tagsChecked);


                    return mav;
                }
            }

        return new ModelAndView("redirect:/403");
    }

    @RequestMapping(path={"/restaurant/{restaurantId}/edit"}, method = RequestMethod.POST)
    public ModelAndView editRestaurant(@ModelAttribute("loggedUser") final User loggedUser, 
            @PathVariable("restaurantId") final long restaurantId, 
            @Valid @ModelAttribute("RestaurantForm") final RestaurantForm form,
            final BindingResult errors) {


        if (form.getTags().length>3) {
            errors.rejectValue("tags", "restaurant.edit.tagsLimit");
        }


        if (errors.hasErrors()) {
            LOGGER.debug("Form has errors at /restaurant/{}/edit", restaurantId);
            return editRestaurant(loggedUser, restaurantId, form);
        }
        // Should be if it got here, 
        // but it doesn't hurt to escape a potential null pointer exception
        if (loggedUser != null) {
            LOGGER.debug("Updating restaurant for user {}", loggedUser.getName());
            List<Tags> tagList = Arrays.asList(form.getTags()).stream().map((i) -> Tags.valueOf(i)).collect(Collectors.toList());
            LOGGER.debug("tags: {}", tagList);

            final Restaurant restaurant = restaurantService
                .updateRestaurant(restaurantId, form.getName(), form.getAddress(), form.getPhoneNumber(), tagList)
                .orElseThrow(RestaurantNotFoundException::new);

            if (form.getProfileImage() != null && !form.getProfileImage().isEmpty()) {
                try {
                Image image = new Image(form.getProfileImage().getBytes());
                restaurantService.setImageByRestaurantId(image, restaurant.getId());
                } catch (IOException e) {
                    LOGGER.error("error while setting restaurant profile image");
                }
            }

            return new ModelAndView("redirect:/restaurant/" + restaurant.getId());
        }
        return new ModelAndView("redirect:/403");
    }

    // Delete restaurant
        
    @RequestMapping(path = { "/restaurant/{restaurantId}/delete" }, method = RequestMethod.POST)
    public ModelAndView deleteRestaurant(@ModelAttribute("loggedUser") final User loggedUser, 
            @PathVariable("restaurantId") final long restaurantId) {

        if (loggedUser != null) {
            if(userService.isTheRestaurantOwner(loggedUser.getId(), restaurantId)){
                restaurantService.deleteRestaurantById(restaurantId);
                if(userService.isRestaurantOwner(loggedUser.getId())){
                    updateAuthorities(loggedUser);
                }
                return new ModelAndView("redirect:/restaurants/user/" + loggedUser.getId());
            }
        }
        return new ModelAndView("redirect:/403");

    }

    // Manage Reservations

    // @RequestMapping(path={"/restaurant/{restaurantId}/manage/pending"}, method = RequestMethod.GET)
    // public ModelAndView manageRestaurantPending(
            // @ModelAttribute("loggedUser") final User loggedUser,
            // @PathVariable("restaurantId") final long restaurantId,
            // @RequestParam(defaultValue = "1") Integer page) {

        // if (loggedUser != null) {
            // final ModelAndView mav =  new ModelAndView("managePendingReservations");
            // Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
            // if(restaurant.isPresent()){
                // if(restaurant.get().getUserId() != loggedUser.getId()){
                    // return new ModelAndView("redirect:/403");
                // }
                // int maxPages = reservationService.findPendingByRestaurantPageCount(AMOUNT_OF_RESERVATIONS, restaurantId);
                // if(page == null || page <1) {
                    // page=1;
                // }else if (page > maxPages) {
                    // page = maxPages;
                // }
                // mav.addObject("restaurant", restaurant.get());

                // List<Reservation> pendingReservations = reservationService.findPendingByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId);
                // if(pendingReservations.isEmpty()){ mav.addObject("restaurantHasPendingReservations", false); }
                // else { mav.addObject("restaurantHasPendingReservations", true); }

                // mav.addObject("maxPages", maxPages);
                // mav.addObject("pendingReservations", pendingReservations);

                // return mav;
            // }
            // else{
                // return new ModelAndView("redirect:/404");
            // }
        // }
        // return new ModelAndView("redirect:/403");
    // }

    // @RequestMapping(path={"/restaurant/{restaurantId}/manage/confirmed"}, method = RequestMethod.GET)
    // public ModelAndView manageRestaurantConfirmed(
            // @ModelAttribute("loggedUser") final User loggedUser,
            // @PathVariable("restaurantId") final long restaurantId,
            // @RequestParam(defaultValue = "1") Integer page) {
        // if (loggedUser != null) {
            // final ModelAndView mav =  new ModelAndView("manageConfirmedReservations");
            // Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
            // if(restaurant.isPresent()){
                // if(restaurant.get().getUserId() != loggedUser.getId()){
                    // return new ModelAndView("redirect:/403");
                // }
                // int maxPages = reservationService.findConfirmedByRestaurantPageCount(AMOUNT_OF_RESERVATIONS, restaurantId);
                // if(page == null || page <1) {
                    // page=1;
                // }else if (page > maxPages) {
                    // page = maxPages;
                // }
                // mav.addObject("restaurant", restaurant.get());

                // With Pagination
                // List<Reservation> confirmedReservations = reservationService.findConfirmedByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId);

                // if(confirmedReservations.isEmpty()){ mav.addObject("restaurantHasConfirmedReservations", false); }
                // else { mav.addObject("restaurantHasConfirmedReservations", true); }

                // mav.addObject("maxPages", maxPages);
                // mav.addObject("confirmedReservations", confirmedReservations);

                // return mav;
            // }
            // else{
                // return new ModelAndView("redirect:/404");
            // }
        // }
        // return new ModelAndView("redirect:/403");
    // }



    public void updateAuthorities(@ModelAttribute("loggedUser") final User loggedUser) {
        if(loggedUser!=null){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            if(userService.isRestaurantOwner(loggedUser.getId())){
                authorities.add(new SimpleGrantedAuthority("ROLE_RESTAURANTOWNER"));
            }
            Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), authorities);
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
    }

}
