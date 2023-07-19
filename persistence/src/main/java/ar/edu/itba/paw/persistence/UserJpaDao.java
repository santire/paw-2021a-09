package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.PasswordToken;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.model.exceptions.UsernameInUseException;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Repository
public class UserJpaDao implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserJpaDao.class);

    @PersistenceContext
    EntityManager em;

    // CREATE

    @Override
    public User register(String username, String password, String firstName, String lastName, String email,
                         String phone) {
        final User user = new User(username, password, firstName, lastName, email, phone);
        try {
            em.persist(user);
            em.flush();
            return user;
        } catch (PersistenceException e) {
            Throwable ex = e.getCause().getCause();
            if (ex instanceof PSQLException) {
                PSQLException psqlEx = (PSQLException) ex;
                if ("23505".equals(psqlEx.getSQLState())) {
                    ServerErrorMessage postgresError = psqlEx.getServerErrorMessage();
                    if (postgresError != null) {
                        String constraint = postgresError.getConstraint();
                        if (constraint != null) {
                            switch (constraint.toLowerCase()) {
                                case "users_email_key":
                                    throw new EmailInUseException("Email " + email + " already in use", email);
                                case "user_name_unq":
                                    throw new UsernameInUseException("Username " + username + " already in use", username);
                            }
                        }

                    }
                }
            }
            if (ex instanceof ConstraintViolationException) {
                throw new UsernameInUseException("Username " + username + " already in use", username);
            }

            // If it gets here without throwing something else catch and release :)
            throw e;
        }
    }


    @Override
    public void assignTokenToUser(String token, LocalDateTime createdAt, long userId) {
        User user = findById(userId).orElseThrow(UserNotFoundException::new);
        VerificationToken verificationToken = new VerificationToken(token, createdAt, user);
        try {
            em.persist(verificationToken);
        } catch (Exception e) {
            throw new TokenCreationException();
        }

    }

    @Override
    public void assignPasswordTokenToUser(String token, LocalDateTime createdAt, long userId) {
        User user = findById(userId).orElseThrow(UserNotFoundException::new);
        PasswordToken passwordToken = new PasswordToken(token, createdAt, user);
        try {
            em.persist(passwordToken);
        } catch (Exception e) {
            throw new TokenCreationException();
        }
    }

    // READ

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(em.find(User.class, username));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        final TypedQuery<User> query = em.createQuery("from User u where u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    // VerificationToken and PasswordToken should be two different entities

    @Override
    public Optional<VerificationToken> getToken(String token) {
        final TypedQuery<VerificationToken> query = em.createQuery("from VerificationToken v where v.token = :token", VerificationToken.class);
        query.setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<PasswordToken> getPasswordToken(String token) {
        final TypedQuery<PasswordToken> query = em.createQuery("from PasswordToken v where v.token = :token", PasswordToken.class);
        query.setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }


    @Override
    public void deleteToken(String token) {
        Optional<VerificationToken> maybeToken = getToken(token);
        if (maybeToken.isPresent()) {
            em.remove(maybeToken.get());
        }
    }

    @Override
    public void deleteAssociatedPasswordTokens(User user) {
        final Query nativeQuery = em.createNativeQuery("DELETE FROM password_tokens WHERE user_id = ?1");
        nativeQuery.setParameter(1, user.getId());
        nativeQuery.executeUpdate();
    }

    @Override
    public void purgeAllExpiredTokensSince(LocalDateTime expiredCreatedAt) {
        Timestamp expiredCreatedAtTimestamp = Timestamp.valueOf(expiredCreatedAt);

        Query query = em.createQuery("delete VerificationToken v where v.createdAt < :exDate");
        query.setParameter("exDate", expiredCreatedAtTimestamp);
        Query query2 = em.createQuery("delete PasswordToken v where v.createdAt < :exDate");
        query2.setParameter("exDate", expiredCreatedAtTimestamp);

        query.executeUpdate();
        query2.executeUpdate();
    }

}
