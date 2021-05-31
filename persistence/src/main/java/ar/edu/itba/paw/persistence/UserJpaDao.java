package ar.edu.itba.paw.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;

@Repository
public class UserJpaDao implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserJpaDao.class);

    @PersistenceContext
    EntityManager em;

    // CREATE

    @Override
    public User register(String username, String password, String firstName, String lastName, String email,
            String phone) throws EmailInUseException {
        final User user = new User(username, password, firstName, lastName, email, phone);
        try {
            em.persist(user);
        } catch (DuplicateKeyException e) {
            LOGGER.warn("Can't register, email: {} already in use", email);
            throw new EmailInUseException("Email "+ email +" already in use", email);
        }
        return user;
    }


    @Override
    // TODO: move to user reference
    public void assignTokenToUser(String token, LocalDateTime createdAt, long userId) throws TokenCreationException {
        User user = findById(userId).orElseThrow(UserNotFoundException::new);
        VerificationToken verificationToken = new VerificationToken(token, createdAt, user);
        em.persist(verificationToken);
    }

    @Override
    @Deprecated
    // Can be done from service?
    public void assignPasswordTokenToUser(String token, LocalDateTime createdAt, long userId)
            throws TokenCreationException {
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

    // VerificationToken and PasswordToken should be two different entities

    @Override
    public Optional<VerificationToken> getToken(String token) {
        final TypedQuery<VerificationToken> query = em.createQuery("from VerificationToken v where v.token = :token", VerificationToken.class);
        query.setParameter("token", token);
        final List<VerificationToken> list = query.getResultList();
        return Optional.ofNullable(list.isEmpty() ? null : list.get(0));
    }

    @Override
    public Optional<VerificationToken> getPasswordToken(String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Deprecated
    // Could be a service method
    // But maybe it's better as a single query
    public boolean isTheRestaurantOwner(long userId, long restaurantId) {
        return false;
    }

    @Override
    @Deprecated
    // Could be a service method
    public boolean isRestaurantOwner(long userId) {
        return false;
    }

    @Override
    @Deprecated
    public Optional<User> activateUserById(long userId) {
        User user = findById(userId).orElseThrow(UserNotFoundException::new);
        user.setActive(true);
        // em.persist(user); is this necessary?
        return Optional.of(user);
    }

    @Override
    @Deprecated
    // Gets done automatically when edited
    public void updateUser(long id, String username, String password, String firstName, String lastName, String email,
            String phone) {
        // TODO Auto-generated method stub

    }

    @Override
    @Deprecated
    public void deleteToken(String token) {
        Optional<VerificationToken> maybeToken = getToken(token);
        if (maybeToken.isPresent()) {
            em.remove(maybeToken.get());
        }
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
