package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.ReservationStatus;
import ar.edu.itba.paw.model.ReservationType;
import ar.edu.itba.paw.model.exceptions.InvalidParameterException;
import ar.edu.itba.paw.model.exceptions.ReservationNotFoundException;
import ar.edu.itba.paw.service.ReservationService;
import ar.edu.itba.paw.webapp.dto.ReservationDto;
import ar.edu.itba.paw.webapp.forms.ReservationForm;
import ar.edu.itba.paw.webapp.forms.ReservationStatusForm;
import ar.edu.itba.paw.webapp.methods.PATCH;
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

;

@Component
@Path("/reservations")
public class ReservationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);
    private static final int AMOUNT_OF_RESERVATIONS = 10;

    @Autowired
    private ReservationService reservationService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{reservationId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findReservationById(@PathParam("reservationId") final long reservationId) {
        final Reservation reservation = reservationService.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        final ReservationDto reservationDto = ReservationDto.fromReservation(reservation, uriInfo);
        return Response.ok(new GenericEntity<ReservationDto>(reservationDto) {
        }).build();
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findReservations(@QueryParam("madeBy") String madeBy, @QueryParam("madeTo") String madeTo, @QueryParam("status") ReservationStatus status, @QueryParam("type") ReservationType type, @QueryParam("page") @DefaultValue("1") Integer page, @QueryParam("pageAmount") @DefaultValue("10") Integer pageAmount, @Context HttpServletRequest request) {
        if (pageAmount > AMOUNT_OF_RESERVATIONS || pageAmount < 1) {
            pageAmount = AMOUNT_OF_RESERVATIONS;
        }

        Long userId;
        try {
            userId = Long.valueOf(madeBy);
        } catch (NumberFormatException e) {
            userId = null;
        }

        Long restaurantId;
        try {
            restaurantId = Long.valueOf(madeTo);
        } catch (NumberFormatException e) {
            restaurantId = null;
        }

        LOGGER.debug("Filtering reservations by: {}", status);
        List<ReservationDto> reservations = reservationService.findReservations(page, pageAmount, userId, restaurantId, status, type).stream().map(u -> ReservationDto.fromReservation(u, uriInfo)).collect(Collectors.toList());
        int totalReservations = reservationService.findReservationsCount(userId, restaurantId, status, type);

        return PageUtils.paginatedResponse(new GenericEntity<List<ReservationDto>>(reservations) {
        }, uriInfo, page, AMOUNT_OF_RESERVATIONS, totalReservations);
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("!@authComponent.isRestaurantOwner(#reservationForm.restaurantId) && @authComponent.isUser(#reservationForm.userId)")
    public Response addRestaurantReservation(@Valid @NotNull ReservationForm reservationForm, @Context HttpServletRequest request) {
        URI baseUri = URI.create(request.getRequestURL().toString()).resolve(request.getContextPath());

        final String ownerUrl = UriBuilder.fromUri(baseUri).path("restaurants").path(reservationForm.getRestaurantId().toString()).path("reservations").queryParam("tab", "pending").build().toString();
        final String userUrl = UriBuilder.fromUri(baseUri).path("user").path("reservations").build().toString();

        final Reservation res = reservationService.addReservation(reservationForm.getUserId(), reservationForm.getRestaurantId(), reservationForm.getDate(), reservationForm.getQuantity(), ownerUrl, userUrl);

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(res.getId())).build();
        return Response.created(uri).entity(ReservationDto.fromReservation(res, uriInfo)).build();
    }

    @PATCH
    @Path("/{reservationId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response confirmReservation(@PathParam("reservationId") final Long reservationId, @Valid final ReservationStatusForm statusForm) {
        switch (statusForm.getStatus()) {
            case DENIED:
                String message = statusForm.getMessage();
                if (message == null || message.length() < 10) {
                    throw new InvalidParameterException("message", "message should have at least 10 characters");
                }
                reservationService.ownerCancelReservation(reservationId, message);
                break;
            case CONFIRMED:
                reservationService.confirmReservation(reservationId);
                break;
            default:
                throw new InvalidParameterException("status", "Can only be 'confirmed' or 'denied' ");
        }

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{reservationId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isReservationUser(#reservationId)")
    public Response cancelRestaurantReservation(
            @PathParam("reservationId") final long reservationId) {
        reservationService.userCancelReservation(reservationId);
        return Response.noContent().build();
    }
}
