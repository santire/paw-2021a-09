package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Reservation;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;

public class ReservationDto {

    private LocalDateTime date;
    private long quantity;
    private boolean confirmed;

    private URI user;
    private URI restaurant;

    public static ReservationDto fromReservation(Reservation reservation, UriInfo uriInfo){
        final ReservationDto dto = new ReservationDto();

        dto.date = reservation.getDate();
        dto.quantity = reservation.getQuantity();
        dto.confirmed = reservation.getConfirmed();

        dto.user = uriInfo.getBaseUriBuilder().path("users/"+ reservation.getUser().getId()).build();
        dto.restaurant = uriInfo.getBaseUriBuilder().path("restaurants/"+reservation.getRestaurant().getId()).build();


        return  dto;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public long getQuantity() {
        return quantity;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public URI getUser() {
        return user;
    }

    public URI getRestaurant() {
        return restaurant;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public void setUser(URI user) {
        this.user = user;
    }

    public void setRestaurant(URI restaurant) {
        this.restaurant = restaurant;
    }
}
