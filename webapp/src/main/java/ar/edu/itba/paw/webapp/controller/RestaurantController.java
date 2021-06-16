package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;

import javax.validation.Valid;


import ar.edu.itba.paw.service.MenuService;
import ar.edu.itba.paw.service.ReservationService;

import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.forms.CommentForm;
import ar.edu.itba.paw.webapp.forms.MenuItemForm;
import ar.edu.itba.paw.webapp.forms.RatingForm;
import ar.edu.itba.paw.webapp.forms.ReservationForm;
import ar.edu.itba.paw.webapp.forms.RestaurantForm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class RestaurantController {


    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantController.class);
    private static final int AMOUNT_OF_MENU_ITEMS = 8;
    private static final int AMOUNT_OF_RESTAURANTS = 10;
    private static final int AMOUNT_OF_REVIEWS = 4;

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

    @Autowired
    private SocialMediaService socialMediaService;

    @Autowired
    private CommentService commentService;


    @Autowired
    private CommonAttributes ca;





    @RequestMapping(path = { "/restaurant/{restaurantId}" }, method = RequestMethod.GET)
    public ModelAndView restaurant(@ModelAttribute("reservationForm") final ReservationForm form,
            @ModelAttribute("menuItemForm") final MenuItemForm menuForm,
            @ModelAttribute("ratingForm") final RatingForm ratingForm,
            @RequestParam(defaultValue="1") Integer page,
            @PathVariable("restaurantId") final long restaurantId) {
        final ModelAndView mav = new ModelAndView("restaurant");

        User loggedUser = ca.loggedUser();

        int maxPages = restaurantService.findByIdWithMenuPagesCount(AMOUNT_OF_MENU_ITEMS, restaurantId);

        if(page == null || page <1) {
            page=1;
        }else if (page > maxPages) {
            page = maxPages;
        }
        mav.addObject("maxPages", maxPages);


        Restaurant restaurant = restaurantService.findByIdWithMenu(restaurantId, page, AMOUNT_OF_MENU_ITEMS).orElseThrow(RestaurantNotFoundException::new);

        mav.addObject("facebook", false);
        mav.addObject("instagram", false);
        mav.addObject("twitter", false);


        if(restaurant.getFacebook() != null && !restaurant.getFacebook().isEmpty()){
            mav.addObject("facebook", true);
        }
        if(restaurant.getInstagram() != null && !restaurant.getInstagram().isEmpty()){
            mav.addObject("instagram", true);
        }
        if(restaurant.getTwitter() != null && !restaurant.getTwitter().isEmpty()){
            mav.addObject("twitter", true);
        }


        if(loggedUser != null){
            Optional<Rating> userRating = ratingService.getRating(loggedUser.getId(), restaurantId);
            boolean isTheRestaurantOwner = userService.isTheRestaurantOwner(loggedUser.getId(), restaurantId);
            if (isTheRestaurantOwner) {
                mav.addObject("isTheOwner", true);
            }

            mav.addObject("userRatingToRestaurant", 0);


            if(userRating.isPresent()){
                mav.addObject("rated", true);
                mav.addObject("userRatingToRestaurant", userRating.get().getRating());
            }

            mav.addObject("userLikesRestaurant", likesService.userLikesRestaurant(loggedUser.getId(), restaurantId));
            List<String> times = restaurantService.availableStringTime(restaurantId);
            mav.addObject("times", times);
        }


        LOGGER.error("page value: {}", page);
        mav.addObject("restaurant", restaurant);

        return mav;
    }

    @RequestMapping(path = { "/restaurant/{restaurantId}" }, method = RequestMethod.POST)
    public ModelAndView reservationAndMenu(@Valid @ModelAttribute("reservationForm") final ReservationForm form,
                                           final BindingResult errors,
                                           @ModelAttribute("menuItemForm") final MenuItemForm menuForm,
                                           final BindingResult menuErrors,
                                           @ModelAttribute("ratingForm") final RatingForm ratingForm,
                                           final BindingResult ratingError,
                                           @RequestParam(defaultValue="1") Integer page,
                                           @PathVariable("restaurantId") final long restaurantId,
                                           RedirectAttributes redirectAttributes) {

        User loggedUser = ca.loggedUser();
        if (errors.hasErrors()) {
            return restaurant(form, menuForm, ratingForm, page, restaurantId);
        }

        if (loggedUser != null) {
            LocalTime time = form.getTime();
            LocalDate date = form.getDate();
            LocalDateTime dateAt = date.atTime(time.getHour(), time.getMinute());
            reservationService.addReservation(loggedUser.getId(), restaurantId, dateAt, Long.parseLong(form.getQuantity()), ca.getUri());
            redirectAttributes.addFlashAttribute("madeReservation", true);
        } else {
            return new ModelAndView("redirect:/login");
        }

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(path = { "/restaurant/{restaurantId}/reviews" }, method = RequestMethod.GET)
    public ModelAndView restaurantReviews(@ModelAttribute("reservationForm") final ReservationForm form,
                                          @ModelAttribute("menuItemForm") final MenuItemForm menuForm,
                                   @ModelAttribute("ratingForm") final RatingForm ratingForm,
                                   @ModelAttribute("commentForm") final CommentForm commentForm,
                                   @RequestParam(defaultValue="1") Integer page,
                                   @PathVariable("restaurantId") final long restaurantId) {
        final ModelAndView mav = new ModelAndView("restaurantReviews");
        User loggedUser = ca.loggedUser();
        //int maxPages = restaurantService.findByIdWithMenuPagesCount(AMOUNT_OF_MENU_ITEMS, restaurantId);
        int maxPages = commentService.findByRestaurantPageCount(AMOUNT_OF_REVIEWS, restaurantId);

        if(page == null || page <1) {
            page=1;
        }else if (page > maxPages) {
            page = maxPages;
        }
        mav.addObject("maxPages", maxPages);

        Restaurant restaurant = restaurantService.findByIdWithMenu(restaurantId, page, AMOUNT_OF_MENU_ITEMS).orElseThrow(RestaurantNotFoundException::new);

        mav.addObject("facebook", false);
        mav.addObject("instagram", false);
        mav.addObject("twitter", false);


        if(restaurant.getFacebook() != null && !restaurant.getFacebook().isEmpty()){
            mav.addObject("facebook", true);
        }
        if(restaurant.getInstagram() != null && !restaurant.getInstagram().isEmpty()){
            mav.addObject("instagram", true);
        }
        if(restaurant.getTwitter() != null && !restaurant.getTwitter().isEmpty()){
            mav.addObject("twitter", true);
        }

        if(loggedUser != null){
            Optional<Rating> userRating = ratingService.getRating(loggedUser.getId(), restaurantId);
            boolean isTheRestaurantOwner = userService.isTheRestaurantOwner(loggedUser.getId(), restaurantId);
            if (isTheRestaurantOwner) {
                mav.addObject("isTheOwner", true);
            }

            mav.addObject("userRatingToRestaurant", 0);

            if(userRating.isPresent()){
                mav.addObject("rated", true);
                mav.addObject("userRatingToRestaurant", userRating.get().getRating());
            }

            mav.addObject("userLikesRestaurant", likesService.userLikesRestaurant(loggedUser.getId(), restaurantId));
            List<String> times = restaurantService.availableStringTime(restaurantId);
            mav.addObject("times", times);

            Optional<Comment> maybeComment = commentService.findByUserAndRestaurantId(loggedUser.getId(), restaurantId);
            if(maybeComment.isPresent()){
                mav.addObject("userMadeComment", true);
                mav.addObject("userReview", maybeComment.get());
            }
            else{
                mav.addObject("userMadeComment", false);
            }
            mav.addObject("hasOnceReserved", true);
        }

        LOGGER.error("page value: {}", page);
        mav.addObject("restaurant", restaurant);
        mav.addObject("reviews", commentService.findByRestaurant(page, AMOUNT_OF_REVIEWS, restaurantId));
        return mav;
    }

    @RequestMapping(path = { "/restaurant/{restaurantId}/reviews" }, method = RequestMethod.POST)
    public ModelAndView addRestaurantReview(@PathVariable("restaurantId") final long restaurantId,
                                            @Valid @ModelAttribute("commentForm") final CommentForm commentForm,
                                            final BindingResult errors,
                                            @ModelAttribute("reservationForm") final ReservationForm form,
                                            @ModelAttribute("menuItemForm") final MenuItemForm menuForm,
                                            @ModelAttribute("ratingForm") final RatingForm ratingForm,
                                            @RequestParam(defaultValue="1") Integer page
                                            ) {
        if(errors.hasErrors()) {
            return(restaurantReviews(form, menuForm, ratingForm, commentForm, page, restaurantId));
        }
        User loggedUser = ca.loggedUser();
        if (loggedUser == null) {
            throw new UserNotFoundException();
        }
        commentService.addComment(loggedUser.getId(), restaurantId, commentForm.getReview());
        return new ModelAndView("redirect:/restaurant/" + restaurantId + "/reviews");
    }

    @RequestMapping(path = { "/restaurant/{restaurantId}/reviews/{reviewId}/delete" }, method = RequestMethod.POST)
    @PreAuthorize("@authComponent.isReviewOwner(#reviewId)")
    public ModelAndView deleteReview(@PathVariable("restaurantId") final long restaurantId,
                                     @PathVariable("reviewId") final long reviewId) {
        commentService.deleteComment(reviewId);
        return new ModelAndView("redirect:/restaurant/" + restaurantId + "/reviews");
    }


    @RequestMapping(path = { "/restaurant/{restaurantId}/menu" }, method = RequestMethod.POST)
    @PreAuthorize("@authComponent.isRestaurantOwner(#restaurantId)")
    public ModelAndView addMenu(@ModelAttribute("reservationForm") final ReservationForm form,
             @Valid @ModelAttribute("menuItemForm") final MenuItemForm menuForm,
             final BindingResult errors,
             @ModelAttribute("ratingForm") final RatingForm ratingForm,
             final BindingResult ratingErrors,
             @RequestParam(defaultValue="1") Integer page,
             @PathVariable("restaurantId") final long restaurantId,
             RedirectAttributes redirectAttributes) {
        if(errors.hasErrors()) {
            return restaurant(form, menuForm, ratingForm, page, restaurantId);
        }
        MenuItem item = new MenuItem(
                menuForm.getName(),
                menuForm.getDescription(),
                menuForm.getPrice());
        menuService.addItemToRestaurant(restaurantId, item);
        return new ModelAndView("redirect:/restaurant/" + restaurantId);
    }
    

    @RequestMapping(path = { "/register/restaurant" }, method = RequestMethod.GET)
    public ModelAndView registerRestaurant(@ModelAttribute("RestaurantForm") final RestaurantForm form) {

        ModelAndView mav =  new ModelAndView("registerRestaurant");
        mav.addObject("tags", Tags.allTags());
        return mav;
    }



    @RequestMapping(path = { "/register/restaurant" }, method = RequestMethod.POST)
    public ModelAndView registerRestaurant(@Valid @ModelAttribute("RestaurantForm") final RestaurantForm form, final BindingResult errors) {

        User loggedUser = ca.loggedUser();

        if (errors.hasErrors()) {
            LOGGER.debug("Form has errors at /register/restaurant");
            return registerRestaurant(form);
        }
        LOGGER.debug("Creating restaurant for user {}", loggedUser.getName());
        List<Tags> tagList = Arrays.asList(form.getTags()).stream().map((i) -> Tags.valueOf(i)).collect(Collectors.toList());
        LOGGER.debug("tags: {}", tagList);
        final Restaurant restaurant = restaurantService.registerRestaurant(form.getName(), form.getAddress(),
                form.getPhoneNumber(), tagList, loggedUser);
        updateAuthorities();

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

    @RequestMapping(path = {"restaurant/{restaurantId}/like"}, method = RequestMethod.POST)
    public ModelAndView like(@PathVariable("restaurantId") final long restaurantId){
        User loggedUser = ca.loggedUser();
        long userId = loggedUser.getId();
        likesService.like(userId, restaurantId);
        return new ModelAndView("redirect:/restaurant/" + restaurantId);
    }

    @RequestMapping(path = {"restaurant/{restaurantId}/dislike"}, method = RequestMethod.POST)
    public ModelAndView dislike(@PathVariable("restaurantId") final long restaurantId){
        User loggedUser = ca.loggedUser();
        long userId = loggedUser.getId();
        likesService.dislike(userId, restaurantId);
        return new ModelAndView("redirect:/restaurant/" + restaurantId);
    }

    @RequestMapping(path ={  "/restaurant/{restaurantId}/delete/{menuId}" }, method=RequestMethod.POST)
    @PreAuthorize("@authComponent.isRestaurantAndMenuOwner(#restaurantId, #menuId)")
    public ModelAndView deleteMenuItem(@PathVariable("restaurantId") final long restaurantId,
            @PathVariable("menuId") final long menuId) {

        menuService.deleteItemById(menuId);
        return new ModelAndView("redirect:/restaurant/" + restaurantId);
    }


    @RequestMapping(path={ "/restaurants/user/{userId}" }, method=RequestMethod.GET)
    public ModelAndView userRestaurants(@PathVariable("userId") final long userId,
            @RequestParam(defaultValue = "1") Integer page) {

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




    @RequestMapping(path = { "/restaurant/{restaurantId}/rate" }, method = RequestMethod.POST)
    public ModelAndView rateRestaurant(@PathVariable("restaurantId") final long restaurantId,
                                        @Valid @ModelAttribute("ratingForm") final RatingForm ratingForm,
                                        final BindingResult errors) {
        User loggedUser = ca.loggedUser();
        if(errors.hasErrors()) {
            return new ModelAndView("redirect:/restaurant/" + restaurantId);
        }
        ratingService.rateRestaurant(loggedUser.getId(), restaurantId, ratingForm.getRating());
        return new ModelAndView("redirect:/restaurant/" + restaurantId);
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
