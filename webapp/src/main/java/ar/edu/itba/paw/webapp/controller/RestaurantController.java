package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.utils.CachingUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import ar.edu.itba.paw.service.UserService;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;



@Component
@Path("/restaurants")
public class RestaurantController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantController.class);
    private static final int AMOUNT_OF_MENU_ITEMS = 8;
    private static final int MAX_AMOUNT_PER_PAGE = 10;
    private static final int AMOUNT_OF_REVIEWS = 4;
    private static final int AMOUNT_OF_RESERVATIONS = 10;


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
    @Autowired
    private RatingService ratingService;
    @Autowired
    private LikesService likesService;
    @Autowired
    private ReservationService reservationService;

    @Context
    private UriInfo uriInfo;

    //READ RESTAURANTS
    @GET
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response getRestaurants(@QueryParam("page") @DefaultValue("1") Integer page,
                                        @QueryParam("pageAmount") @DefaultValue("10") Integer pageAmount,
                                   @QueryParam("search")@DefaultValue("") String search,
                                   @QueryParam("tags") List<Integer> tags,
                                   @QueryParam("min") @DefaultValue("1") Integer min,
                                   @QueryParam("max") @DefaultValue("10000") Integer max,
                                   @QueryParam("sort")@DefaultValue("name") String sortBy,
                                   @QueryParam("order")@DefaultValue("asc") String order,
                                   @QueryParam("filterby")@DefaultValue("") String filterby) {

        if(filterby == "hot"){
            List<RestaurantDto> restaurants = restaurantService.getHotRestaurants(pageAmount, 7).stream().map(u -> RestaurantDto.fromRestaurant(u, uriInfo)).collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<RestaurantDto>>(restaurants){}).build();
        }
        if(filterby == "popular"){
            List<RestaurantDto> restaurants = restaurantService.getPopularRestaurants(pageAmount, 2).stream().map(u -> RestaurantDto.fromRestaurant(u, uriInfo)).collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<RestaurantDto>>(restaurants){}).build();
        }

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
        LOGGER.info(String.valueOf(restaurants.size()));
        return Response.ok(new GenericEntity<List<RestaurantDto>>(restaurants){})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", maxPages).build(), "last")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.max((page - 1), 1)).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.min((page + 1), maxPages)).build(), "next")
                .build();
    }

    @HEAD
    @Path("/{restaurantName}")
    public Response checkRestaurantName(@PathParam("restaurantName") final String restaurantName){
        final Boolean restaurantExists = restaurantService.findByName(restaurantName);
        if(restaurantExists){
            return Response.status(Response.Status.CONFLICT).build();
        }
        
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    //READ A RESTAURANT
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

    //READ RESTAURANT MENU
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

    //READ RESTAURANT REVIEWS
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

    //CREATE REVIEW
    @POST
    @Path("/{restaurantId}/reviews")
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

    //DELETE REVIEW
    @DELETE
    @Path("/{restaurantId}/reviews/{reviewId}")
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

        return Response.status(Response.Status.ACCEPTED).build();
    }

    //ADD MENU ITEM
    @POST
    @Path("/{restaurantId}/menu")
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

    //DELETE MENU ITEM
    @DELETE
    @Path("/{restaurantId}/menu/{menuId}")
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

        return Response.status(Response.Status.ACCEPTED).build();
    }

    //REGISTER RESTAURANT
    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response registerRestaurant(final RestaurantDto restaurantDto, @Context HttpServletRequest request) {
        LOGGER.info("Registering restaurant: " + restaurantDto.toString());
        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }

        LOGGER.debug("Creating restaurant for user {}", user.get().getUsername());
        List<Tags> tagList = new ArrayList<>();
        if(restaurantDto.getTags() != null){
            tagList = restaurantDto.getTags().stream().map(t -> Tags.valueOf(t)).collect(Collectors.toList());
            LOGGER.debug("tags: {}", tagList);
        }
        
        final Restaurant restaurant = restaurantService.registerRestaurant(restaurantDto.getName(), restaurantDto.getAddress(),
                restaurantDto.getPhoneNumber(), tagList, user.get());

        if (restaurantDto.getFacebook() != null){
            socialMediaService.updateFacebook(restaurantDto.getFacebook(), restaurant.getId());
        }
        if (restaurantDto.getInstagram() != null){
            socialMediaService.updateInstagram(restaurantDto.getInstagram(), restaurant.getId());
        }
        if (restaurantDto.getTwitter() != null){
            socialMediaService.updateTwitter(restaurantDto.getTwitter(), restaurant.getId());
        }

        if(restaurantDto.getImage() != null){
            Image image = null;
            try {
                image = new Image(restaurantDto.getImage());
                restaurantService.setImageByRestaurantId(image, restaurant.getId());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } 

        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(restaurant.getId())).build();
        LOGGER.info("Restaurant created in : " + uri);
        return Response.created(uri).build();
    }

        @POST
        @Path("/{restaurantId}")
        @Produces(value = {MediaType.APPLICATION_JSON})
        @Consumes(value = { MediaType.APPLICATION_JSON})
        public Response updateRestaurant(final RestaurantDto restaurantDto, 
                                        @PathParam("restaurantId") final long restaurantId,
                                        @Context HttpServletRequest request) {                        
            LOGGER.info("Updating restaurant: " + restaurantDto.toString());
            Optional<User> user = getLoggedUser(request);
            if(!user.isPresent()){
                LOGGER.error("anon user attempt to register a restaurant");
                return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
            }

            // get the current restaurant
            Optional<Restaurant> maybeRestaurant = restaurantService.findById(restaurantId);
            if(!maybeRestaurant.isPresent()){
                LOGGER.error("Restaurant with id " + restaurantId + " do not exist");
                return Response.status(Response.Status.BAD_REQUEST).header("error", "non-existent restaurante").build();
            }
        
            if(!user.get().getOwnedRestaurants().contains(maybeRestaurant.get())){
                LOGGER.error("another user attempt to update a restaurant");
                return Response.status(Response.Status.BAD_REQUEST).header("error", "Wrong user attempt to update a restaurant").build();
            }
    
            LOGGER.debug("Updating restaurant for user {}", user.get().getUsername());
            List<Tags> tagList = new ArrayList<>();
            if(restaurantDto.getTags() != null){
                tagList = restaurantDto.getTags().stream().map(t -> Tags.valueOf(t)).collect(Collectors.toList());
                LOGGER.debug("tags: {}", tagList);
            }
            
            restaurantService.updateRestaurant(restaurantId, restaurantDto.getName(), restaurantDto.getAddress(),
                    restaurantDto.getPhoneNumber(), tagList);
    
            if (restaurantDto.getFacebook() != null){
                socialMediaService.updateFacebook(restaurantDto.getFacebook(), restaurantId);
            }
            if (restaurantDto.getInstagram() != null){
                socialMediaService.updateInstagram(restaurantDto.getInstagram(), restaurantId);
            }
            if (restaurantDto.getTwitter() != null){
                socialMediaService.updateTwitter(restaurantDto.getTwitter(), restaurantId);
            }
    
            if(restaurantDto.getImage() != null){
                Image image = null;
                try {
                    image = new Image(restaurantDto.getImage());
                    restaurantService.setImageByRestaurantId(image, restaurantId);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } 
    
            final URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(String.valueOf(restaurantId)).build();
            LOGGER.info("Restaurant created in : " + uri);
            return Response.created(uri).build();
        }

    //SET IMAGE
    @PUT
    @Path("/{restaurantId}/image")
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response getRestaurantImage(@PathParam("restaurantId") final long restaurantId, final ImageDto imageDto,  @Context HttpServletRequest request) {

        final Optional<Restaurant> maybeRestaurant = restaurantService.findById(restaurantId);
        if (!maybeRestaurant.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();

        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }

        if (!userService.isTheRestaurantOwner(user.get().getId(), restaurantId))
            return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();

        Image image = new Image(imageDto.getData());
        restaurantService.setImageByRestaurantId(image, maybeRestaurant.get().getId());

        return Response.status(Response.Status.ACCEPTED).build();
    }

    //GET IMAGE
    @GET
    @Path("/{restaurantId}/image")
    @Produces("image/jpg")
    public Response getRestaurantImage(@PathParam("restaurantId") final long restaurantId) throws IOException {
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
                final Image defaultImage = new Image((byte[])null);
                LOGGER.info("Restaurant Image not found. Placeholder is used");
                return Response.ok(defaultImage.getDataFromPlaceholder())
                        .cacheControl(cache).expires(expireDate).build();
            }
        } else {
            return Response.ok(null)
                    .cacheControl(cache).expires(expireDate).build();
        }
    }

    //DELETE RESTAURANT
    @DELETE
    @Path("/{restaurantId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response deleteRestaurant(@PathParam("restaurantId") final long restaurantId, @Context HttpServletRequest request){

        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }

        if (!userService.isTheRestaurantOwner(user.get().getId(), restaurantId))
            return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();

        restaurantService.deleteRestaurantById(restaurantId);

        return Response.status(Response.Status.ACCEPTED).build();
    }

    //READ ALL TAGS
    @GET
    @Path("/tags")
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response getRestautantsTags() {
        return Response.ok(new GenericEntity<Collection<Tags>>(Tags.allTags().values()){}).build();
    }

    //LIKE RESTAURANT
    @PUT
    @Path("/{restaurantId}/like")
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response likeRestaurant(@PathParam("restaurantId") final long restaurantId, @Context HttpServletRequest request) {

        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }
        long userId = user.get().getId();
        if (likesService.userLikesRestaurant(userId,restaurantId))
            likesService.dislike(userId,restaurantId);
        else
            likesService.like(userId, restaurantId);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    //GET USER LIKE
    @GET
    @Path("/{restaurantId}/like")
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response getRestaurantlike(@PathParam("restaurantId") final long restaurantId, @Context HttpServletRequest request) {

        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }
        long userId = user.get().getId();
        Boolean like = likesService.userLikesRestaurant(userId,restaurantId);
        return Response.ok(new GenericEntity<Boolean>(like){}).build();
    }

    //RATE RESTAURANT
    @PUT
    @Path("/{restaurantId}/rating")
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response rateRestaurant(@PathParam("restaurantId") final long restaurantId, @QueryParam("rating") Double rating, @Context HttpServletRequest request) {

        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }
        long userId = user.get().getId();
        ratingService.rateRestaurant(userId,restaurantId,rating);

        return Response.status(Response.Status.ACCEPTED).build();
    }

    //READ USER RATING
    @GET
    @Path("/{restaurantId}/rating")
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response getRestaurantRate(@PathParam("restaurantId") final long restaurantId, @Context HttpServletRequest request) {

        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }
        long userId = user.get().getId();
        Double rate = ratingService.getRating(userId, restaurantId).get().getRating();
        return Response.ok(new GenericEntity<Double>(rate){}).build();
    }

    //READ RESTAURANT RESERVATIONS
    @GET
    @Path("/{restaurantId}/reservation")
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response findRestaurantReservations(@PathParam("restaurantId") final long restaurantId, @QueryParam("filterby")@DefaultValue("") String filterby, @QueryParam("page") @DefaultValue("1") Integer page, @Context HttpServletRequest request) {

        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }
        if (!userService.isTheRestaurantOwner(user.get().getId(), restaurantId))
            return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();

        int maxPages;
        List<ReservationDto> reservations;

        if(filterby == "pending"){
            maxPages = reservationService.findPendingByRestaurantPageCount(AMOUNT_OF_RESERVATIONS, restaurantId);
            reservations = reservationService.findPendingByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId).stream().map(u -> ReservationDto.fromReservation(u, uriInfo)).collect(Collectors.toList());
        }else if(filterby == "confirmed"){
            maxPages = reservationService.findConfirmedByRestaurantPageCount(AMOUNT_OF_RESERVATIONS, restaurantId);
            reservations = reservationService.findConfirmedByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId).stream().map(u -> ReservationDto.fromReservation(u, uriInfo)).collect(Collectors.toList());
        }else{
            maxPages = reservationService.findByRestaurantPageCount(AMOUNT_OF_RESERVATIONS, restaurantId);
            reservations = reservationService.findByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId).stream().map(u -> ReservationDto.fromReservation(u, uriInfo)).collect(Collectors.toList());
        }
        return Response.ok(new GenericEntity<List<ReservationDto>>(reservations){})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", maxPages).build(), "last")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.max((page - 1), 1)).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.min((page + 1), maxPages)).build(), "next")
                .build();
    }

    //ADD RESERVATION
    @POST
    @Path("/{restaurantId}/reservation")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response addRestaurantReservation(@PathParam("restaurantId") final long restaurantId, final ReservationDto reservationDto, @Context HttpServletRequest request){

        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }
        String baseUrl = request.getHeader("Origin");
        if (baseUrl == null) {
            baseUrl = uriInfo.getBaseUri().toString();
        }
        final Reservation res = reservationService.addReservation(user.get().getId(), restaurantId, reservationDto.getDate(),reservationDto.getQuantity(),baseUrl);

        final URI uri = uriInfo.getBaseUriBuilder().path("/"+restaurantId+"/reservation").build();
        return Response.created(uri).build();
    }

    //DELETE RESERVATION
    @DELETE
    @Path("/{restaurantId}/reservation/{reservationId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response deleteRestaurantReservation(@PathParam("restaurantId") final long restaurantId, @PathParam("reservationId") final long reservationId, @QueryParam("message")@DefaultValue("") String message, @Context HttpServletRequest request){

        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("Anon user attempt to delete a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }
        Optional<Reservation> reservation = reservationService.findById(reservationId);
        if(!reservation.isPresent())
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();

        if(reservation.get().getUser().getId().equals(user.get().getId()))
            reservationService.userCancelReservation(reservationId);
        else if(userService.isTheRestaurantOwner(user.get().getId(), reservation.get().getRestaurant().getId()))
            reservationService.ownerCancelReservation(reservationId, message);
        else
            return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();

        return Response.status(Response.Status.ACCEPTED).build();
    }

    //CONFIRM RESERVATION
    @PUT
    @Path("/{restaurantId}/reservation/{reservationId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response confirmRestaurantReservation(@PathParam("restaurantId") final long restaurantId, @PathParam("reservationId") final long reservationId, @Context HttpServletRequest request){

        Optional<User> user = getLoggedUser(request);
        if(!user.isPresent()){
            LOGGER.error("anon user attempt to register a restaurant");
            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
        }
        Optional<Reservation> reservation = reservationService.findById(reservationId);
        if(!reservation.isPresent())
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();

        if(userService.isTheRestaurantOwner(user.get().getId(), reservation.get().getRestaurant().getId()))
            reservationService.confirmReservation(reservationId);
        else
            return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();
        return Response.status(Response.Status.ACCEPTED).build();
    }



    @ModelAttribute("loggedUser")
    public Optional<User> getLoggedUser(HttpServletRequest request){
        LOGGER.info("USER: " + SecurityContextHolder.getContext().getAuthentication().getName());
        return userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());  
    }


}
