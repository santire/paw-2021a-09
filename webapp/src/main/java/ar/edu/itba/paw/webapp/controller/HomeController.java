package ar.edu.itba.paw.webapp.controller;


import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.swing.border.LineBorder;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.model.Sorting;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.*;
import ar.edu.itba.paw.service.RestaurantService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.dto.ForgotDto;
import ar.edu.itba.paw.webapp.dto.RecoveryDto;
import ar.edu.itba.paw.webapp.dto.TokenResponseDto;
import ar.edu.itba.paw.webapp.dto.UserDto;



@Path("/")
@Component
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private UserService userService;
    @Context
    private UriInfo uriInfo;

    @PUT
    @Path("/reset")
    @Produces(value = { MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response resetPasswordByToken(final RecoveryDto recoveryDto, @Context HttpServletRequest request) {
        User user;
        try {
            user = userService.updatePasswordByToken(recoveryDto.getToken(),recoveryDto.getPassword());
        } catch ( TokenExpiredException | TokenDoesNotExistException e) {
            return Response.status(Response.Status.UNAUTHORIZED).header("error", e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).header("error", e.getMessage()).build();
        }
        LOGGER.info("password recovered by user {}", user.getUsername());
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @POST
    @Path("/forgot")
    @Produces(value = { MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response forgotPassword(final ForgotDto forgotDto, @Context HttpServletRequest request) {
        try {
            String baseUrl = request.getHeader("Origin");
            if (baseUrl == null) {
                baseUrl = uriInfo.getBaseUri().toString();
            }
            userService.requestPasswordReset(forgotDto.getEmail(), baseUrl);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).header("error", e.getMessage()).build();
        }
        LOGGER.info("paswword recovery requested by: {}", forgotDto.getEmail());
        return Response.status(Response.Status.ACCEPTED).build();
    }
    @POST
    @Path("/register")
    @Produces(value = { MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response registerUser(final UserDto userDto, @Context HttpServletRequest request) {
        
        final User user;
        final Map<String, Object> body = new LinkedHashMap<>();

        // TODO: Exception handling should be done by a general exception manager instead of handled in controller
        try {
            String baseUrl = request.getHeader("Origin");
            if (baseUrl == null) {
                baseUrl = uriInfo.getBaseUri().toString();
            }
            user = userService.register(userDto.getUsername(),userDto.getPassword(),userDto.getFirstName(),userDto.getLastName(),userDto.getEmail(),userDto.getPhone(), baseUrl);
        } catch (EmailInUseException e) {
            LOGGER.error("Email {} is already in use (this should have been caught by validator)", e.getEmail());
            body.put("error", "email in use");
            return Response.status(Response.Status.CONFLICT).entity(body).build();
        } catch(TokenCreationException e) {
            LOGGER.error("Could not generate token");
            body.put("error", "could not generate token");
            return Response.status(Response.Status.CONFLICT).entity(body).build();
        } 
        catch (Exception e) {
            body.put("error", e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(body).build();
        }
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(user.getId())).build();
        LOGGER.info("user created: {}", uri);
        return Response.created(uri).build();
    }

    @POST
    @Path("/activate")
    @Produces(value = { MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response activate(final TokenResponseDto tokenDto, @Context HttpServletRequest request) {
        final Map<String, Object> body = new LinkedHashMap<>();
        final String token = tokenDto.getToken();
        try {
            userService.activateUserByToken(token);
        } catch(TokenExpiredException e) {
            LOGGER.error("token {} is expired", token);
        } catch(TokenDoesNotExistException e) {
            LOGGER.warn("token {} does not exist", token);
        }catch(Exception e) {
            // Unexpected error happened, showing register screen with generic error message
            LOGGER.error("Unexpected error: {}", e.getMessage());
        }
        
        body.put("message", "User activated, please log in.");
        return Response.status(Response.Status.OK).entity(body).build();
    }

/* 

    @RequestMapping(value={"/activate"}, method = RequestMethod.GET)
    public ModelAndView activate(@RequestParam(name="token", required=true) final String token) {
        User user;
        try {
            user = userService.activateUserByToken(token);
        } catch(TokenExpiredException e) {
            LOGGER.error("token {} is expired", token);
            return new ModelAndView("redirect:/register").addObject("expiredToken", true);
        }catch(TokenDoesNotExistException e) {
            LOGGER.warn("token {} does not exist", token);
            return new ModelAndView("activate").addObject("invalidToken", true);
        } catch(Exception e) {
            // Unexpected error happened, showing register screen with generic error message
            return new ModelAndView("redirect:/register").addObject("tokenError", true);
        }

        UserDetails userDetails = pawUserDetailsService.loadUserByUsername(user.getEmail());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value={ "/login" }, method=RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) final String error) {
        final ModelAndView mav = new ModelAndView("login");
        mav.addObject("error", error!=null);

        return mav;
    }

    @RequestMapping("/403")
    public ModelAndView forbidden() {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("code", 403);

        return mav;
    } */

}
