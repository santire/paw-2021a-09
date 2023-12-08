package ar.edu.itba.paw.persistence.models;

import org.springframework.jdbc.core.RowMapper;

public class TokenRow {
    public static RowMapper<TokenRow> rowMapper = ((rs, rowNum) -> new TokenRow(
            rs.getLong("token_id"),
            rs.getString("token"),
            rs.getString("created_at"),
            rs.getLong("user_id")
    ));
    private Long id;
    private String token;
    private String createdAt;

    private Long userId;

    public TokenRow(Long id, String token, String createdAt, Long userId) {
        this.id = id;
        this.token = token;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
