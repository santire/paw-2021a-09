package ar.edu.itba.paw.webapp.auth;

import java.util.Optional;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.controller.CommonAttributes;

@Component
public class AuthComponent {

    @Autowired
    ReservationService reservationService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    UserService userService;

    @Autowired
    MenuService menuService;

    @Autowired
    CommentService commentService;

    @Autowired
    CommonAttributes ca;

    public boolean isReservationUser(Long reservationId) {
        User loggedUser = ca.loggedUser();
        Optional<Reservation> maybeReservation = reservationService.findById(reservationId);
        return loggedUser != null 
            && maybeReservation.isPresent() 
            && loggedUser.getId().equals(maybeReservation.get().getUser().getId());
    }

    public boolean isReservationOwner(Long reservationId) {
        Optional<Reservation> maybeReservation = reservationService.findById(reservationId);
        User loggedUser = ca.loggedUser();

        return loggedUser != null
            && maybeReservation.isPresent()
            && loggedUser.getId().equals(maybeReservation.get().getRestaurant().getOwner().getId());
    }

    public boolean isRestaurantOwner(Long restaurantId) {
        User loggedUser = ca.loggedUser();
        return userService.isTheRestaurantOwner(loggedUser.getId(), restaurantId);
    }

    public boolean isReviewOwner(Long reviewId) {
        Optional<Comment> maybeReview = commentService.findById(reviewId);
        User loggedUser = ca.loggedUser();

        return loggedUser != null
                && maybeReview.isPresent()
                && loggedUser.getId().equals(maybeReview.get().getUser().getId());
    }

    public boolean isRestaurantAndMenuOwner(Long restaurantId, Long menuItemId) {
        User loggedUser = ca.loggedUser();
        return userService.isTheRestaurantOwner(loggedUser.getId(), restaurantId)
            && menuService.menuBelongsToRestaurant(menuItemId, restaurantId);
    }
}

