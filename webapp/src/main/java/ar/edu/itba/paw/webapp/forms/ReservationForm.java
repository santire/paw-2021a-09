package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validators.PresentTime;
import ar.edu.itba.paw.webapp.validators.ValidTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@PresentTime(date = "date", daysLimit = 7)
public class ReservationForm {

    @ValidTime(minHour = 12, maxHour = 23, stepMinutes = 30)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull
    private LocalDateTime date;

    @Min(1)
    @Max(15)
    @NotNull
    private Long quantity;
    @NotNull
    private Long userId;
    @NotNull
    private Long restaurantId;

    public ReservationForm() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

}
