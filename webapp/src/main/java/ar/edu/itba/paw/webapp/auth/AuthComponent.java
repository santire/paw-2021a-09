package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthComponent.class);

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

    public boolean isReservationUser(Long reservationId) {
        User loggedUser = loggedUser();
        Optional<Reservation> maybeReservation = reservationService.findById(reservationId);
        return loggedUser != null
                && maybeReservation.isPresent()
                && loggedUser.getId().equals(maybeReservation.get().getUser().getId());
    }

    public boolean isCommentOwner(Long commentId) {
        User loggedUser = loggedUser();
        Optional<Comment> maybeComment = commentService.findById(commentId);
        return loggedUser != null
                && maybeComment.isPresent()
                && loggedUser.getId().equals(maybeComment.get().getUser().getId());
    }

    public boolean isReservationOwner(Long reservationId) {
        Optional<Reservation> maybeReservation = reservationService.findById(reservationId);
        User loggedUser = loggedUser();

        return loggedUser != null
                && maybeReservation.isPresent()
                && loggedUser.getId().equals(maybeReservation.get().getRestaurant().getOwner().getId());
    }

    public boolean isRestaurantOwner(Long restaurantId) {
        User loggedUser = loggedUser();
        if (loggedUser == null) {
            return false;
        }
        LOGGER.debug("Logged user: {} ", loggedUser.getId());
        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        LOGGER.debug("Restaurant id: {} ", restaurantId);
        LOGGER.debug("Owner id: {} ", restaurant.getOwner().getId());
        return loggedUser.getId().equals(restaurant.getOwner().getId());
    }


    public boolean isRestaurantAndMenuOwner(Long restaurantId, Long menuItemId) {
        User loggedUser = loggedUser();
        if (loggedUser == null) {
            return false;
        }
        return userService.isTheRestaurantOwner(loggedUser.getId(), restaurantId)
                && menuService.menuBelongsToRestaurant(menuItemId, restaurantId);
    }

    public boolean isUser(Long userId) {
        User loggedUser = loggedUser();
        if (loggedUser == null) {
            return false;
        }
        LOGGER.debug("logged user: {}", loggedUser.getId());
        LOGGER.debug("path id: {}", userId);
        return loggedUser.getId().equals(userId);
    }

    public boolean isUserByEmail(String email) {
        User loggedUser = loggedUser();
        if (loggedUser == null) {
            return false;
        }
        return loggedUser.getEmail().equalsIgnoreCase(email);
    }

    private User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> maybeUser = userService.findByEmail(auth.getName());

        User user = maybeUser.orElse(null);
        if (user != null) {
            LOGGER.debug("Logged user is {}", user.getId());
        } else {
            LOGGER.debug("No logged user");
        }

        return user;
    }
}

