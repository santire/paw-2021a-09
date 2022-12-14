package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import ar.edu.itba.paw.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.*;
import java.net.URI;


@Path("users")
@Component
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


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
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).header("error", e.getMessage()).build();
        }
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(userDto.getUserId())).build();
        LOGGER.info("user updated: " + uri);
        return Response.created(uri).build();
    }

    @PUT
    @Path("/forgot")
    @Produces(value = { MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response forgotPassword(final UserDto userDto, @Context HttpServletRequest request) {
        try {
            userService.requestPasswordReset(userDto.getEmail(),request.getRemoteHost().toString());
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).header("error", e.getMessage()).build();
        }
        LOGGER.info("paswword recovery requested by: " + userDto.getUsername());
        return Response.status(Response.Status.ACCEPTED).build();
    }

    //READ USER
    
    @GET
    @Path("/{userId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUser(@PathParam("userId") final int userId, @Context HttpServletRequest request) {
        final Optional<User> user = userService.findById(userId);
        if(user.isPresent()){
            return Response.ok(UserDto.fromUser(user.get(), request.getRequestURL().toString())).build();
        } else {
            return Response.status(Response.Status.ACCEPTED).header("error", "user not found").build();
        }
    }

    // CREATE USER

    @POST
    @Produces(value = { MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response registerUser(final UserDto userDto, @Context HttpServletRequest request) {
        
        final User user;

        try {
            LOGGER.info("POST /users -> attempt to create user");
            user = userService.register(userDto.getUsername(),userDto.getPassword(),userDto.getFirstName(),userDto.getLastName(),userDto.getEmail(),userDto.getPhone(),request.getRequestURL().toString());
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).header("error", e.getMessage()).build();
        }
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(user.getId())).build();
        LOGGER.info("user created: " + uri);
        return Response.created(uri).build();
    }

}
