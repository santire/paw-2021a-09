package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Locale;

@Controller
public class ReservationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);
    private static final int AMOUNT_OF_RESERVATIONS = 2;


    @Autowired
    private ReservationService reservationService;


    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CommonAttributes ca;


    @RequestMapping(path = { "/reservations" }, method = RequestMethod.GET)
    public ModelAndView userReservations(@RequestParam(defaultValue="1") Integer page){

        final ModelAndView mav = new ModelAndView("myReservations");
        User loggedUser = ca.loggedUser();

        long userId = loggedUser.getId();
        int maxPages = reservationService.findByUserPageCount(AMOUNT_OF_RESERVATIONS, userId);

        if(page == null || page <1) {
            page=1;
        }else if (page > maxPages) {
            page = maxPages;
        }

        mav.addObject("maxPages", maxPages);

        List<Reservation> reservations = reservationService.findByUser(page, AMOUNT_OF_RESERVATIONS, userId);
        mav.addObject("userHasReservations", !reservations.isEmpty());
        mav.addObject("reservations", reservations);
        return mav;
     }

    @RequestMapping(path = { "/reservations/history" }, method = RequestMethod.GET)
    public ModelAndView userReservationsHistory(@RequestParam(defaultValue="1") Integer page){

        final ModelAndView mav = new ModelAndView("myReservationsHistory");
        User loggedUser = ca.loggedUser();

        long userId = loggedUser.getId();
        int maxPages = reservationService.findByUserHistoryPageCount(AMOUNT_OF_RESERVATIONS, userId);
        if(page == null || page <1) {
            page=1;
        }else if (page > maxPages) {
            page = maxPages;
        }
        mav.addObject("maxPages", maxPages);

        List<Reservation> reservations = reservationService.findByUserHistory(page, AMOUNT_OF_RESERVATIONS, userId);
        mav.addObject("userHasReservations", !reservations.isEmpty());
        mav.addObject("reservations", reservations);
        return mav;
    }


    @RequestMapping(path = "/reservations/{reservationId}/cancel", method = RequestMethod.POST)
    @PreAuthorize("@authComponent.isReservationUser(#reservationId)")
    public ModelAndView cancelReservation(@PathVariable("reservationId") final int reservationId){

        reservationService.userCancelReservation(reservationId);
        return new ModelAndView("redirect:/reservations");
    }


    @RequestMapping(path = "/reservations/{restaurantId}/{reservationId}/cancel", method = RequestMethod.POST)
    @PreAuthorize("@authComponent.isReservationOwner(#reservationId)")
    public ModelAndView cancelReservationFromOwner(@PathVariable("restaurantId") final long restaurantId,
                                                   @PathVariable("reservationId") final int reservationId,
                                                   @RequestParam("cancellationMessage") final String cancellationMessage){

        reservationService.ownerCancelReservation(reservationId, cancellationMessage);
        return new ModelAndView("redirect:/restaurant/" + restaurantId + "/manage/confirmed");
     }

    @RequestMapping(path = "/reservations/{restaurantId}/{reservationId}/reject", method = RequestMethod.POST)
    @PreAuthorize("@authComponent.isReservationOwner(#reservationId)")
    public ModelAndView rejectReservationFromOwner(@PathVariable("restaurantId") final long restaurantId,
                                                    @PathVariable("reservationId") final int reservationId){

        Locale locale = LocaleContextHolder.getLocale();
        reservationService.ownerCancelReservation(reservationId, messageSource.getMessage("mail.rejection",null,locale));
        return new ModelAndView("redirect:/restaurant/" + restaurantId + "/manage/pending");
     }

    @RequestMapping(path = "/reservations/{restaurantId}/{reservationId}/confirm", method = RequestMethod.POST)
    @PreAuthorize("@authComponent.isReservationOwner(#reservationId)")
    public ModelAndView confirmReservation(@PathVariable("restaurantId") final long restaurantId,
                                        @PathVariable("reservationId") final int reservationId){

        reservationService.confirmReservation(reservationId);
        return new ModelAndView("redirect:/restaurant/" + restaurantId + "/manage/pending");
    }

 }
