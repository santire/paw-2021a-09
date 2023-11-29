package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;

public class ReservationDto {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime date;
    private long quantity;
    private ReservationStatus status;

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    private URI self;
    private URI user;
    private URI restaurant;

    public static ReservationDto fromReservation(Reservation reservation, UriInfo uriInfo) {
        final ReservationDto dto = new ReservationDto();

        dto.id = reservation.getId();
        dto.date = reservation.getDate();
        dto.quantity = reservation.getQuantity();
        dto.status = reservation.getStatus();
        dto.user = uriInfo.getBaseUriBuilder().path("users").path(reservation.getUser().getId().toString()).build();
        dto.restaurant = uriInfo.getBaseUriBuilder().path("restaurants").path(reservation.getRestaurant().getId().toString()).build();
        dto.self = uriInfo.getBaseUriBuilder().path("reservations").path(reservation.getId().toString()).build();

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }


    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }

    public URI getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(URI restaurant) {
        this.restaurant = restaurant;
    }
}
