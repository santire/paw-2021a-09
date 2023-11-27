package ar.edu.itba.paw.model;

public enum ReservationStatus {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    HISTORY("history");

    private final String statusType;

    ReservationStatus(String statusType) {
        this.statusType = statusType;
    }

    public String getStatusType() {
        return statusType;
    }
}
