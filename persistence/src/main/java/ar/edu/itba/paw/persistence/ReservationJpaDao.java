package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.ReservationStatus;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.utils.JpaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReservationJpaDao implements ReservationDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationJpaDao.class);
    @PersistenceContext
    private EntityManager em;

    // CREATE

    @Override
    public Reservation addReservation(User user, Restaurant restaurant, LocalDateTime date, long quantity) {
        final Reservation reservation = new Reservation(date, quantity);
        reservation.setUser(user);
        reservation.setRestaurant(restaurant);
        em.persist(reservation);
        return reservation;
    }

    @Override
    public void cancelReservation(long reservationId) {
        Optional<Reservation> maybeReservation = findById(reservationId);
        maybeReservation.ifPresent(reservation -> em.remove(reservation));
    }

    @Override
    public Optional<Reservation> findById(long id) {
        return Optional.ofNullable(em.find(Reservation.class, id));
    }

    @Override
    public List<Reservation> findFilteredReservations(int page, int amountOnPage, Long userId, Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate, ReservationStatus status, boolean desc) {
        Query nativeQuery = findFilteredReservationsQuery(userId, restaurantId, fromDate, toDate, status, desc);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);

        return collectReservations(page, amountOnPage, nativeQuery);
    }

    @Override
    public int findFilteredReservationsCount(Long userId, Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate, ReservationStatus status) {
        return findFilteredReservationsQuery(userId, restaurantId, fromDate, toDate, status, false).getResultList().size();
    }

    private Query findFilteredReservationsQuery(Long userId, Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate, ReservationStatus status, boolean desc) {
        Timestamp fromTimestamp = (fromDate != null) ? Timestamp.valueOf(fromDate) : JpaUtils.MIN_TIMESTAMP;
        Timestamp toTimestamp = (toDate != null) ? Timestamp.valueOf(toDate) : JpaUtils.MAX_TIMESTAMP;

        String userPart = (userId != null) ? " AND user_id = :userId" : "";
        String restaurantPart = (restaurantId != null) ? " AND restaurant_id = :restaurantId" : "";
        String statusPart = (status != null) ? " AND status = :status" : "";
        String orderPart = desc ? "DESC" : "ASC";


        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations" + " WHERE date BETWEEN :fromTimestamp AND :toTimestamp" + statusPart + userPart + restaurantPart + " ORDER BY date " + orderPart);

        if (userId != null) {
            nativeQuery.setParameter("userId", userId);
        }
        if (restaurantId != null) {
            nativeQuery.setParameter("restaurantId", restaurantId);
        }
        if (status != null) {
            nativeQuery.setParameter("status", status.toString());
        }

        nativeQuery.setParameter("fromTimestamp", fromTimestamp);
        nativeQuery.setParameter("toTimestamp", toTimestamp);
        return nativeQuery;

    }

    private List<Reservation> collectReservations(int page, int amountOnPage, Query query) {

        List<Long> filteredIds = JpaUtils.getFilteredIds(page, amountOnPage, query);
        final TypedQuery<Reservation> typedQuery = em.createQuery("from Reservation where id IN :filteredIds", Reservation.class);
        typedQuery.setParameter("filteredIds", filteredIds);

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        return typedQuery.getResultList().stream().sorted(Comparator.comparing(v -> filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }
}
