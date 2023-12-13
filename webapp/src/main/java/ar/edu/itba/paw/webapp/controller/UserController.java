package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.*;
import ar.edu.itba.paw.service.JwtService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.forms.RegisterUserForm;
import ar.edu.itba.paw.webapp.forms.UpdateUserForm;
import ar.edu.itba.paw.webapp.methods.PATCH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Map;


@Path(UserController.PATH)
@Component
public class UserController {
    public static final String PATH = "users";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private JwtService tokenService;
    @Autowired
    private UserDetailsService userDetailsService;
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
        final URI baseUri = uriInfo.getBaseUriBuilder()
                .path(PATH)
                .build();

        String baseUrl = URI.create(request.getRequestURL().toString()).resolve(request.getContextPath()).toString();

        if (forgotEmail != null && !forgotEmail.isEmpty()) {
            userService.requestPasswordReset(forgotEmail, baseUrl, baseUri);
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
                userForm.getPhone(), baseUrl, baseUri);

        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(user.getId()))
                .build();

        LOGGER.info("user created: {}", uri);
        UserDto userDto = UserDto.fromUser(user, uriInfo);
        return Response.created(uri).entity(
                new GenericEntity<UserDto>(userDto){}
        ).build();
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
        if (action == null || ("PATCH".equalsIgnoreCase(userForm.getAction()))) {
            User user = userService.updateUser(userId,
                    userForm.getPassword(),
                    userForm.getFirstName(),
                    userForm.getLastName(),
                    userForm.getPhone());
            UserDto userDto = UserDto.fromUser(user, uriInfo);
            return Response.ok(new GenericEntity<UserDto>(userDto){}).build();
        }


        if ("ACTIVATE".equalsIgnoreCase(userForm.getAction())) {
            User user = userService.activateUserByToken(userForm.getToken());
            Map<String, String> tokens = tokenService.generateTokens(userDetailsService.loadUserByUsername(user.getEmail()));
            return Response.ok()
                    .header(
                            "X-Refresh-Token", "Bearer " + tokens.get("refresh_token")
                    )
                    .header(
                            "X-Auth-Token", "Bearer " + tokens.get("access_token")
                    )
                    .build();
        }

        if ("RESET".equalsIgnoreCase(userForm.getAction())) {
            if (userForm.getPassword() == null) {
                throw new InvalidParameterException("password", "Password can't be blank");
            }
            User user = userService.updatePasswordByToken(userForm.getToken(), userForm.getPassword());
            Map<String, String> tokens = tokenService.generateTokens(userDetailsService.loadUserByUsername(user.getEmail()));
            return Response.ok()
                    .header(
                            "X-Refresh-Token", "Bearer " + tokens.get("refresh_token")
                    )
                    .header(
                            "X-Auth-Token", "Bearer " + tokens.get("access_token")
                    )
                    .build();
        }

        throw new InvalidParameterException("action", "Invalid PATCH Operation");
    }


}
