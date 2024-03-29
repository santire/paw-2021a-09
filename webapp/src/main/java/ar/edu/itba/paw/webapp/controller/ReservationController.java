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
    public Response findReservationById(@PathParam("reservationId") final Long reservationId) {
        final Reservation reservation = reservationService
                .findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        final ReservationDto reservationDto = ReservationDto.fromReservation(reservation, uriInfo);
        return Response.ok(new GenericEntity<ReservationDto>(reservationDto) {
        }).build();
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findReservations(@QueryParam("madeBy") final Long madeBy,
                                     @QueryParam("madeTo") final Long madeTo,
                                     @QueryParam("status") ReservationStatus status,
                                     @QueryParam("type") ReservationType type,
                                     @QueryParam("page") @DefaultValue("1") Integer page,
                                     @QueryParam("pageAmount") @DefaultValue("10") Integer pageAmount) {
        if (pageAmount > AMOUNT_OF_RESERVATIONS || pageAmount < 1) {
            pageAmount = AMOUNT_OF_RESERVATIONS;
        }

        LOGGER.debug("Filtering reservations by: {}", status);
        final List<ReservationDto> reservations = reservationService
                .findReservations(page, pageAmount, madeBy, madeTo, status, type)
                .stream()
                .map(u -> ReservationDto.fromReservation(u, uriInfo))
                .collect(Collectors.toList());

        final int totalReservations = reservationService.findReservationsCount(madeBy, madeTo, status, type);

        return PageUtils.paginatedResponse(new GenericEntity<List<ReservationDto>>(reservations) {
        }, uriInfo, page, pageAmount, totalReservations);
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("!@authComponent.isRestaurantOwner(#reservationForm.restaurantId) " +
            "and @authComponent.isUser(#reservationForm.userId)")
    public Response addRestaurantReservation(@Valid @NotNull final ReservationForm reservationForm,
                                             @Context HttpServletRequest request) {
        final URI baseUri = URI.create(request.getRequestURL().toString()).resolve(request.getContextPath());

        final Reservation res = reservationService.addReservation(
                reservationForm.getUserId(),
                reservationForm.getRestaurantId(),
                reservationForm.getDate(),
                reservationForm.getQuantity(),
                baseUri);

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(res.getId())).build();
        return Response.created(uri).entity(ReservationDto.fromReservation(res, uriInfo)).build();
    }

    @PATCH
    @Path("/{reservationId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isReservationOwner(#reservationId)")
    public Response updateReservationStatus(@PathParam("reservationId") final Long reservationId,
                                            @Valid @NotNull final ReservationStatusForm statusForm) {
        Reservation reservation;
        switch (statusForm.getStatus()) {
            case DENIED:
                String message = statusForm.getMessage();
                if (message == null || message.length() < 10) {
                    throw new InvalidParameterException("message", "message should have at least 10 characters");
                }
                reservation = reservationService.ownerCancelReservation(reservationId, message);
                break;
            case CONFIRMED:
                reservation = reservationService.confirmReservation(reservationId);
                break;
            default:
                throw new InvalidParameterException("status", "Can only be 'confirmed' or 'denied' ");
        }

        return Response.ok().entity(ReservationDto.fromReservation(reservation, uriInfo)).build();
    }

    @DELETE
    @Path("/{reservationId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isReservationUser(#reservationId)")
    public Response cancelRestaurantReservation(
            @PathParam("reservationId") final Long reservationId) {
        reservationService.userCancelReservation(reservationId);
        return Response.ok().build();
    }
}
