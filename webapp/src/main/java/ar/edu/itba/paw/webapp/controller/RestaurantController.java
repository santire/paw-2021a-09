package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.CommentNotFoundException;
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
import java.util.*;
import java.util.stream.Collectors;


@Component
@Path("/restaurants")
public class RestaurantController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantController.class);
    private static final int AMOUNT_OF_MENU_ITEMS = 8;
    private static final int MAX_AMOUNT_PER_PAGE = 10;

    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;


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



    //REGISTER RESTAURANT
    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response registerRestaurant(final @Valid @NotNull RegisterRestaurantForm restaurantForm,
                                       @Context HttpServletRequest request) {
        // TODO: pass userId in restaurantForm, check isUser with preAuth
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


    private User getLoggedUser() {
        return userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                // This shouldn't happen as authority is handled before
                .orElseThrow(() -> new AccessDeniedException("Unauthorized"));
    }


}
