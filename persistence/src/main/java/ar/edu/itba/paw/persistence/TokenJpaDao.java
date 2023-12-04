package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.RefreshToken;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class TokenJpaDao implements TokenDao {
    @PersistenceContext
    EntityManager em;

    @Override
    public Optional<RefreshToken> findRefreshToken(String refreshToken) {
        final TypedQuery<RefreshToken> query = em.createQuery("from RefreshToken u where u.jti = :token", RefreshToken.class);
        query.setParameter("token", refreshToken);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public RefreshToken addRefreshToken(User user, RefreshToken token) {
        token.setUser(user);
        try {
            em.persist(token);
            return token;
        } catch (Exception e) {
            throw new TokenCreationException();
        }
    }

    @Override
    public void invalidateRefreshToken(RefreshToken refreshToken) {
        em.remove(refreshToken);
    }
}
