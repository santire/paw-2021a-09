package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.CommentNotFoundException;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.forms.CommentForm;
import ar.edu.itba.paw.webapp.forms.MenuItemForm;
import ar.edu.itba.paw.webapp.forms.RatingForm;
import ar.edu.itba.paw.webapp.forms.RegisterRestaurantForm;
import ar.edu.itba.paw.webapp.utils.CachingUtils;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import ar.edu.itba.paw.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


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
                                   @QueryParam("filterBy")@DefaultValue("") String filterBy) {

        if(filterBy.equalsIgnoreCase("hot")){
            List<RestaurantDto> restaurants = restaurantService.getHotRestaurants(pageAmount, 7)
                    .stream()
                    .map(u -> RestaurantDto.fromRestaurant(u, uriInfo))
                    .collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<RestaurantDto>>(restaurants){}).build();
        }
        if(filterBy.equalsIgnoreCase("popular")){
            List<RestaurantDto> restaurants = restaurantService.getPopularRestaurants(pageAmount, 2)
                    .stream()
                    .map(u -> RestaurantDto.fromRestaurant(u, uriInfo))
                    .collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<RestaurantDto>>(restaurants){}).build();
        }

        if (search != "")
            search = search.trim().replaceAll("[^a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+", "");

        List<Tags> tagsSelected = new ArrayList<>();
//        List<Integer> tagsChecked = new ArrayList<>();
        if(tags!=null){
            for( int i : tags){
                if(Tags.valueOf(i) == null) {
                    LOGGER.warn("Tag {} does not exist. Ignoring...", i);
                }
                else {
                    tagsSelected.add(Tags.valueOf(i));
//                    tagsChecked.add(i);
                }
            }
        }

        Sorting sort = Sorting.NAME;
        try {
            sort = Sorting.valueOf(sortBy.toUpperCase());
        } catch (Exception e) {
            LOGGER.warn("Invalid sorting option {}, defaulting to NAME", sortBy);
        }

        boolean desc = false;
        if(order != null && order.equalsIgnoreCase("DESC"))
            desc = true;
        if(pageAmount > MAX_AMOUNT_PER_PAGE) {
            pageAmount = MAX_AMOUNT_PER_PAGE;
        }

        int maxPages = restaurantService.getRestaurantsFilteredByPageCount(pageAmount, search, tagsSelected, min, max);
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsFilteredBy(
                page, pageAmount, search, tagsSelected,min,max, sort, desc, 7)
                .stream()
                .map(u -> RestaurantDto.fromRestaurant(u, uriInfo))
                .collect(Collectors.toList());
        LOGGER.info(String.valueOf(restaurants.size()));
        return Response.ok(new GenericEntity<List<RestaurantDto>>(restaurants){})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", maxPages).build(), "last")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.max((page - 1), 1)).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.min((page + 1), maxPages)).build(), "next")
                .build();
    }

    // FIX: This is not Restful
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
        final Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        final RestaurantDto restaurantDto = RestaurantDto.fromRestaurant(restaurant, uriInfo);
        return Response.ok(new GenericEntity<RestaurantDto>(restaurantDto){}).build();
    }
    @GET
    @Path("/{restaurantId}/image")
    @Produces("image/jpg")
    public Response getRestaurantImage(@PathParam("restaurantId") final long restaurantId) throws IOException {
        CacheControl cache = CachingUtils.getCaching(CachingUtils.HOUR_TO_SEC);
        Date expireDate = CachingUtils.getExpirationDate(CachingUtils.HOUR_TO_SEC);
        final Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        final Image image = restaurant.getProfileImage();
        final byte[] imageData = image != null ? image.getData() : Image.getPlaceholderImage();

        return Response.ok(imageData)
                .cacheControl(cache).expires(expireDate).build();
    }

    //READ RESTAURANT MENU
    @GET
    @Path("/{restaurantId}/menu")
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response findRestaurantMenu(@PathParam("restaurantId") final long restaurantId, @QueryParam("page") @DefaultValue("1") Integer page) {

        int maxPages = restaurantService.findByIdWithMenuPagesCount(AMOUNT_OF_MENU_ITEMS, restaurantId);
        final Restaurant restaurant = restaurantService.findByIdWithMenu(restaurantId,page,AMOUNT_OF_MENU_ITEMS)
                .orElseThrow(RestaurantNotFoundException::new);

        List<MenuItemDto> menu = restaurant.getMenu()
                .stream()
                .map(MenuItemDto::fromMenuItem)
                .collect(Collectors.toList());

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

        List<CommentDto> reviews = commentService.findByRestaurant(page, AMOUNT_OF_REVIEWS, restaurantId)
                .stream()
                .map(u -> CommentDto.fromComment(u, uriInfo))
                .collect(Collectors.toList());

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
    @PreAuthorize("!@authComponent.isRestaurantOwner(#restaurantId)")
    public Response addRestaurantReview(@PathParam("restaurantId") final Long restaurantId,
                                        final @Valid @NotNull CommentForm comment, @Context HttpServletRequest request){
        User user = getLoggedUser();
        final Comment rev = commentService.addComment(user.getId(), restaurantId, comment.getReview());
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(rev.getId())).build();
        return Response.created(uri).build();
    }

    //DELETE REVIEW
    @DELETE
    @Path("/{restaurantId}/reviews/{reviewId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isReviewOwner(#reviewId) OR hasRole('ROLE_ADMIN')")
    public Response deleteRestaurantReview(@PathParam("restaurantId") final Long restaurantId,
                                           @PathParam("reviewId") final Long reviewId, @Context HttpServletRequest request){
        Comment review = commentService.findById(reviewId).orElseThrow(CommentNotFoundException::new);
        commentService.deleteComment(reviewId);
        return Response.noContent().build();
    }

    //ADD MENU ITEM
    @POST
    @Path("/{restaurantId}/menu")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isRestaurantOwner(#restaurantId)")
    public Response addRestaurantMenuItem(@PathParam("restaurantId") final Long restaurantId,
                                          final @Valid @NotNull MenuItemForm menuItem, @Context HttpServletRequest request){
        final MenuItem item = new MenuItem(menuItem.getId(), menuItem.getName(), menuItem.getDescription(), menuItem.getPrice());
        menuService.addItemToRestaurant(restaurantId, item);
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(item.getId())).build();
        return Response.created(uri).build();
    }

    //DELETE MENU ITEM
    @DELETE
    @Path("/{restaurantId}/menu/{menuId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    // Restaurant owner and menu item belongs to said restaurant
    @PreAuthorize("@authComponent.isRestaurantAndMenuOwner(#restaurantId, #menuId) or hasRole('ROLE_ADMIN')")
    public Response deleteRestaurantMenuItem(@PathParam("restaurantId") final Long restaurantId,
                                             @PathParam("menuId") final Long menuId, @Context HttpServletRequest request){
        menuService.deleteItemById(menuId);
        return Response.noContent().build();
    }

    //REGISTER RESTAURANT
    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response registerRestaurant(final @Valid @NotNull RegisterRestaurantForm restaurantForm,
                                       @Context HttpServletRequest request) {
        User user = getLoggedUser();
        List<Tags> tagList = new ArrayList<>();
        if(restaurantForm.getTags() != null){
            tagList = Arrays.stream(restaurantForm.getTags()).map(Tags::valueOf).collect(Collectors.toList());
            LOGGER.debug("tags: {}", tagList);
        }

        final Restaurant restaurant = restaurantService.registerRestaurant(
                restaurantForm.getName(),
                restaurantForm.getAddress(),
                restaurantForm.getPhoneNumber(),
                tagList,
                user,
                restaurantForm.getFacebook(),
                restaurantForm.getTwitter(),
                restaurantForm.getInstagram()
                );

        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(restaurant.getId())).build();
        LOGGER.info("Restaurant created in : " + uri);
        return Response.created(uri).build();
    }

    @PUT
    @Path("/{restaurantId}/image")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = { MediaType.MULTIPART_FORM_DATA})
    @PreAuthorize("@authComponent.isRestaurantOwner(#restaurantId)")
    public Response setImage(@PathParam("restaurantId") final Long restaurantId,
                             @FormDataParam("image") final FormDataBodyPart body,
                             @FormDataParam("image") final byte[] bytes,
                             @Context HttpServletRequest request) throws IOException {
        // Check media type
        // body.getMediaType()
        String contentType = body.getMediaType().toString();
        if (!(contentType.equalsIgnoreCase("image/png")
                || contentType.equalsIgnoreCase("image/jpg")
                || contentType.equalsIgnoreCase("image/jpeg"))) {
            // throw new InvalidMediaTypeException
        }

        // Check file size
//        if(bytes.length > 200) {
//            throw new FileTooLargeException;
//        }
        Image image = new Image(bytes);
        restaurantService.setImageByRestaurantId(image, restaurantId);
        final URI uri = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(uri).build();
    }
        @PUT
        @Path("/{restaurantId}")
        @Produces(value = {MediaType.APPLICATION_JSON})
        @Consumes(value = { MediaType.APPLICATION_JSON})
        @PreAuthorize("@authComponent.isRestaurantOwner(#restaurantId)")
        public Response updateRestaurant(
                @PathParam("restaurantId") final Long restaurantId,
                final @Valid @NotNull RegisterRestaurantForm restaurantForm,
                                           @Context HttpServletRequest request) {
            List<Tags> tagList = new ArrayList<>();
            if(restaurantForm.getTags() != null){
                tagList = Arrays.stream(restaurantForm.getTags()).map(Tags::valueOf).collect(Collectors.toList());
                LOGGER.debug("tags: {}", tagList);
            }
            restaurantService.updateRestaurant(
                    restaurantId,
                    restaurantForm.getName(),
                    restaurantForm.getAddress(),
                    restaurantForm.getPhoneNumber(),
                    tagList,
                    restaurantForm.getFacebook(),
                    restaurantForm.getTwitter(),
                    restaurantForm.getInstagram()
                    );
            return Response.noContent().build();
        }

    //DELETE RESTAURANT
    @DELETE
    @Path("/{restaurantId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isRestaurantOwner(#restaurantId)")
    public Response deleteRestaurant(@PathParam("restaurantId") final Long restaurantId,
                                     @Context HttpServletRequest request){
        restaurantService.deleteRestaurantById(restaurantId);
        return Response.noContent().build();
    }


    //LIKE RESTAURANT
    @POST
    @Path("/{restaurantId}/likes")
    @Produces( value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("!@authComponent.isRestaurantOwner(#restaurantId)")
    public Response likeRestaurant(@PathParam("restaurantId") final Long restaurantId,
                                   @Context HttpServletRequest request) {
        User user = getLoggedUser();
        likesService.like(user.getId(), restaurantId);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @DELETE
    @Path("/{restaurantId}/likes")
    @Produces( value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("!@authComponent.isRestaurantOwner(#restaurantId)")
    public Response dislikeRestaurant(@PathParam("restaurantId") final Long restaurantId,
                                   @Context HttpServletRequest request) {
        User user = getLoggedUser();
        likesService.dislike(user.getId(), restaurantId);
        return Response.status(Response.Status.ACCEPTED).build();
    }

   //GET USER LIKE
    @GET
    @Path("/{restaurantId}/likes")
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response getRestaurantlikes(
            @PathParam("restaurantId") final Long restaurantId,
            @QueryParam("userId") final Long userId,
            @Context HttpServletRequest request) {

        Boolean like = likesService.userLikesRestaurant(userId,restaurantId);
        return Response.ok(new LikeDto(like)).build();
    }

    //RATE RESTAURANT
    @POST
    @Path("/{restaurantId}/ratings")
    @Produces( value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("!@authComponent.isRestaurantOwner(#restaurantId)")
    public Response rateRestaurant(@PathParam("restaurantId") final Long restaurantId,
                                   @Valid @NotNull RatingForm rating, @Context HttpServletRequest request) {

        User user = getLoggedUser();
        ratingService.rateRestaurant(user.getId(),restaurantId,rating.getRating());
        return Response.noContent().build();
    }

    //READ USER RATING
    @GET
    @Path("/{restaurantId}/ratings")
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response getRestaurantRate(@PathParam("restaurantId") final Long restaurantId,
                                      @QueryParam("userId") final Long userId,
                                      @Context HttpServletRequest request) {


        Optional<Rating> maybeRating = ratingService.getRating(userId, restaurantId);
        Double rate = maybeRating.isPresent() ? maybeRating.get().getRating() : 0;
        return Response.ok(new RatingDto(rate)).build();
    }
//
//    //READ RESTAURANT RESERVATIONS
//    @GET
//    @Path("/{restaurantId}/reservation")
//    @Produces( value = {MediaType.APPLICATION_JSON})
//    public Response findRestaurantReservations(@PathParam("restaurantId") final long restaurantId, @QueryParam("filterby")@DefaultValue("") String filterby, @QueryParam("page") @DefaultValue("1") Integer page, @Context HttpServletRequest request) {
//
//        Optional<User> user = getLoggedUser(request);
//        if(!user.isPresent()){
//            LOGGER.error("anon user attempt to register a restaurant");
//            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
//        }
//        if (!userService.isTheRestaurantOwner(user.get().getId(), restaurantId))
//            return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();
//
//        int maxPages;
//        List<ReservationDto> reservations;
//
//        if(filterby == "pending"){
//            maxPages = reservationService.findPendingByRestaurantPageCount(AMOUNT_OF_RESERVATIONS, restaurantId);
//            reservations = reservationService.findPendingByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId).stream().map(u -> ReservationDto.fromReservation(u, uriInfo)).collect(Collectors.toList());
//        }else if(filterby == "confirmed"){
//            maxPages = reservationService.findConfirmedByRestaurantPageCount(AMOUNT_OF_RESERVATIONS, restaurantId);
//            reservations = reservationService.findConfirmedByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId).stream().map(u -> ReservationDto.fromReservation(u, uriInfo)).collect(Collectors.toList());
//        }else{
//            maxPages = reservationService.findByRestaurantPageCount(AMOUNT_OF_RESERVATIONS, restaurantId);
//            reservations = reservationService.findByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId).stream().map(u -> ReservationDto.fromReservation(u, uriInfo)).collect(Collectors.toList());
//        }
//        return Response.ok(new GenericEntity<List<ReservationDto>>(reservations){})
//                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
//                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", maxPages).build(), "last")
//                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.max((page - 1), 1)).build(), "prev")
//                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", Math.min((page + 1), maxPages)).build(), "next")
//                .build();
//    }
//
//    //ADD RESERVATION
//    @POST
//    @Path("/{restaurantId}/reservation")
//    @Produces(value = {MediaType.APPLICATION_JSON})
//    @Consumes(value = { MediaType.APPLICATION_JSON})
//    public Response addRestaurantReservation(@PathParam("restaurantId") final long restaurantId, final ReservationDto reservationDto, @Context HttpServletRequest request){
//
//        Optional<User> user = getLoggedUser(request);
//        if(!user.isPresent()){
//            LOGGER.error("anon user attempt to register a restaurant");
//            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
//        }
//        String baseUrl = request.getHeader("Origin");
//        if (baseUrl == null) {
//            baseUrl = uriInfo.getBaseUri().toString();
//        }
//        final Reservation res = reservationService.addReservation(user.get().getId(), restaurantId, reservationDto.getDate(),reservationDto.getQuantity(),baseUrl);
//
//        final URI uri = uriInfo.getBaseUriBuilder().path("/"+restaurantId+"/reservation").build();
//        return Response.created(uri).build();
//    }
//
//    //DELETE RESERVATION
//    @DELETE
//    @Path("/{restaurantId}/reservation/{reservationId}")
//    @Produces(value = {MediaType.APPLICATION_JSON})
//    public Response deleteRestaurantReservation(@PathParam("restaurantId") final long restaurantId, @PathParam("reservationId") final long reservationId, @QueryParam("message")@DefaultValue("") String message, @Context HttpServletRequest request){
//
//        Optional<User> user = getLoggedUser(request);
//        if(!user.isPresent()){
//            LOGGER.error("Anon user attempt to delete a restaurant");
//            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
//        }
//        Optional<Reservation> reservation = reservationService.findById(reservationId);
//        if(!reservation.isPresent())
//            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
//
//        if(reservation.get().getUser().getId().equals(user.get().getId()))
//            reservationService.userCancelReservation(reservationId);
//        else if(userService.isTheRestaurantOwner(user.get().getId(), reservation.get().getRestaurant().getId()))
//            reservationService.ownerCancelReservation(reservationId, message);
//        else
//            return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();
//
//        return Response.status(Response.Status.ACCEPTED).build();
//    }
//
//    //CONFIRM RESERVATION
//    @PUT
//    @Path("/{restaurantId}/reservation/{reservationId}")
//    @Produces(value = {MediaType.APPLICATION_JSON})
//    public Response confirmRestaurantReservation(@PathParam("restaurantId") final long restaurantId, @PathParam("reservationId") final long reservationId, @Context HttpServletRequest request){
//
//        Optional<User> user = getLoggedUser(request);
//        if(!user.isPresent()){
//            LOGGER.error("anon user attempt to register a restaurant");
//            return Response.status(Response.Status.BAD_REQUEST).header("error", "error user not logged").build();
//        }
//        Optional<Reservation> reservation = reservationService.findById(reservationId);
//        if(!reservation.isPresent())
//            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
//
//        if(userService.isTheRestaurantOwner(user.get().getId(), reservation.get().getRestaurant().getId()))
//            reservationService.confirmReservation(reservationId);
//        else
//            return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();
//        return Response.status(Response.Status.ACCEPTED).build();
//    }




    private User getLoggedUser(){
        return userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                // This shouldn't happen as authority is handled before
                .orElseThrow(()-> new AccessDeniedException("Unauthorized"));
    }


}
