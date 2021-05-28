package ar.edu.itba.paw.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_tokens_token_id_seq")
    @SequenceGenerator(sequenceName = "verification_tokens_token_id_seq", name = "verification_tokens_token_id_seq", allocationSize = 1)
    Long id;

    @Column(length = 36, unique = true)
    private String token;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    VerificationToken() {
        // Just for hibernate
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VerificationToken(String token, LocalDateTime timestamp, User user) {
        this.setToken(token);
        this.setCreatedAt(timestamp);
        this.user = user;
    }

    public VerificationToken(String token, Timestamp timestamp, User user) {
        this.setToken(token);
        this.setCreatedAt(timestamp.toLocalDateTime());
        this.user = user;
    }
}
