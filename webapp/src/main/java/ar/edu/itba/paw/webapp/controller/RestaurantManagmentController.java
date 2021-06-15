package ar.edu.itba.paw.webapp.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import ar.edu.itba.paw.service.SocialMediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private SocialMediaService socialMediaService;

    @Autowired
    private CommonAttributes ca;


    // Edit restaurant
    @RequestMapping(path ={"/restaurant/{restaurantId}/edit"}, method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_RESTAURANTOWNER') and @authComponent.isRestaurantOwner(#restaurantId)")
    public ModelAndView editRestaurant(@PathVariable("restaurantId") final long restaurantId, 
            @ModelAttribute("RestaurantForm") final RestaurantForm form) {


        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
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
        if(form.getFacebook() != null && !form.getFacebook().isEmpty()) {
            restaurant.setFacebook(form.getFacebook());
        }
        if(form.getInstagram() != null && !form.getInstagram().isEmpty()) {
            restaurant.setInstagram(form.getInstagram());
        }
        if(form.getTwitter() != null && !form.getTwitter().isEmpty()) {
            restaurant.setTwitter(form.getTwitter());
        }


        mav.addObject("restaurant", restaurant);
        mav.addObject("tags", Tags.allTags());
        List<Integer> tagsChecked = restaurant.getTags().stream().map(t -> t.getValue()).collect(Collectors.toList());
        mav.addObject("tagsChecked", tagsChecked);


        return mav;
    }

    @RequestMapping(path={"/restaurant/{restaurantId}/edit"}, method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_RESTAURANTOWNER') and @authComponent.isRestaurantOwner(#restaurantId)")
    public ModelAndView editRestaurant(@PathVariable("restaurantId") final long restaurantId, 
            @Valid @ModelAttribute("RestaurantForm") final RestaurantForm form,
            final BindingResult errors) {

        if (errors.hasErrors()) {
            LOGGER.debug("Form has errors at /restaurant/{}/edit", restaurantId);
            return editRestaurant(restaurantId, form);
        }

        List<Tags> tagList = Arrays.asList(form.getTags()).stream().map((i) -> Tags.valueOf(i)).collect(Collectors.toList());
        LOGGER.debug("tags: {}", tagList);

        final Restaurant restaurant = restaurantService
            .updateRestaurant(restaurantId, form.getName(), form.getAddress(), form.getPhoneNumber(), tagList)
            .orElseThrow(RestaurantNotFoundException::new);

            if (form.getFacebook() != null){
                socialMediaService.updateFacebook(form.getFacebook(), restaurant.getId());
            }
            if (form.getInstagram() != null){
                socialMediaService.updateInstagram(form.getInstagram(), restaurant.getId());
            }
            if (form.getTwitter() != null){
                socialMediaService.updateTwitter(form.getTwitter(), restaurant.getId());
            }


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

    // Delete restaurant
        
    @RequestMapping(path = { "/restaurant/{restaurantId}/delete" }, method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_RESTAURANTOWNER') and @authComponent.isRestaurantOwner(#restaurantId)")
    public ModelAndView deleteRestaurant(@PathVariable("restaurantId") final long restaurantId) {
        User loggedUser = ca.loggedUser();
        restaurantService.deleteRestaurantById(restaurantId);
        updateAuthorities();

        return new ModelAndView("redirect:/restaurants/user/" + loggedUser.getId());
    }

    // Manage Reservations

    @RequestMapping(path={"/restaurant/{restaurantId}/manage/pending"}, method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_RESTAURANTOWNER') and @authComponent.isRestaurantOwner(#restaurantId)")
    public ModelAndView manageRestaurantPending(@PathVariable("restaurantId") final long restaurantId,
            @RequestParam(defaultValue = "1") Integer page) {

        final ModelAndView mav =  new ModelAndView("managePendingReservations");
        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);

        int maxPages = reservationService.findPendingByRestaurantPageCount(AMOUNT_OF_RESERVATIONS, restaurantId);
        if(page == null || page <1) {
            page=1;
        }else if (page > maxPages) {
            page = maxPages;
        }
        mav.addObject("restaurant", restaurant);

        List<Reservation> pendingReservations = reservationService.findPendingByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId);
        if(pendingReservations.isEmpty()){ 
            mav.addObject("restaurantHasPendingReservations", false);
        } else{ 
            mav.addObject("restaurantHasPendingReservations", true);
        }

        mav.addObject("maxPages", maxPages);
        mav.addObject("pendingReservations", pendingReservations);

        return mav;
    }

    @RequestMapping(path={"/restaurant/{restaurantId}/manage/confirmed"}, method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_RESTAURANTOWNER') and @authComponent.isRestaurantOwner(#restaurantId)")
    public ModelAndView manageRestaurantConfirmed(
            @PathVariable("restaurantId") final long restaurantId,
            @RequestParam(defaultValue = "1") Integer page) {

        final ModelAndView mav =  new ModelAndView("manageConfirmedReservations");
        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);

        int maxPages = reservationService.findConfirmedByRestaurantPageCount(AMOUNT_OF_RESERVATIONS, restaurantId);
        if(page == null || page <1) {
            page=1;
        }else if (page > maxPages) {
            page = maxPages;
        }
        mav.addObject("restaurant", restaurant);

        // With Pagination
        List<Reservation> confirmedReservations = reservationService.findConfirmedByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId);

        if(confirmedReservations.isEmpty()){ mav.addObject("restaurantHasConfirmedReservations", false); }
        else { mav.addObject("restaurantHasConfirmedReservations", true); }

        mav.addObject("maxPages", maxPages);
        mav.addObject("confirmedReservations", confirmedReservations);

        return mav;
    }



    public void updateAuthorities() {
        User loggedUser = ca.loggedUser();
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
