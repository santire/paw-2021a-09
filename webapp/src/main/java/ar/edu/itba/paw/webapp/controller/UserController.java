package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import ar.edu.itba.paw.model.exceptions.EmptyBodyException;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.service.RatingService;
import ar.edu.itba.paw.service.RestaurantService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.RestaurantDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.forms.PasswordResetForm;
import ar.edu.itba.paw.webapp.forms.RegisterUserForm;
import ar.edu.itba.paw.webapp.forms.UpdateUserForm;
import ar.edu.itba.paw.webapp.utils.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@Path("users")
@Component
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static final int AMOUNT_OF_RESTAURANTS = 10;

    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;
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
        if (pageAmount > AMOUNT_OF_RESTAURANTS) {
            pageAmount = AMOUNT_OF_RESTAURANTS;
        }
        if (pageAmount <= 0) {
            pageAmount = 1;
        }
        int totalRestaurants = restaurantService.getRestaurantsFromOwnerCount(userId);
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsFromOwner(page, pageAmount, userId).stream().map(u -> RestaurantDto.fromRestaurant(u, uriInfo)).collect(Collectors.toList());
        return PageUtils.paginatedResponse(new GenericEntity<List<RestaurantDto>>(restaurants) {
        }, uriInfo, page, pageAmount, totalRestaurants);
    }

}
