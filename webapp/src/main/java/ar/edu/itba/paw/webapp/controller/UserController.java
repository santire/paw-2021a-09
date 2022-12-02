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
import java.util.Optional;

@Path("users")
@Component
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // UPDATE USER
    @PUT
    @Path("/user/edit")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response editUser(final UserDto userDto, @Context HttpServletRequest request){
        try{
            userService.updateUser(
                    userDto.getUserId(),
                    userDto.getUsername(),
                    userDto.getPassword(),
                    userDto.getFirstName(),
                    userDto.getLastName(),
                    userDto.getEmail(),
                    userDto.getPhone()
            );
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).header("error", e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/user/{userId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUserById(@PathParam("userId") final int userId, @Context HttpServletRequest request) {
        final Optional<User> user = userService.findById(userId);
        if(user.isPresent()){
            return Response.ok(UserDto.fromUser(user.get(), request.getRequestURL().toString())).build();
        } else {
            return Response.status(Response.Status.ACCEPTED).header("error", "user not found").build();
        }
    }
}
