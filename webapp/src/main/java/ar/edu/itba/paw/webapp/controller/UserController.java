package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.model.Sorting;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.LikesService;
import ar.edu.itba.paw.service.ReservationService;
import ar.edu.itba.paw.service.RestaurantService;
import ar.edu.itba.paw.webapp.dto.ReservationDto;
import ar.edu.itba.paw.webapp.dto.RestaurantDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


import ar.edu.itba.paw.service.UserService;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.net.URI;
import java.util.stream.Collectors;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;


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
    private LikesService likesService;
    @Autowired
    private ReservationService reservationService;

    @Context
    private UriInfo uriInfo;

    // UPDATE USER
    @PUT
    @Path("/edit")
    @Produces(value = { MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response updateUser(final UserDto userDto, @Context HttpServletRequest request) {
        try {
            userService.updateUser(userDto.getUserId(), userDto.getUsername(),userDto.getPassword(), userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getPhone());
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.CONFLICT).header("error", "user does not exist").build();
        }
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(userDto.getUserId())).build();
        LOGGER.info("user updated: {}", uri);
        return Response.created(uri).build();
    }



    //READ USER
    @GET
    @Path("/{userId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUser(@PathParam("userId") final int userId, @Context HttpServletRequest request) {
        final Optional<User> user = userService.findById(userId);
        if(user.isPresent()){
            return Response.ok(UserDto.fromUser(user.get(), request.getRequestURL().toString(), uriInfo)).build();
        } else {
            return Response.status(Response.Status.ACCEPTED).header("error", "user not found").build();
        }
    }

    //READ USER RESTAURANTS
    @GET
    @Path("/restaurants")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUserRestaurants(@QueryParam("page") @DefaultValue("1") Integer page, @Context HttpServletRequest request) {
        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("Anon user attempt to get restaurants");
            return Response.status(Response.Status.OK).header("error", "error user not logged").build();
        }
        Long userId = user.get().getId();
        int maxPages = restaurantService.getRestaurantsFromOwnerPagesCount(AMOUNT_OF_RESTAURANTS,userId);
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsFromOwner(page, AMOUNT_OF_RESTAURANTS, userId).stream().map(u -> RestaurantDto.fromRestaurant(u, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<RestaurantDto>>(restaurants){})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", maxPages).build(), "last")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.max((page - 1), 1)).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.min((page + 1), maxPages)).build(), "next")
                .build();
    }

    //READ USER RESERVATIONS
    @GET
    @Path("/reservations")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getLikedRestaurants(@QueryParam("page") @DefaultValue("1") Integer page, @Context HttpServletRequest request) {
        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("Anon user attempt to see reservations");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }
        Long userId = user.get().getId();
        int maxPages = reservationService.findByUserPageCount(AMOUNT_OF_RESERVATIONS, userId);
        List<ReservationDto> reservation = reservationService.findByUser(page, AMOUNT_OF_RESERVATIONS, userId).stream().map(u -> ReservationDto.fromReservation(u, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<ReservationDto>>(reservation){})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", maxPages).build(), "last")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.max((page - 1), 1)).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.min((page + 1), maxPages)).build(), "next")
                .build();
    }

    @ModelAttribute("loggedUser")
    public Optional<User> getLoggedUser(HttpServletRequest request){
        LOGGER.info("USER: " + SecurityContextHolder.getContext().getAuthentication().getName());
        return userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());    }

}
