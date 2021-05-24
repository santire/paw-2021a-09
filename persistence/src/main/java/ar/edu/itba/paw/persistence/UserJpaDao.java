package ar.edu.itba.paw.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;

@Repository
public class UserJpaDao implements UserDao {

    @PersistenceContext
    EntityManager em;

    // CREATE

    @Override
    public User register(String username, String password, String firstName, String lastName, String email,
            String phone) throws EmailInUseException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void assignTokenToUser(String token, LocalDateTime createdAt, long userId) throws TokenCreationException {
        // TODO Auto-generated method stub

    }

    @Override
    public void assignPasswordTokenToUser(String token, LocalDateTime createdAt, long userId)
            throws TokenCreationException {
        // TODO Auto-generated method stub

    }

    // READ

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        final TypedQuery<User> query = em.createQuery("from User u where u.email = :email", User.class);
        query.setParameter("email", email);
        final List<User> list = query.getResultList();
        return Optional.ofNullable(list.isEmpty() ? null : list.get(0));
    }

    @Override
    public Optional<VerificationToken> getToken(String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<VerificationToken> getPasswordToken(String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Deprecated
    public boolean isTheRestaurantOwner(long userId, long restaurantId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    @Deprecated
    public boolean isRestaurantOwner(long userId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Optional<User> activateUserById(long userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateUser(long id, String username, String password, String firstName, String lastName, String email,
            String phone) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteToken(String token) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAssociatedPasswordTokens(String token) {
        // TODO Auto-generated method stub

    }

    @Override
    public void purgeAllExpiredTokensSince(LocalDateTime expiredCreatedAt) {
        // TODO Auto-generated method stub

    }

}
