package ar.edu.itba.paw.model;

public enum ReservationType {
    HISTORY, CURRENT;

    public static ReservationType fromString(final String s) {
        return ReservationType.valueOf(s.toUpperCase());
    }
    }
