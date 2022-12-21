package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.dto.CommentDto;
import ar.edu.itba.paw.webapp.dto.MenuItemDto;
import ar.edu.itba.paw.webapp.dto.RestaurantDto;
import ar.edu.itba.paw.webapp.utils.CachingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import ar.edu.itba.paw.service.UserService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;



@Component
@Path("/restaurants")
public class RestaurantController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static final int AMOUNT_OF_MENU_ITEMS = 8;
    private static final int MAX_AMOUNT_PER_PAGE = 10;
    private static final int AMOUNT_OF_REVIEWS = 4;

    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private SocialMediaService socialMediaService;

    @Context
    private UriInfo uriInfo;

    //Like
    //rating
    //updatesocialmedia
    //delete restaurant
    //reservation require dto? reservationService.addReservation(loggedUser.getId(), restaurantId, dateAt, Long.parseLong(form.getQuantity()), ca.getUri());

    @GET
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response getRestaurants(@QueryParam("page") @DefaultValue("1") Integer page,
                                        @QueryParam("pageAmount") @DefaultValue("10") Integer pageAmount,
                                   @QueryParam("search")@DefaultValue("") String search,
                                   @QueryParam("tags") List<Integer> tags,
                                   @QueryParam("min") @DefaultValue("1") Integer min,
                                   @QueryParam("max") @DefaultValue("10000") Integer max,
                                   @QueryParam("sort")@DefaultValue("name") String sortBy,
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
        if(order != null && order.equalsIgnoreCase("DESC"))
            desc = true;
        if(pageAmount > MAX_AMOUNT_PER_PAGE) {
            pageAmount = MAX_AMOUNT_PER_PAGE;
        }


        int maxPages = restaurantService.getRestaurantsFilteredByPageCount(pageAmount, search, tagsSelected, min, max);
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsFilteredBy(page, pageAmount, search, tagsSelected,min,max, sort, desc, 7).stream().map(u -> RestaurantDto.fromRestaurant(u, uriInfo)).collect(Collectors.toList());

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
    public Response addRestaurantReview(@PathParam("restaurantId") final long restaurantId, final CommentDto comment, @Context HttpServletRequest request){

        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }

        final Comment rev = commentService.addComment(user.get().getId(), restaurantId, comment.getUserComment());

        final URI uri = uriInfo.getBaseUriBuilder().path("/"+restaurantId+"/reviews").build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("/{restaurantId}/reviews/{reviewId}/delete")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response deleteRestaurantReview(@PathParam("restaurantId") final long restaurantId, @PathParam("reviewId") final long reviewId, @Context HttpServletRequest request){

        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }

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
    public Response addRestaurantMenuItem(@PathParam("restaurantId") final long restaurantId, final MenuItemDto menuItem, @Context HttpServletRequest request){

        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }

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
    public Response deleteRestaurantMenuItem(@PathParam("restaurantId") final long restaurantId, @PathParam("menuId") final long menuId, @Context HttpServletRequest request){

        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }

        if (!userService.isTheRestaurantOwner(user.get().getId(), restaurantId))
            return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();

        menuService.deleteItemById(menuId);

        final URI uri = uriInfo.getBaseUriBuilder().path("/"+restaurantId+"/menu").build();
        return Response.created(uri).build();
    }


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

        if (restaurantDto.getImage() != null) {
            //TODO post to /image and dto cointains just the URI
            //Image image = new Image(restaurantDto.getImage().getData());
            //restaurantService.setImageByRestaurantId(image, restaurant.getId());
        }
        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(restaurant.getId())).build();
        LOGGER.info("Restaurant created in : " + uri);
        return Response.created(uri).build();
    }
    /*
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Path("/{restaurantId}")
    public Response getRestaurantById(@PathParam("restaurantId") final int restaurantId, @Context HttpServletRequest request) {
        final Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if(restaurant.isPresent()){
            return Response.ok(RestaurantDto.fromRestaurant(restaurant.get(), uriInfo)).build();
        } else {
            return Response.status(Response.Status.ACCEPTED).header("error", "restaurant not found").build();
        }
    }
     */

    @GET
    @Path("/{restaurantId}/image")
    @Produces("image/jpg")
    public Response getEventImage(@PathParam("restaurantId") final long restaurantId) throws IOException {
        CacheControl cache = CachingUtils.getCaching(CachingUtils.HOUR_TO_SEC);
        Date expireDate = CachingUtils.getExpirationDate(CachingUtils.HOUR_TO_SEC);
        final Optional<Restaurant> maybeRestaurant = restaurantService.findById(restaurantId);
        if (maybeRestaurant.isPresent()) {
            Restaurant restaurant = maybeRestaurant.get();
            final Image image = restaurant.getProfileImage();

            if(image != null){
                LOGGER.info("Found restaurant image");
                return Response.ok(image.getData())
                        .cacheControl(cache).expires(expireDate).build();
            }
            else{
                final Image defaultImage = new Image(null);
                LOGGER.info("Restaurant Image not found. Placeholder is used");
                return Response.ok(defaultImage.getDataFromPlaceholder())
                        .cacheControl(cache).expires(expireDate).build();
            }
        } else {
            return Response.ok(null)
                    .cacheControl(cache).expires(expireDate).build();
        }
    }




    @ModelAttribute("loggedUser")
    public Optional<User> getLoggedUser(HttpServletRequest request){
        return userService.findByUsername(request.getRemoteUser());
    }


}
