package ar.edu.itba.paw.model;

import java.sql.Timestamp;

public class VerificationToken {

    private String token;
    private Timestamp createdAt;
    private long userId;

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public VerificationToken(String token, Timestamp timestamp, long userId) {
        this.setToken(token);
        this.setCreatedAt(timestamp);
        this.userId = userId;
    }
}
