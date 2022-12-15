package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.Tags;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.RestaurantService;
import ar.edu.itba.paw.service.SocialMediaService;
import ar.edu.itba.paw.webapp.dto.RestaurantDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.utils.CachingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import ar.edu.itba.paw.service.UserService;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.HTML;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("restaurants")
@Component
public class RestaurantController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private SocialMediaService socialMediaService;

    @Context
    private UriInfo uriInfo;

    // Get all restaurants

    @POST
    @Path("/register")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response registerRestaurant(final RestaurantDto restaurantDto, @Context HttpServletRequest request) {
        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }

        LOGGER.debug("Creating restaurant for user {}", user.get().getUsername());
        List<Tags> tagList = restaurantDto.getTags().stream().map(t -> Tags.valueOf(t.getValue())).collect(Collectors.toList());
        LOGGER.debug("tags: {}", tagList);
        final Restaurant restaurant = restaurantService.registerRestaurant(restaurantDto.getName(), restaurantDto.getAddress(),
                restaurantDto.getPhoneNumber(), tagList, user.get());
        //updateAuthorities();

        if (restaurantDto.getFacebook() != null){
            socialMediaService.updateFacebook(restaurantDto.getFacebook(), restaurant.getId());
        }
        if (restaurantDto.getInstagram() != null){
            socialMediaService.updateInstagram(restaurantDto.getInstagram(), restaurant.getId());
        }
        if (restaurantDto.getTwitter() != null){
            socialMediaService.updateTwitter(restaurantDto.getTwitter(), restaurant.getId());
        }

        if (restaurantDto.getProfileImage() != null) {
            Image image = new Image(restaurantDto.getProfileImage().getData());
            restaurantService.setImageByRestaurantId(image, restaurant.getId());
        }
        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(restaurant.getId())).build();
        LOGGER.info("Restaurant created in : " + uri);
        return Response.created(uri).build();
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Path("/{restaurantId}")
    public Response getRestaurantById(@PathParam("restaurantId") final int restaurantId, @Context HttpServletRequest request) {
        final Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if(restaurant.isPresent()){
            return Response.ok(RestaurantDto.fromRestaurant(restaurant.get(), request.getRequestURL().toString())).build();
        } else {
            return Response.status(Response.Status.ACCEPTED).header("error", "restaurant not found").build();
        }
    }

    @GET
    @Path("/{restaurantId}/image")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response getEventImage(@PathParam("restaurantId") final long restaurantId) throws IOException {
        CacheControl cache = CachingUtils.getCaching(CachingUtils.HOUR_TO_SEC);
        Date expireDate = CachingUtils.getExpirationDate(CachingUtils.HOUR_TO_SEC);
        final Optional<Restaurant> maybeRestaurant = restaurantService.findById(restaurantId);
        if (maybeRestaurant.isPresent()) {
            Restaurant restaurant = maybeRestaurant.get();
            final Image image = restaurant.getProfileImage();

            if(image != null){
                LOGGER.info("Found restaurant image");
                return Response.ok(image.getData()).header(HttpHeaders.CONTENT_TYPE, "image/*")
                        .cacheControl(cache).expires(expireDate).build();
            }
            else{
                final Image defaultImage = new Image(null);
                LOGGER.info("Restaurant Image not found. Placeholder is used");
                return Response.ok(defaultImage.getDataFromPlaceholder()).header(HttpHeaders.CONTENT_TYPE, "image/*")
                        .cacheControl(cache).expires(expireDate).build();
            }
        } else {
            return Response.ok(null).header(HttpHeaders.CONTENT_TYPE, "image/*")
                    .cacheControl(cache).expires(expireDate).build();
        }
    }


    @ModelAttribute("loggedUser")
    public Optional<User> getLoggedUser(HttpServletRequest request){
        return userService.findByUsername(request.getRemoteUser());
    }


}
