package ar.edu.itba.paw.model;

public enum ReservationStatus {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    DENIED("denied");

    private final String statusType;

    ReservationStatus(String statusType) {
        this.statusType = statusType;
    }

    public String getStatusType() {
        return statusType;
    }

    public static ReservationStatus fromString(final String s) {
        return ReservationStatus.valueOf(s.toUpperCase());
    }
}
