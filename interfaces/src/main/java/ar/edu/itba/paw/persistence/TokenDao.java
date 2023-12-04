package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.RefreshToken;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface TokenDao {
    Optional<RefreshToken> findRefreshToken(String refreshToken);
    RefreshToken addRefreshToken(User user, RefreshToken token);
    void invalidateRefreshToken(RefreshToken token);
}
