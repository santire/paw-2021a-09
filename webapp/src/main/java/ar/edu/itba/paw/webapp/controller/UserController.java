package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import ar.edu.itba.paw.model.exceptions.EmptyBodyException;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.forms.PasswordResetForm;
import ar.edu.itba.paw.webapp.forms.RegisterUserForm;
import ar.edu.itba.paw.webapp.forms.UpdateUserForm;
import ar.edu.itba.paw.webapp.utils.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Path("users")
@Component
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static final int AMOUNT_OF_RESTAURANTS = 10;
    private static final int AMOUNT_OF_RESERVATIONS = 10;
    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private LikesService likesService;
    @Autowired
    private RatingService ratingService;

    @Context
    private UriInfo uriInfo;

    // CREATE USER
    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response registerUser(@QueryParam("email") final String forgotEmail, @Valid final RegisterUserForm userForm, @Context HttpServletRequest request) throws EmailInUseException, TokenCreationException {
        String baseUrl = request.getHeader("Origin");
        if (baseUrl == null) {
            baseUrl = uriInfo.getBaseUri().toString();
        }

        if (forgotEmail != null && !forgotEmail.isEmpty()) {
            userService.requestPasswordReset(forgotEmail, baseUrl);
            return Response.status(Response.Status.ACCEPTED).build();
        }

        if (userForm == null) {
            throw new EmptyBodyException();
        }

        final User user = userService.register(userForm.getUsername(), userForm.getPassword(), userForm.getFirstName(), userForm.getLastName(), userForm.getEmail(), userForm.getPhone(), baseUrl);

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(user.getId())).build();
        LOGGER.info("user created: {}", uri);
        return Response.created(uri).build();
    }

    @PUT
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response activateOrReset(@QueryParam("token") final String token, @QueryParam("type") final String type, @Valid PasswordResetForm passwordForm, @Context HttpServletRequest request) {
        if (type != null && type.equalsIgnoreCase("activation")) {
            userService.activateUserByToken(token);
        } else if (type != null && type.equalsIgnoreCase("reset")) {
            if (passwordForm == null) throw new EmptyBodyException();
            userService.updatePasswordByToken(token, passwordForm.getPassword());
        } else {
            // TODO: Exception?
            LOGGER.warn("Invalid type");
        }

        return Response.noContent().build();
    }

    // UPDATE USER
    @PUT
    @Path("/{userId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isUser(#userId)")
    public Response updateUser(@PathParam("userId") final Long userId, @Valid UpdateUserForm userForm, @Context HttpServletRequest request) {
        userService.updateUser(userId, userForm.getPassword(), userForm.getFirstName(), userForm.getLastName(), userForm.getPhone());
        return Response.noContent().build();
    }


    // READ USER
    @GET
    @Path("/{userId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
//    @PreAuthorize("@authComponent.isUser(#userId)")
    public Response getUser(@PathParam("userId") final Long userId, @Context HttpServletRequest request) {
        final User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        return Response.ok(UserDto.fromUser(user, request.getRequestURL().toString(), uriInfo)).build();

    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isUserByEmail(#email)")
    public Response getUserByEmail(@QueryParam("email") final String email, @Context HttpServletRequest request) {
        final User user = userService.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return Response.ok(UserDto.fromUser(user, request.getRequestURL().toString(), uriInfo)).build();
    }

    //READ USER RESTAURANTS
    @GET
    @Path("/{userId}/restaurants")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isUser(#userId)")
    public Response getUserRestaurants(@PathParam("userId") final Long userId,
                                       @QueryParam("page") @DefaultValue("1") Integer page,
                                       @QueryParam("pageAmount") @DefaultValue("10") Integer pageAmount,
                                       @Context HttpServletRequest request) {
        if(pageAmount > AMOUNT_OF_RESTAURANTS) {
            pageAmount = AMOUNT_OF_RESTAURANTS;
        }
        if(pageAmount <= 0) {
            pageAmount = 1;
        }
        int totalRestaurants = restaurantService.getRestaurantsFromOwnerCount(userId);
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsFromOwner(page, pageAmount, userId).stream().map(u -> RestaurantDto.fromRestaurant(u, uriInfo)).collect(Collectors.toList());
        return PageUtils.paginatedResponse(new GenericEntity<List<RestaurantDto>>(restaurants) {
        }, uriInfo, page, pageAmount, totalRestaurants);
    }

    //READ USER RESERVATIONS
    @GET
    @Path("/{userId}/reservations")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isUser(#userId)")
    public Response getUserReservations(@PathParam("userId") final Long userId, @QueryParam("filterBy") @DefaultValue("") String filterBy, @QueryParam("page") @DefaultValue("1") Integer page, @Context HttpServletRequest request) {
        List<ReservationDto> reservations;
        int totalReservations;
        if (filterBy.equalsIgnoreCase("history")) {
            totalReservations = reservationService.findByUserHistoryCount(userId);
            reservations = reservationService.findByUserHistory(page, AMOUNT_OF_RESERVATIONS, userId).stream().map(u -> ReservationDto.fromReservation(u, uriInfo)).collect(Collectors.toList());
        } else {
            totalReservations = reservationService.findByUserCount(userId);
            reservations = reservationService.findByUser(page, AMOUNT_OF_RESERVATIONS, userId).stream().map(u -> ReservationDto.fromReservation(u, uriInfo)).collect(Collectors.toList());
        }

        return PageUtils.paginatedResponse(new GenericEntity<List<ReservationDto>>(reservations) {
        }, uriInfo, page, AMOUNT_OF_RESERVATIONS, totalReservations);
    }

    //READ USER LIKES
    @GET
    @Path("/{userId}/likes")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isUser(#userId)")
    public Response userLikesRestaurants(@PathParam("userId") final Long userId, @QueryParam("restaurantId") List<Long> restaurantIds, @Context HttpServletRequest request) {
        List<Long> likedIds = likesService.userLikesRestaurants(userId, restaurantIds).stream().map(l -> l.getRestaurant().getId()).collect(Collectors.toList());
        List<LikeDto> likes = new ArrayList<>();
        for (Long id : restaurantIds) {
            LikeDto like = new LikeDto(false, id, userId, request.getRequestURL().toString(), uriInfo);
            if (likedIds.contains(id)) {
                like.setLiked(true);
            }
            likes.add(like);
        }
        return Response.ok(new GenericEntity<List<LikeDto>>(likes) {
        }).build();
    }

    //LIKE RESTAURANT
    @POST
    @Path("/{userId}/likes/{restaurantId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("!@authComponent.isRestaurantOwner(#restaurantId)")
    public Response likeRestaurant(@PathParam("userId") final Long userId,
                                    @PathParam("restaurantId") final Long restaurantId,
                                    @Context HttpServletRequest request) {
        User user = getLoggedUser();
        likesService.like(user.getId(), restaurantId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{userId}/likes/{restaurantId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("!@authComponent.isRestaurantOwner(#restaurantId)")
    public Response dislikeRestaurant(@PathParam("userId") final Long userId,
                                        @PathParam("restaurantId") final Long restaurantId,
                                        @Context HttpServletRequest request) {
        User user = getLoggedUser();
        likesService.dislike(user.getId(), restaurantId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    //READ USER RATING
    @GET
    @Path("/{userId}/ratings")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRestaurantRate(@PathParam("userId") final Long userId,
                                      @QueryParam("restaurantId") final Long restaurantId,
                                      @Context HttpServletRequest request) {

        Optional<Rating> maybeRating = ratingService.getRating(userId, restaurantId);
        Double rate = maybeRating.isPresent() ? maybeRating.get().getRating() : 0;
        return Response.ok(new RatingDto(rate)).build();
    }

    private User getLoggedUser() {
    return userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
            // This shouldn't happen as authority is handled before
            .orElseThrow(() -> new AccessDeniedException("Unauthorized"));
    }
}
