package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.exceptions.RatingNotFoundException;
import ar.edu.itba.paw.service.RatingService;
import ar.edu.itba.paw.webapp.dto.RatingDto;
import ar.edu.itba.paw.webapp.forms.RatingForm;
import ar.edu.itba.paw.webapp.forms.UpdateRatingForm;
import ar.edu.itba.paw.webapp.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("/users/{userId}/ratings")
public class RatingController {
    private static final int AMOUNT_OF_RATINGS = 10;
    @Autowired
    private RatingService ratingService;
    @Context
    private UriInfo uriInfo;
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isUser(#userId)")
    public Response getRatings(@PathParam("userId") final Long userId,
                               @QueryParam("page") @DefaultValue("1") Integer page,
                               @QueryParam("pageAmount") @DefaultValue("10") Integer pageAmount,
                               @QueryParam("restaurantId") final List<Long> restaurantIds) {
        if (restaurantIds != null && !restaurantIds.isEmpty()) {
            return Response.ok(getRatingByRestaurantIds(userId, restaurantIds)).build();
        }
        if (pageAmount > AMOUNT_OF_RATINGS || pageAmount < 1) {
            pageAmount = AMOUNT_OF_RATINGS;
        }

        final List<RatingDto> ratings = ratingService.getUserRatings(page, pageAmount, userId)
                .stream()
                .map(u -> RatingDto.fromRating(u, uriInfo))
                .collect(Collectors.toList());
        final int totalRatings = ratingService.getUserRatingsCount(userId);

        return PageUtils.paginatedResponse(new GenericEntity<List<RatingDto>>(ratings) {
        }, uriInfo, page, pageAmount, totalRatings);
    }

    private GenericEntity<List<RatingDto>> getRatingByRestaurantIds(Long userId, List<Long> restaurantIds) {
        final List<RatingDto> ratings = ratingService.getRatingsByRestaurant(userId, restaurantIds)
                .stream()
                .map(u -> RatingDto.fromRating(u, uriInfo))
                .collect(Collectors.toList());

        return new GenericEntity<List<RatingDto>>(ratings) {
        };
    }

    @GET
    @Path("/{restaurantId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRating(@PathParam("userId") final Long userId,
                              @PathParam("restaurantId") final Long restaurantId) {

        final Rating rating = ratingService.getRating(userId, restaurantId).orElseThrow(RatingNotFoundException::new);
        return Response.ok(RatingDto.fromRating(rating, uriInfo)).build();
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isUser(#userId) " +
            "and not @authComponent.isRestaurantOwner(#ratingForm.restaurantId)")
    public Response createRating(@PathParam("userId") final Long userId, @Valid @NotNull final RatingForm ratingForm) {

        final Rating rating = ratingService.rateRestaurant(userId, ratingForm.getRestaurantId(), ratingForm.getRating());
        // Create the location URI
        final URI uri = uriInfo.getAbsolutePathBuilder().path(rating.getRestaurant().getId().toString()).build();
        return Response.created(uri).entity(RatingDto.fromRating(rating, uriInfo)).build();
    }

    @PUT
    @Path("/{restaurantId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isUser(#userId) " +
            "and not @authComponent.isRestaurantOwner(#restaurantId)")
    public Response updateRating(@PathParam("userId") final Long userId,
                                 @PathParam("restaurantId") Long restaurantId,
                                 @Valid UpdateRatingForm ratingForm) {
        final Rating rating = ratingService.updateRating(userId, restaurantId, ratingForm.getRating());
        return Response.ok(RatingDto.fromRating(rating, uriInfo)).build();
    }

    @DELETE
    @Path("/{restaurantId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isUser(#userId) " +
            "and not @authComponent.isRestaurantOwner(#restaurantId)")
    public Response deleteRating(@PathParam("userId") final Long userId,
                                 @PathParam("restaurantId") Long restaurantId) {
        ratingService.deleteRating(userId, restaurantId);
        return Response.ok().build();
    }
}
