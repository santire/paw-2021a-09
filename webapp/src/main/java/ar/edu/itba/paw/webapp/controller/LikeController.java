package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.service.LikesService;
import ar.edu.itba.paw.webapp.dto.LikeDto;
import ar.edu.itba.paw.webapp.forms.LikeForm;
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
@Path("/users/{userId}/likes")
public class LikeController {

    private static final int AMOUNT_OF_LIKES = 10;

    @Autowired
    private LikesService likesService;
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isUser(#userId)")
    public Response getUserLikes(@PathParam("userId") final Long userId,
                                         @QueryParam("page") @DefaultValue("1") Integer page,
                                         @QueryParam("pageAmount") @DefaultValue("10") Integer pageAmount,
                                         @QueryParam("restaurantId") final List<Long> restaurantIds) {

        if (restaurantIds != null && !restaurantIds.isEmpty()) {
            return Response.ok(getUserLikesByRestaurantIds(userId, restaurantIds)).build();
        }
        if (pageAmount > AMOUNT_OF_LIKES || pageAmount < 1) {
            pageAmount = AMOUNT_OF_LIKES;
        }

        final List<LikeDto> likes = likesService.getUserLikes(page, pageAmount, userId)
                .stream()
                .map(u -> LikeDto.fromLike(u, uriInfo))
                .collect(Collectors.toList());
        final int totalLikes = likesService.getUserLikesCount(userId);

        return PageUtils.paginatedResponse(new GenericEntity<List<LikeDto>>(likes) {
        }, uriInfo, page, pageAmount, totalLikes);
    }

    private GenericEntity<List<LikeDto>> getUserLikesByRestaurantIds(Long userId, List<Long> restaurantIds) {
        final List<LikeDto> likes = likesService.userLikesRestaurants(userId, restaurantIds)
                .stream()
                .map(u -> LikeDto.fromLike(u, uriInfo))
                .collect(Collectors.toList());

        return new GenericEntity<List<LikeDto>>(likes) {
        };
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isUser(#userId) and !@authComponent.isRestaurantOwner(#likeForm.restaurantId)")
    public Response likeRestaurant(@PathParam("userId") final Long userId, @Valid @NotNull final LikeForm likeForm) {

        final Like like = likesService.like(userId, likeForm.getRestaurantId());
        // Create the location URI
        final URI uri = uriInfo.getAbsolutePathBuilder().path(like.getRestaurant().getId().toString()).build();
        return Response.created(uri).entity(LikeDto.fromLike(like, uriInfo)).build();
    }

    @DELETE
    @Path("/{restaurantId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isUser(#userId) and !@authComponent.isRestaurantOwner(#restaurantId)")
    public Response dislikeRestaurant(@PathParam("userId") final Long userId,
                                      @PathParam("restaurantId") final Long restaurantId) {
        likesService.dislike(userId, restaurantId);
        return Response.ok().build();
    }
}
