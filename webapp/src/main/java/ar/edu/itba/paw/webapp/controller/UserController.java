package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.*;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.forms.RegisterUserForm;
import ar.edu.itba.paw.webapp.forms.UpdateUserForm;
import ar.edu.itba.paw.webapp.methods.PATCH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;


@Path(UserController.PATH)
@Component
public class UserController {
    public static final String PATH = "users";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Context
    private UriInfo uriInfo;


    @GET
    @Path("/{userId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isUser(#userId)")
    public Response getUser(@PathParam("userId") final Long userId) {
        final User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();

    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isUserByEmail(#email)")
    public Response getUserByEmail(@QueryParam("email") final String email) {
        final User user = userService.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response registerUser(@QueryParam("email") final String forgotEmail,
                                 @Valid final RegisterUserForm userForm,
                                 @Context HttpServletRequest request) throws EmailInUseException, TokenCreationException {
        final URI baseUri = URI.create(request.getRequestURL().toString()).resolve(request.getContextPath());

        final String baseUsersUrl = uriInfo.getBaseUriBuilder()
                .path(PATH)
                .build().toString();

        if (forgotEmail != null && !forgotEmail.isEmpty()) {
            userService.requestPasswordReset(forgotEmail, baseUsersUrl, baseUri);
            return Response.status(Response.Status.ACCEPTED).build();
        }

        if (userForm == null) {
            throw new EmptyBodyException();
        }

        final User user = userService.register(userForm.getUsername(),
                userForm.getPassword(),
                userForm.getFirstName(),
                userForm.getLastName(),
                userForm.getEmail(),
                userForm.getPhone(), baseUsersUrl, baseUri);

        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(user.getId()))
                .build();

        LOGGER.info("user created: {}", uri);
        return Response.created(uri).build();
    }

    @PATCH
    @Path("/{userId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isValidToken(#userForm.token) or @authComponent.isUser(#userId)")
    public Response updateUser(@PathParam("userId") final Long userId,
                               @Valid @NotNull final UpdateUserForm userForm) {
        String action = userForm.getAction();
        // This is a standard PATCH operation to update a user
        if (action == null) {
            userService.updateUser(userId,
                    userForm.getPassword(),
                    userForm.getFirstName(),
                    userForm.getLastName(),
                    userForm.getPhone());
            return Response.noContent().build();
        }


        if ("ACTIVATE".equalsIgnoreCase(userForm.getAction())) {
            userService.activateUserByToken(userForm.getToken());
            return Response.ok().build();
        }

        if ("RESET".equalsIgnoreCase(userForm.getAction())) {
            if (userForm.getPassword() == null) {
                throw new InvalidParameterException("password", "Password can't be blank");
            }
            userService.updatePasswordByToken(userForm.getToken(), userForm.getPassword());
            return Response.ok().build();
        }

        return Response.noContent().build();
    }


}
