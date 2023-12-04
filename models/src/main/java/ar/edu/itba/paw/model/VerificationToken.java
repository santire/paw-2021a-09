package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_tokens_token_id_seq")
    @SequenceGenerator(sequenceName = "verification_tokens_token_id_seq", name = "verification_tokens_token_id_seq", allocationSize = 1)
    private Long id;
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


    public VerificationToken(String token, LocalDateTime timestamp, User user) {
        this.setToken(token);
        this.setCreatedAt(timestamp);
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}