package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;

import javax.validation.Valid;


import ar.edu.itba.paw.service.MenuService;
import ar.edu.itba.paw.service.ReservationService;

import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.forms.MenuItemForm;
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


import java.util.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Controller
public class RestaurantController {


    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantController.class);
    private static final int AMOUNT_OF_MENU_ITEMS = 5;
    private static final int AMOUNT_OF_RESTAURANTS = 10;
    private static final int AMOUNT_OF_RESERVATIONS = 2;

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private MenuService menuService;

    @RequestMapping(path = { "/restaurant/{restaurantId}" }, method = RequestMethod.GET)
    public ModelAndView restaurant(@ModelAttribute("loggedUser") final User loggedUser,
            @ModelAttribute("reservationForm") final ReservationForm form,
            @ModelAttribute("menuItemForm") final MenuItemForm menuForm,
            @RequestParam(defaultValue="1") Integer page,
            @PathVariable("restaurantId") final long restaurantId) {
        final ModelAndView mav = new ModelAndView("restaurant");

        int maxPages = restaurantService.findByIdWithMenuPagesCount(AMOUNT_OF_MENU_ITEMS, restaurantId);

        if(page == null || page <1) {
            page=1;
        }else if (page > maxPages) {
            page = maxPages;
        }
        mav.addObject("maxPages", maxPages);

        if(loggedUser != null){
            Optional<Rating> userRating = ratingService.getRating(loggedUser.getId(), restaurantId);
            boolean isTheRestaurantOwner = userService.isTheRestaurantOwner(loggedUser.getId(), restaurantId);
            if (isTheRestaurantOwner) {
                mav.addObject("isTheOwner", true);
            }
            if(userRating.isPresent()){
                mav.addObject("rated", true);
                mav.addObject("userRatingToRestaurant", userRating.get().getRating());
            }
            mav.addObject("userLikesRestaurant", likesService.userLikesRestaurant(loggedUser.getId(), restaurantId));
            List<String> times = restaurantService.availableStringTime(restaurantId);
            /*List<String> times = Arrays.asList("19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30");*/
            mav.addObject("times", times);
        }

        LOGGER.error("page value: {}", page);
        mav.addObject("restaurant",
                restaurantService.findByIdWithMenu(restaurantId, page, AMOUNT_OF_MENU_ITEMS).orElseThrow(RestaurantNotFoundException::new));
        return mav;
    }

    @RequestMapping(path = { "/restaurant/{restaurantId}" }, method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute("loggedUser") final User loggedUser,
            @Valid @ModelAttribute("reservationForm") final ReservationForm form,
            final BindingResult errors,
             @ModelAttribute("menuItemForm") final MenuItemForm menuForm,
            final BindingResult menuErrors, 
            @RequestParam(defaultValue="1") Integer page,
            @PathVariable("restaurantId") final long restaurantId,
            RedirectAttributes redirectAttributes) {

        LocalTime time;
        if (form != null && errors != null) {
            /*time = LocalTime.parse(form.getTime());*/
            time = form.getTime();
            int currentHours = LocalTime.now().getHour();
            if (time.getHour() <= currentHours) {
                errors.rejectValue("time",
                                    "reservationForm.time",
                                    "Selected hour has already passed");
            }
            // Range validator
            if(!restaurantService.availableTime(restaurantId).contains(time)){
                errors.rejectValue("time",
                        "reservationForm.time",
                        "Select an hour from list");
            }
        }
        if (errors.hasErrors()) {
            return restaurant(loggedUser, form, menuForm,page, restaurantId);
        }

        if (loggedUser != null) {
            /*time = LocalTime.parse(form.getTime());*/
            time = form.getTime();
            LocalDateTime todayAtDate = LocalDate.now().atTime(time.getHour(), time.getMinute());
            /*Date date = Date.from(todayAtDate.atZone(ZoneId.systemDefault()).toInstant());*/
            reservationService.addReservation(loggedUser.getId(), restaurantId, todayAtDate, Long.parseLong(form.getQuantity()));
            redirectAttributes.addFlashAttribute("madeReservation", true);
        } else {
            return new ModelAndView("redirect:/register");
        }

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(path = { "/restaurant/{restaurantId}/menu" }, method = RequestMethod.POST)
    public ModelAndView addMenu(@ModelAttribute("loggedUser") final User loggedUser,
             @ModelAttribute("reservationForm") final ReservationForm form,
             @Valid @ModelAttribute("menuItemForm") final MenuItemForm menuForm,
             final BindingResult errors, 
             @RequestParam(defaultValue="1") Integer page,
             @PathVariable("restaurantId") final long restaurantId,
             RedirectAttributes redirectAttributes) {

        if(errors.hasErrors()) {
            return restaurant(loggedUser, form, menuForm, page, restaurantId);
        }
        if (loggedUser != null) {
            boolean isTheRestaurantOwner = userService.isTheRestaurantOwner(loggedUser.getId(), restaurantId);
            if (isTheRestaurantOwner) {
                LOGGER.debug("{} is the owner at /restaurant/{}", loggedUser.getName(), restaurantId );
                MenuItem item = new MenuItem(
                        menuForm.getName(),
                        menuForm.getDescription(),
                        menuForm.getPrice());
                menuService.addItemToRestaurant(restaurantId, item);
                LOGGER.debug("Owner added restaurant");
            return new ModelAndView("redirect:/restaurant/" + restaurantId);
            }
        }
        return new ModelAndView("redirect:/login");
    }
    

    @RequestMapping(path = { "/register/restaurant" }, method = RequestMethod.GET)
    public ModelAndView registerRestaurant( @ModelAttribute("loggedUser") final User loggedUser, @ModelAttribute("RestaurantForm") final RestaurantForm form) {

        ModelAndView mav =  new ModelAndView("registerRestaurant");
        mav.addObject("tags", Tags.allTags());

        return mav;
    }



    @RequestMapping(path = { "/register/restaurant" }, method = RequestMethod.POST)
    public ModelAndView registerRestaurant( @ModelAttribute("loggedUser") final User loggedUser, @Valid @ModelAttribute("RestaurantForm") final RestaurantForm form, final BindingResult errors) {

        if (form.getTags().length>3) {
            errors.rejectValue("tags", "restaurant.edit.tagsLimit");
        }

        if (errors.hasErrors()) {
            LOGGER.debug("Form has errors at /register/restaurant");
            return registerRestaurant(loggedUser, form);
        }
        if(loggedUser != null){
            LOGGER.debug("Creating restaurant for user {}", loggedUser.getName());
            final Restaurant restaurant = restaurantService.registerRestaurant(form.getName(), form.getAddress(),
                    form.getPhoneNumber(), 0, loggedUser.getId());
            updateAuthorities(loggedUser);
            if (form.getProfileImage() != null && !form.getProfileImage().isEmpty()) {
                try {
                Image image = new Image(form.getProfileImage().getBytes());
                restaurantService.setImageByRestaurantId(image, restaurant.getId());
                } catch (IOException e) {
                    LOGGER.error("error while setting restaurant profile image");
                }
            }
            for( Integer i :form.getTags()){
                restaurantService.addTag(restaurant.getId(),Tags.valueOf(i));
            }


            return new ModelAndView("redirect:/restaurant/" + restaurant.getId());
        }
        return new ModelAndView("redirect:/login");
    }



    @RequestMapping(path = {"/restaurant/{restaurantId}/rate"}, method = RequestMethod.POST)
    public ModelAndView setRating(@ModelAttribute("loggedUser") final User loggedUser, @PathVariable("restaurantId") final long restaurantId, @RequestParam("rating") int rating){
        if(loggedUser != null){
            long userId = loggedUser.getId();
            ratingService.rateRestaurant(userId, restaurantId, rating);
        }
        return new ModelAndView("redirect:/restaurant/" + restaurantId);
    }

    @RequestMapping(path = {"/restaurant/{restaurantId}/rate"}, method = RequestMethod.PUT)
    public ModelAndView updateRating(@ModelAttribute("loggedUser") final User loggedUser, @PathVariable("restaurantId") final long restaurantId, @RequestParam("rating") int rating){
        if(loggedUser != null){
            long userId = loggedUser.getId();
            ratingService.modifyRestaurantRating(userId, restaurantId, rating);
        }
        return new ModelAndView("redirect:/restaurant/" + restaurantId);
    }

    @RequestMapping(path = {"restaurant/{restaurantId}/like"}, method = RequestMethod.POST)
    public ModelAndView like(@ModelAttribute("loggedUser") final User loggedUser, @PathVariable("restaurantId") final long restaurantId){
        if(loggedUser != null){
            long userId = loggedUser.getId();
            likesService.like(userId, restaurantId);
            return new ModelAndView("redirect:/restaurant/" + restaurantId);
        }
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = {"restaurant/{restaurantId}/dislike"}, method = RequestMethod.POST)
    public ModelAndView dislike(@ModelAttribute("loggedUser") final User loggedUser, @PathVariable("restaurantId") final long restaurantId){
        if(loggedUser != null){
            long userId = loggedUser.getId();
            likesService.dislike(userId, restaurantId);
            return new ModelAndView("redirect:/restaurant/" + restaurantId);
        }
        return new ModelAndView("redirect:/login");
    }



    @RequestMapping(path = { "/restaurant/{restaurantId}/delete" })
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
                    List<Integer> tagsChecked = new ArrayList();
                    for( Tags t :restaurantService.tagsInRestaurant(restaurantId)){
                        tagsChecked.add(t.getValue());
                    }
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
            final Restaurant restaurant = restaurantService
                .updateRestaurant(restaurantId, form.getName(), form.getAddress(), form.getPhoneNumber())
                .orElseThrow(RestaurantNotFoundException::new);

            if (form.getProfileImage() != null && !form.getProfileImage().isEmpty()) {
                try {
                Image image = new Image(form.getProfileImage().getBytes());
                restaurantService.setImageByRestaurantId(image, restaurant.getId());
                } catch (IOException e) {
                    LOGGER.error("error while setting restaurant profile image");
                }
            }

            for( Tags t :restaurantService.tagsInRestaurant(restaurantId)){
                restaurantService.removeTag(restaurantId,t);
            }
            for( Integer i :form.getTags()){
                restaurantService.addTag(restaurantId,Tags.valueOf(i));
            }


            return new ModelAndView("redirect:/restaurant/" + restaurant.getId());
        }
        return new ModelAndView("redirect:/403");
    }

    @RequestMapping(path={"/restaurant/{restaurantId}/manage"})
    public ModelAndView manageRestaurant(
            @ModelAttribute("loggedUser") final User loggedUser,
            @PathVariable("restaurantId") final long restaurantId,
            @RequestParam(defaultValue = "1") Integer page) {

        if (loggedUser != null) {
            final ModelAndView mav =  new ModelAndView("manageRestaurant");
            Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
            if(restaurant.isPresent()){
                if(restaurant.get().getUserId() != loggedUser.getId()){
                    return new ModelAndView("redirect:/403");
                }
                int maxPages = reservationService.findByRestaurantPageCount(AMOUNT_OF_RESERVATIONS, restaurantId);
                if(page == null || page <1) {
                    page=1;
                }else if (page > maxPages) {
                    page = maxPages;
                }
                mav.addObject("restaurant", restaurant.get());

                // With Pagination
                /*List<Reservation> confirmedReservations = reservationService.findConfirmedByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId);
                List<Reservation> pendingReservations = reservationService.findPendingByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId);*/

                // Without pagination
                HashMap<String, List<Reservation>> reservations = reservationService.findByRestaurantByConfirmation(restaurantId);
                List<Reservation> confirmedReservations = reservations.get("confirmed");
                List<Reservation> pendingReservations = reservations.get("pending");

                //List<Reservation> reservations = reservationService.findByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId);
                if(confirmedReservations.isEmpty()){ mav.addObject("restaurantHasConfirmedReservations", false); }
                else { mav.addObject("restaurantHasConfirmedReservations", true); }

                if(pendingReservations.isEmpty()){ mav.addObject("restaurantHasPendingReservations", false); }
                else { mav.addObject("restaurantHasPendingReservations", true); }

                mav.addObject("maxPages", maxPages);
                mav.addObject("confirmedReservations", confirmedReservations);
                mav.addObject("pendingReservations", pendingReservations);

                return mav;
            }
            else{
                return new ModelAndView("redirect:/404");
            }
        }
        return new ModelAndView("redirect:/403");
    }


    @RequestMapping("/restaurants/user/{userId}")
    public ModelAndView userRestaurants(
            @ModelAttribute("loggedUser") final User loggedUser,
            @PathVariable("userId") final long userId,
            @RequestParam(defaultValue = "1") Integer page) {

        // Shouldn't be able to get here unless logged it but just in case
        if(loggedUser != null){
            final ModelAndView mav = new ModelAndView("myRestaurants");
            int maxPages = restaurantService.getRestaurantsFromOwnerPagesCount(AMOUNT_OF_RESTAURANTS, userId);

            if(page == null || page <1) {
                page=1;
            }else if (page > maxPages) {
                page = maxPages;
            }
            mav.addObject("maxPages", maxPages);
            List<Restaurant> restaurants = restaurantService.getRestaurantsFromOwner(page, AMOUNT_OF_RESTAURANTS, userId);
            mav.addObject("userHasRestaurants", !restaurants.isEmpty());
            mav.addObject("restaurants", restaurants);
            return mav;
        }
        return new ModelAndView("redirect:/403");
    }



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
