package ar.edu.itba.paw.webapp.forms;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

public class ReservationForm {

    @DateTimeFormat
    private String date;

    @NumberFormat
    private String quantity;

    public ReservationForm() {
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
