package ar.edu.itba.paw.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class VerificationToken {

    private String token;
    private LocalDateTime createdAt;
    private long userId;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public VerificationToken(String token, LocalDateTime timestamp, long userId) {
        this.setToken(token);
        this.setCreatedAt(timestamp);
        this.userId = userId;
    }

    public VerificationToken(String token, Timestamp timestamp, long userId) {
        this.setToken(token);
        this.setCreatedAt(timestamp.toLocalDateTime());
        this.userId = userId;
    }
}
