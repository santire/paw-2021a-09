package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.exceptions.ReservationNotFoundException;
import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.webapp.dto.CommentDto;
import ar.edu.itba.paw.webapp.forms.CommentForm;
import ar.edu.itba.paw.webapp.utils.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("/comments")
public class CommentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);
    private static final int AMOUNT_OF_COMMENTS = 10;

    @Autowired
    private CommentService commentService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{commentId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findCommentById(@PathParam("commentId") final long commentId) {
        final Comment comment = commentService.findById(commentId).orElseThrow(ReservationNotFoundException::new);
        final CommentDto commentDto = CommentDto.fromComment(comment, uriInfo);
        return Response.ok(new GenericEntity<CommentDto>(commentDto) {
        }).build();
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findComments(@QueryParam("madeBy") Long madeBy,
                                 @QueryParam("madeTo") Long madeTo,
                                 @QueryParam("page") @DefaultValue("1") Integer page,
                                 @QueryParam("pageAmount") @DefaultValue("10") Integer pageAmount,
                                 @Context HttpServletRequest request) {
        if (pageAmount > AMOUNT_OF_COMMENTS || pageAmount < 1) {
            pageAmount = AMOUNT_OF_COMMENTS;
        }

        List<CommentDto> comments = commentService.findComments(page, pageAmount, madeBy, madeTo)
                .stream()
                .map(u -> CommentDto.fromComment(u, uriInfo))
                .collect(Collectors.toList());

        int totalComments = commentService.findCommentsCount(madeBy, madeTo);

        return PageUtils.paginatedResponse(new GenericEntity<List<CommentDto>>(comments) {
        }, uriInfo, page, pageAmount, totalComments);
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("!@authComponent.isRestaurantOwner(#commentForm.restaurantId) && @authComponent.isUser(#commentForm.userId)")
    public Response createComment(@Valid @NotNull CommentForm commentForm) {

        final Comment comment = commentService.addComment(commentForm.getUserId(), commentForm.getRestaurantId(), commentForm.getMessage());

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(comment.getId())).build();
        return Response.created(uri).entity(CommentDto.fromComment(comment, uriInfo)).build();
    }

    @DELETE
    @Path("/{commentId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isCommentOwner(#commentId)")
    public Response deleteComment(
            @PathParam("commentId") final long commentId) {
        commentService.deleteComment(commentId);
        return Response.ok().build();
    }
}
