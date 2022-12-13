package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;


import ar.edu.itba.paw.service.MenuService;
import ar.edu.itba.paw.service.ReservationService;

import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.dto.CommentDto;
import ar.edu.itba.paw.webapp.dto.MenuItemDto;
import ar.edu.itba.paw.webapp.dto.RestaurantDto;
import ar.edu.itba.paw.webapp.forms.CommentForm;
import ar.edu.itba.paw.webapp.forms.MenuItemForm;
import ar.edu.itba.paw.webapp.forms.RatingForm;
import ar.edu.itba.paw.webapp.forms.ReservationForm;
import ar.edu.itba.paw.webapp.forms.RestaurantForm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@Path("/restaurants")
public class RestaurantController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantController.class);
    private static final int AMOUNT_OF_MENU_ITEMS = 8;
    private static final int AMOUNT_OF_RESTAURANTS = 10;
    private static final int AMOUNT_OF_REVIEWS = 4;

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private SocialMediaService socialMediaService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommonAttributes ca;



    @Context
    private UriInfo uriInfo;

    //Like
    //review
    //reservation reservationService.addReservation(loggedUser.getId(), restaurantId, dateAt, Long.parseLong(form.getQuantity()), ca.getUri());

    @GET
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response getRestaurants(@QueryParam("page") @DefaultValue("1") Integer page,
                                   @QueryParam("search")@DefaultValue("") String search,
                                   @QueryParam("tags") List<Integer> tags,
                                   @QueryParam("min") @DefaultValue("1") Integer min,
                                   @QueryParam("max") @DefaultValue("10000") Integer max,
                                   @QueryParam("search")@DefaultValue("name") String sortBy,
                                   @QueryParam("order")@DefaultValue("asc") String order ) {

        if (search != "")
            search = search.trim().replaceAll("[^a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+", "");

        List<Tags> tagsSelected = new ArrayList<>();
        List<Integer> tagsChecked = new ArrayList<>();
        if(tags!=null){
            for( int i : tags){
                // TODO: fix this, should throw some exception
                if(Tags.valueOf(i) == null)
                    return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
                tagsSelected.add(Tags.valueOf(i));
                tagsChecked.add(i);
            }
        }

        Sorting sort = Sorting.NAME;
        try {
            sort = Sorting.valueOf(sortBy.toUpperCase());
        } catch (Exception e) {
            LOGGER.warn("Caught illegal sorting option {}, defaulting to NAME", sortBy);
        }

        boolean desc = false;
        if(order != null && order.equals("DESC"))
            desc = true;
        order = desc ? "DESC" : "ASC";


        int maxPages = restaurantService.getRestaurantsFilteredByPageCount(AMOUNT_OF_RESTAURANTS, search, tagsSelected, min, max);
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsFilteredBy(page, AMOUNT_OF_RESTAURANTS, search, tagsSelected,min,max, Sorting.NAME, true, 7).stream().map(u -> RestaurantDto.fromRestaurant(u, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<RestaurantDto>>(restaurants){})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", maxPages).build(), "last")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.max((page - 1), 1)).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.min((page + 1), maxPages)).build(), "next")
                .build();
    }

    @GET
    @Path("/{restaurantId}")
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response findRestaurantByID(@PathParam("restaurantId") final long restaurantId) {

        final Optional<Restaurant> maybeRestaurant = restaurantService.findById(restaurantId);

        if(!maybeRestaurant.isPresent())
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();

        final RestaurantDto restaurant = maybeRestaurant.map(u -> RestaurantDto.fromRestaurant(u, uriInfo)).orElseThrow(RestaurantNotFoundException::new);

        return Response.ok(new GenericEntity<RestaurantDto>(restaurant){}).build();
    }

    @GET
    @Path("/{restaurantId}/menu")
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response findRestaurantMenu(@PathParam("restaurantId") final long restaurantId, @QueryParam("page") @DefaultValue("1") Integer page) {

        int maxPages = restaurantService.findByIdWithMenuPagesCount(AMOUNT_OF_MENU_ITEMS, restaurantId);

        final Optional<Restaurant> maybeRestaurant = restaurantService.findByIdWithMenu(restaurantId,page,AMOUNT_OF_MENU_ITEMS);

        if(!maybeRestaurant.isPresent())
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();

        List<MenuItemDto> menu = maybeRestaurant.orElseThrow(RestaurantNotFoundException::new).getMenu().stream().map(MenuItemDto::fromMenuItem).collect(Collectors.toList());


        return Response.ok(new GenericEntity<List<MenuItemDto>>(menu){})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", maxPages).build(), "last")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.max((page - 1), 1)).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.min((page + 1), maxPages)).build(), "next")
                .build();
    }

    @GET
    @Path("/{restaurantId}/reviews")
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response findRestaurantReviews(@PathParam("restaurantId") final long restaurantId, @QueryParam("page") @DefaultValue("1") Integer page) {
        int maxPages = commentService.findByRestaurantPageCount(AMOUNT_OF_REVIEWS, restaurantId);

        List<CommentDto> reviews = commentService.findByRestaurant(page, AMOUNT_OF_REVIEWS, restaurantId).stream().map(u -> CommentDto.fromComment(u, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<CommentDto>>(reviews){})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", maxPages).build(), "last")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.max((page - 1), 1)).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.min((page + 1), maxPages)).build(), "next")
                .build();
    }


    @POST
    @Path("/{restaurantId}/reviews/create")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response addRestaurantReview(@PathParam("restaurantId") final long restaurantId, final CommentDto comment){

        /*
        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }
        */
        //TODO replace me
        Optional<User> user = userService.findById(2);

        final Comment rev = commentService.addComment(user.get().getId(), restaurantId, comment.getUserComment());

        final URI uri = uriInfo.getBaseUriBuilder().path("/"+restaurantId+"/reviews").build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("/{restaurantId}/reviews/{reviewId}/delete")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response deleteRestaurantReview(@PathParam("restaurantId") final long restaurantId, @PathParam("reviewId") final long reviewId){

        /*
        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }
        */
        //TODO replace me
        Optional<User> user = userService.findById(2);

        Optional<Comment> review = commentService.findById(reviewId);

        if(!review.isPresent())
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        if(!user.get().getId().equals(review.get().getUser().getId()))
            return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();

        commentService.deleteComment(reviewId);

        final URI uri = uriInfo.getBaseUriBuilder().path("/"+restaurantId+"/reviews").build();
        return Response.created(uri).build();
    }

    @POST
    @Path("/{restaurantId}/menu/create")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response addRestaurantMenuItem(@PathParam("restaurantId") final long restaurantId, final MenuItemDto menuItem){

        /*
        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }
        */
        //TODO replace me
        Optional<User> user = userService.findById(2);

        if (!userService.isTheRestaurantOwner(user.get().getId(), restaurantId))
            return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();

        final MenuItem item = new MenuItem(menuItem.getId(), menuItem.getName(), menuItem.getDescription(), menuItem.getPrice());
        menuService.addItemToRestaurant(restaurantId, item);

        final URI uri = uriInfo.getBaseUriBuilder().path("/"+restaurantId+"/menu").build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("/{restaurantId}/menu/{menuId}/delete")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response deleteRestaurantMenuItem(@PathParam("restaurantId") final long restaurantId, @PathParam("menuId") final long menuId){

        /*
        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }
        */
        //TODO replace me
        Optional<User> user = userService.findById(2);

        if (!userService.isTheRestaurantOwner(user.get().getId(), restaurantId))
            return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();

        menuService.deleteItemById(menuId);

        final URI uri = uriInfo.getBaseUriBuilder().path("/"+restaurantId+"/menu").build();
        return Response.created(uri).build();
    }



    /*
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
    */


}
