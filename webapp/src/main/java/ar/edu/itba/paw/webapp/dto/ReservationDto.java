package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;

public class ReservationDto {
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    private Long reservationId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime date;
    private long quantity;
    private boolean confirmed;

    private URI user;
    private URI restaurant;

    public static ReservationDto fromReservation(Reservation reservation, UriInfo uriInfo){
        final ReservationDto dto = new ReservationDto();

        dto.reservationId = reservation.getId();
        dto.date = reservation.getDate();
        dto.quantity = reservation.getQuantity();
        dto.confirmed = reservation.getConfirmed();
        dto.user = uriInfo.getBaseUriBuilder().path("users").path(reservation.getUser().getId().toString()).build();
        dto.restaurant = uriInfo.getBaseUriBuilder().path("restaurants").path(reservation.getRestaurant().getId().toString()).build();
//        dto.restaurant = uriInfo.getBaseUriBuilder().path("restaurants/"+reservation.getRestaurant().getId()).build();


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
