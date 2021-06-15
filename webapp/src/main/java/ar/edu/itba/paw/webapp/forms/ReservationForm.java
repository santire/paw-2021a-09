package ar.edu.itba.paw.webapp.forms;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import ar.edu.itba.paw.webapp.validators.PresentTime;
import ar.edu.itba.paw.webapp.validators.ValidTime;

import java.time.LocalDate;
import java.time.LocalTime;

@PresentTime(date = "date", time = "time", daysLimit = 7)
public class ReservationForm {


    @ValidTime(minHour = 12, maxHour = 23, stepMinutes = 30)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @Size(min=1, max=15)
    @NumberFormat
    @Pattern(regexp = "[0-9]+")
    private String quantity;

    @Email
    private String email;

    public ReservationForm() {
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() {
        return this.time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
