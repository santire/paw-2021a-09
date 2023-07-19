package ar.edu.itba.paw.webapp.forms;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import ar.edu.itba.paw.webapp.validators.PresentTime;
import ar.edu.itba.paw.webapp.validators.ValidTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@PresentTime(date = "date", time = "time", daysLimit = 7)
public class ReservationForm {


    @ValidTime(minHour = 12, maxHour = 23, stepMinutes = 30)
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull
    private LocalTime time;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NotNull
    private LocalDate date;

    @Min(1)
    @Max(15)
    @NotNull
    private Long quantity;

    public ReservationForm() {
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDate() { return date; }

    public void setLocalDate(LocalDate date) { this.date = date; }
    public void setDate(String date) { setLocalDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))); }
    public LocalTime getTime() {
        return this.time;
    }

    public void setLocalTime(LocalTime time) {
        this.time = time;
    }
    public void setTime(String time) { setLocalTime(LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME)); }

}
