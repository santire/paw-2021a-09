package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.CommentNotFoundException;
import ar.edu.itba.paw.model.exceptions.EmptyBodyException;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.forms.*;
import ar.edu.itba.paw.webapp.utils.CachingUtils;
import ar.edu.itba.paw.webapp.utils.PageUtils;
import ar.edu.itba.paw.webapp.validators.ImageFileValidator;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


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
    private RatingService ratingService;
    @Autowired
    private LikesService likesService;
    @Autowired
    private ReservationService reservationService;
    @Context
    private Validator validator;

    @Context
    private UriInfo uriInfo;

    //READ RESTAURANTS
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRestaurants(@QueryParam("page") @DefaultValue("1") Integer page,
                                   @QueryParam("pageAmount") @DefaultValue("10") Integer pageAmount,
                                   @QueryParam("search") @DefaultValue("") String search,
                                   @QueryParam("tags") List<Integer> tags,
                                   @QueryParam("min") @DefaultValue("1") Integer min,
                                   @QueryParam("max") @DefaultValue("10000") Integer max,
                                   @QueryParam("sort") @DefaultValue("name") String sortBy,
                                   @QueryParam("order") @DefaultValue("asc") String order,
                                   @QueryParam("filterBy") @DefaultValue("") String filterBy) {

        if (filterBy.equalsIgnoreCase("hot")) {
            List<RestaurantDto> restaurants = restaurantService.getHotRestaurants(pageAmount, 365)
                    .stream()
                    .map(u -> RestaurantDto.fromRestaurant(u, uriInfo))
                    .collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<RestaurantDto>>(restaurants) {
            }).build();
        }
        if (filterBy.equalsIgnoreCase("popular")) {
            List<RestaurantDto> restaurants = restaurantService.getPopularRestaurants(pageAmount, 2)
                    .stream()
                    .map(u -> RestaurantDto.fromRestaurant(u, uriInfo))
                    .collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<RestaurantDto>>(restaurants) {
            }).build();
        }

        if (!Objects.equals(search, "")) {
            search = search.trim().replaceAll("[^a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+", "");
        }

        List<Tags> tagsSelected = new ArrayList<>();
//        List<Integer> tagsChecked = new ArrayList<>();
        if (tags != null) {
            for (int i : tags) {
                if (Tags.valueOf(i) == null) {
                    LOGGER.warn("Tag {} does not exist. Ignoring...", i);
                } else {
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

        boolean desc = order != null && order.equalsIgnoreCase("DESC");
        if (pageAmount > MAX_AMOUNT_PER_PAGE) {
            pageAmount = MAX_AMOUNT_PER_PAGE;
        }

        int totalRestaurants = restaurantService.getRestaurantsFilteredByCount(search, tagsSelected, min, max);
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsFilteredBy(
                        page, pageAmount, search, tagsSelected, min, max, sort, desc, 7)
                .stream()
                .map(u -> RestaurantDto.fromRestaurant(u, uriInfo))
                .collect(Collectors.toList());
        LOGGER.info(String.valueOf(restaurants.size()));

        return PageUtils.paginatedResponse(new GenericEntity<List<RestaurantDto>>(restaurants) {
        }, uriInfo, page, pageAmount, totalRestaurants);
    }

    //READ A RESTAURANT
    @GET
    @Path("/{restaurantId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findRestaurantByID(@PathParam("restaurantId") final long restaurantId) {
        final Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        final RestaurantDto restaurantDto = RestaurantDto.fromRestaurant(restaurant, uriInfo);
        return Response.ok(new GenericEntity<RestaurantDto>(restaurantDto) {
        }).build();
    }

    @GET
    @Path("/{restaurantId}/image")
    @Produces(value = {MediaType.APPLICATION_JSON, "image/jpg"})
    public Response getRestaurantImage(@PathParam("restaurantId") final long restaurantId) throws IOException {
        CacheControl cache = CachingUtils.getCaching(24*CachingUtils.HOUR_TO_SEC);
        Date expireDate = CachingUtils.getExpirationDate(24*CachingUtils.HOUR_TO_SEC);
        final Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        final Image image = restaurant.getProfileImage();
        LOGGER.debug("Image: {}", image);
        final byte[] imageData = image != null ? image.getData() : Image.getPlaceholderImage();

        return Response.ok(imageData)
                .header("Content-Type", "image/jpg")
                .cacheControl(cache).expires(expireDate).build();
    }

    //READ RESTAURANT MENU
    @GET
    @Path("/{restaurantId}/menu")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findRestaurantMenu(@PathParam("restaurantId") final long restaurantId, @QueryParam("page") @DefaultValue("1") Integer page) {

        int amountOfMenuItems = restaurantService.findByIdWithMenuCount(restaurantId);
        LOGGER.debug("Amount of menu items: {}", amountOfMenuItems);
        final Restaurant restaurant = restaurantService.findByIdWithMenu(restaurantId, page, AMOUNT_OF_MENU_ITEMS)
                .orElseThrow(RestaurantNotFoundException::new);

        List<MenuItemDto> menu = restaurant.getMenu()
                .stream()
                .map(MenuItemDto::fromMenuItem)
                .collect(Collectors.toList());

        return PageUtils.paginatedResponse(new GenericEntity<List<MenuItemDto>>(menu) {
        }, uriInfo, page, AMOUNT_OF_MENU_ITEMS, amountOfMenuItems);
    }

    //READ RESTAURANT REVIEWS
    @GET
    @Path("/{restaurantId}/reviews")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findRestaurantReviews(@PathParam("restaurantId") final long restaurantId, @QueryParam("page") @DefaultValue("1") Integer page) {
        int amountOfReviews = commentService.findByRestaurantCount(restaurantId);

        List<CommentDto> reviews = commentService.findByRestaurant(page, AMOUNT_OF_REVIEWS, restaurantId)
                .stream()
                .map(u -> CommentDto.fromComment(u, uriInfo))
                .collect(Collectors.toList());


        return PageUtils.paginatedResponse(new GenericEntity<List<CommentDto>>(reviews) {
        }, uriInfo, page, AMOUNT_OF_REVIEWS, amountOfReviews);
    }

    //CREATE REVIEW
    @POST
    @Path("/{restaurantId}/reviews")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("!@authComponent.isRestaurantOwner(#restaurantId)")
    public Response addRestaurantReview(@PathParam("restaurantId") final Long restaurantId,
                                        final @Valid @NotNull CommentForm comment, @Context HttpServletRequest request) {
        User user = getLoggedUser();
        LOGGER.debug("review: {}", comment.getReview());
        final Comment rev = commentService.addComment(user.getId(), restaurantId, comment.getReview());
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(rev.getId())).build();
        return Response.created(uri).entity(CommentDto.fromComment(rev, uriInfo)).build();
    }

    //DELETE REVIEW
    @DELETE
    @Path("/{restaurantId}/reviews/{reviewId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isReviewOwner(#reviewId) OR hasRole('ROLE_ADMIN')")
    public Response deleteRestaurantReview(@PathParam("restaurantId") final Long restaurantId,
                                           @PathParam("reviewId") final Long reviewId, @Context HttpServletRequest request) {
        Comment review = commentService.findById(reviewId).orElseThrow(CommentNotFoundException::new);
        commentService.deleteComment(reviewId);
        return Response.noContent().build();
    }

    //ADD MENU ITEM
    @POST
    @Path("/{restaurantId}/menu")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isRestaurantOwner(#restaurantId)")
    public Response addRestaurantMenuItem(@PathParam("restaurantId") final Long restaurantId,
                                          final @Valid @NotNull MenuItemForm menuItem, @Context HttpServletRequest request) {
        final MenuItem item = new MenuItem(menuItem.getId(), menuItem.getName(), menuItem.getDescription(), menuItem.getPrice());
        final MenuItem createdItem = menuService.addItemToRestaurant(restaurantId, item);
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(createdItem.getId())).build();
        return Response.created(uri).entity(MenuItemDto.fromMenuItem(createdItem)).build();
    }

    //DELETE MENU ITEM
    @DELETE
    @Path("/{restaurantId}/menu/{menuId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    // Restaurant owner and menu item belongs to said restaurant
    @PreAuthorize("@authComponent.isRestaurantAndMenuOwner(#restaurantId, #menuId) or hasRole('ROLE_ADMIN')")
    public Response deleteRestaurantMenuItem(@PathParam("restaurantId") final Long restaurantId,
                                             @PathParam("menuId") final Long menuId, @Context HttpServletRequest request) {
        menuService.deleteItemById(menuId);
        return Response.noContent().build();
    }

    //REGISTER RESTAURANT
    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response registerRestaurant(final @Valid @NotNull RegisterRestaurantForm restaurantForm,
                                       @Context HttpServletRequest request) {
        User user = getLoggedUser();
        List<Tags> tagList = new ArrayList<>();
        if (restaurantForm.getTags() != null) {
            tagList = Arrays
                    .stream(restaurantForm.getTags())
                    .map(Tags::valueOf)
                    .collect(Collectors.toList());
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
        return Response.created(uri).entity(RestaurantDto.fromRestaurant(restaurant, uriInfo)).build();
    }

    @PUT
    @Path("/{restaurantId}/image")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    @PreAuthorize("@authComponent.isRestaurantOwner(#restaurantId)")
    public Response setImage(@PathParam("restaurantId") final Long restaurantId,
                             @FormDataParam("image") final FormDataBodyPart body,
                             @FormDataParam("image") final byte[] bytes,
                             @Context HttpServletRequest request) throws IOException {
        // Throws InvalidImageException if not valid
        ImageFileValidator imageFileValidator = new ImageFileValidator();
        imageFileValidator.isValid(body, null);
        String contentType = body.getMediaType().toString();
        LOGGER.debug("Image type: {}", contentType);
        LOGGER.debug("Image size: {}", bytes.length / 1024);
        Image image = new Image(bytes);
        LOGGER.debug("Created image: {}", image);
        restaurantService.setImageByRestaurantId(image, restaurantId);
        final URI uri = uriInfo.getAbsolutePathBuilder().build();
        return Response.ok(uri).entity(bytes).build();
    }

    @PUT
    @Path("/{restaurantId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isRestaurantOwner(#restaurantId)")
    public Response updateRestaurant(
            @PathParam("restaurantId") final Long restaurantId,
            final @Valid @NotNull RegisterRestaurantForm restaurantForm,
            @Context HttpServletRequest request) {
        List<Tags> tagList = new ArrayList<>();
        if (restaurantForm.getTags() != null) {
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
                                     @Context HttpServletRequest request) {
        restaurantService.deleteRestaurantById(restaurantId);
        return Response.noContent().build();
    }


    //LIKE RESTAURANT
    @POST
    @Path("/{restaurantId}/likes")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("!@authComponent.isRestaurantOwner(#restaurantId)")
    public Response likeRestaurant(@PathParam("restaurantId") final Long restaurantId,
                                   @Context HttpServletRequest request) {
        User user = getLoggedUser();
        likesService.like(user.getId(), restaurantId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{restaurantId}/likes")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("!@authComponent.isRestaurantOwner(#restaurantId)")
    public Response dislikeRestaurant(@PathParam("restaurantId") final Long restaurantId,
                                      @Context HttpServletRequest request) {
        User user = getLoggedUser();
        likesService.dislike(user.getId(), restaurantId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    //RATE RESTAURANT
    @POST
    @Path("/{restaurantId}/ratings")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("!@authComponent.isRestaurantOwner(#restaurantId)")
    public Response rateRestaurant(@PathParam("restaurantId") final Long restaurantId,
                                   @Valid @NotNull RatingForm rating, @Context HttpServletRequest request) {
        User user = getLoggedUser();
        ratingService.rateRestaurant(user.getId(), restaurantId, rating.getRating());
        return Response.noContent().build();
    }



    //READ RESTAURANT RESERVATIONS
    @GET
    @Path("/{restaurantId}/reservations")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isRestaurantOwner(#restaurantId)")
    public Response findRestaurantReservations(@PathParam("restaurantId") final Long restaurantId,
                                               @QueryParam("filterBy") @DefaultValue("") String filterBy,
                                               @QueryParam("page") @DefaultValue("1") Integer page,
                                               @Context HttpServletRequest request) {
        int totalReservations;
        List<ReservationDto> reservations;
        LOGGER.debug("Filtering reservations by: {}", filterBy);
        if (filterBy.equalsIgnoreCase("pending")) {
            totalReservations = reservationService.findPendingByRestaurantCount(restaurantId);
            reservations = reservationService.findPendingByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId).stream().map(u -> ReservationDto.fromReservation(u, uriInfo)).collect(Collectors.toList());
        } else if (filterBy.equalsIgnoreCase("confirmed")) {
            totalReservations = reservationService.findConfirmedByRestaurantCount( restaurantId);
            reservations = reservationService.findConfirmedByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId).stream().map(u -> ReservationDto.fromReservation(u, uriInfo)).collect(Collectors.toList());
        } else if (filterBy.equalsIgnoreCase("history")) {
            totalReservations = reservationService.findHistoryByRestaurantCount(restaurantId);
            reservations = reservationService.findHistoryByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId).stream().map(u -> ReservationDto.fromReservation(u, uriInfo)).collect(Collectors.toList());
        } else {
            totalReservations = reservationService.findByRestaurantCount(restaurantId);
            reservations = reservationService.findByRestaurant(page, AMOUNT_OF_RESERVATIONS, restaurantId).stream().map(u -> ReservationDto.fromReservation(u, uriInfo)).collect(Collectors.toList());
        }
        return PageUtils.paginatedResponse(new GenericEntity<List<ReservationDto>>(reservations) {
        }, uriInfo, page, AMOUNT_OF_RESERVATIONS, totalReservations);
    }

    //ADD RESERVATION
    @POST
    @Path("/{restaurantId}/reservations")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("!@authComponent.isRestaurantOwner(#restaurantId)")
    public Response addRestaurantReservation(
            @PathParam("restaurantId") final Long restaurantId,
            @Valid @NotNull ReservationForm reservationDto, @Context HttpServletRequest request) {
        User user = getLoggedUser();
        String baseUrl = request.getHeader("Origin");
        if (baseUrl == null) {
            baseUrl = uriInfo.getBaseUri().toString();
        }
        final Reservation res = reservationService.addReservation(
                user.getId(),
                restaurantId,
                LocalDateTime.of(reservationDto.getDate(), reservationDto.getTime()),
                reservationDto.getQuantity(),
                baseUrl);

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(res.getId())).build();
        return Response.created(uri).entity(ReservationDto.fromReservation(res, uriInfo)).build();
    }

    // USER CANCEL RESERVATION
    @DELETE
    @Path("/{restaurantId}/reservations/{reservationId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isReservationUser(#reservationId)")
    public Response cancelRestaurantReservation(
            @PathParam("restaurantId") final Long restaurantId,
            @PathParam("reservationId") final long reservationId,
            @Context HttpServletRequest request) {
        reservationService.userCancelReservation(reservationId);
        return Response.noContent().build();
    }

    // OWNER CONFIRM OR DENY RESERVATION
    @PUT
    @Path("/{restaurantId}/reservations/{reservationId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isReservationOwner(#reservationId)")
    public Response confirmRestaurantReservation(@PathParam("restaurantId") final Long restaurantId,
                                                 @PathParam("reservationId") final Long reservationId,
                                                 @QueryParam("action") final String action,
                                                 @Valid final CancelMessageForm cancelMessageForm,
                                                 @Context HttpServletRequest request) {
        if (action.equalsIgnoreCase("confirm")) {
            reservationService.confirmReservation(reservationId);
        } else if (action.equalsIgnoreCase("deny") && cancelMessageForm != null) {
            reservationService.ownerCancelReservation(reservationId, cancelMessageForm.getMessage());
        } else {
            throw new EmptyBodyException();
        }
        return Response.noContent().build();
    }


    private User getLoggedUser() {
        return userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                // This shouldn't happen as authority is handled before
                .orElseThrow(() -> new AccessDeniedException("Unauthorized"));
    }


}
