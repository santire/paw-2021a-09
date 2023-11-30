package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.model.ReservationStatus;

import javax.validation.constraints.NotNull;

public class ReservationStatusForm {
    String message;
    @NotNull
    ReservationStatus status;

    public ReservationStatusForm() {
    }

    public ReservationStatusForm(String status, String message) {
        this.status = ReservationStatus.fromString(status);
        this.message = message;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = ReservationStatus.fromString(status);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
