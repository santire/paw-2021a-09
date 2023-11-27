package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.utils.JpaUtils;
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
    public List<Reservation> findConfirmedByRestaurant(int page, int amountOnPage, long restaurantId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations" + " WHERE restaurant_id = :restaurantId AND confirmed = true AND date >= :currTime" + " ORDER BY date ASC");

        nativeQuery.setParameter("restaurantId", restaurantId);
        nativeQuery.setParameter("currTime", currentTimestamp);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);
        @SuppressWarnings("unchecked") List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString())).collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Reservation> query = em.createQuery("from Reservation where id IN :filteredIds", Reservation.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted(Comparator.comparing(v -> filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }

    @Override
    public int findConfirmedByRestaurantCount(long restaurantId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations" + " WHERE restaurant_id = :restaurantId AND confirmed = true AND date >= :currTime");
        nativeQuery.setParameter("restaurantId", restaurantId);
        nativeQuery.setParameter("currTime", currentTimestamp);

        return nativeQuery.getResultList().size();
    }


    @Override
    public boolean cancelReservation(long reservationId) {
        Optional<Reservation> maybeReservation = findById(reservationId);
        if (maybeReservation.isPresent()) {
            em.remove(maybeReservation.get());
            return true;
        }
        return false;
    }


    @Override
    public List<Reservation> findByUser(int page, int amountOnPage, long userId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations" + " WHERE user_id = :userId and date >= :currTimestamp" + " ORDER BY date ASC");

        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("currTimestamp", currentTimestamp);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);
        @SuppressWarnings("unchecked") List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString())).collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Reservation> query = em.createQuery("from Reservation where id IN :filteredIds", Reservation.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted(Comparator.comparing(v -> filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }


    @Override
    public int findByUserCount(long userId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations" + " WHERE user_id = :userId AND date >= :currentTimestamp");
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("currentTimestamp", currentTimestamp);

        return nativeQuery.getResultList().size();

    }

    @Override
    public List<Reservation> findByUserHistory(int page, int amountOnPage, long userId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations" + " WHERE user_id = :userId and date < :currTimestamp" + " ORDER BY date ASC");

        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("currTimestamp", currentTimestamp);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);
        @SuppressWarnings("unchecked") List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString())).collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Reservation> query = em.createQuery("from Reservation where id IN :filteredIds", Reservation.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted(Comparator.comparing(v -> filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }


    @Override
    public int findByUserHistoryCount(long userId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations" + " WHERE user_id = :userId AND date < :currentTimestamp");
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("currentTimestamp", currentTimestamp);

        return nativeQuery.getResultList().size();
    }


    @Override
    public List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId) {
        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations" + " WHERE restaurant_id = :restaurantId" + " ORDER BY date ASC");

        nativeQuery.setParameter("restaurantId", restaurantId);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);
        @SuppressWarnings("unchecked") List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString())).collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Reservation> query = em.createQuery("from Reservation where id IN :filteredIds", Reservation.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted(Comparator.comparing(v -> filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }


    @Override
    public List<Reservation> findPendingByRestaurant(int page, int amountOnPage, long restaurantId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations" + " WHERE restaurant_id = :restaurantId AND confirmed = false AND date >= :currTime" + " ORDER BY date ASC");

        nativeQuery.setParameter("restaurantId", restaurantId);
        nativeQuery.setParameter("currTime", currentTimestamp);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);
        @SuppressWarnings("unchecked") List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString())).collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Reservation> query = em.createQuery("from Reservation where id IN :filteredIds", Reservation.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted(Comparator.comparing(v -> filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }


    @Override
    public int findByRestaurantCount(long restaurantId) {
        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations" + " WHERE restaurant_id = :restaurantId");
        nativeQuery.setParameter("restaurantId", restaurantId);

        return nativeQuery.getResultList().size();
    }


    @Override
    public int findPendingByRestaurantCount(long restaurantId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations" + " WHERE restaurant_id = :restaurantId AND confirmed = false AND date >= :currTime");
        nativeQuery.setParameter("restaurantId", restaurantId);
        nativeQuery.setParameter("currTime", currentTimestamp);

        return nativeQuery.getResultList().size();

    }

    @Override
    public List<Reservation> findHistoryByRestaurant(int page, int amountOnPage, long restaurantId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations" + " WHERE restaurant_id = :restaurantId AND date < :currTime" + " ORDER BY date ASC");

        nativeQuery.setParameter("restaurantId", restaurantId);
        nativeQuery.setParameter("currTime", currentTimestamp);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);
        @SuppressWarnings("unchecked") List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString())).collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Reservation> query = em.createQuery("from Reservation where id IN :filteredIds", Reservation.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted(Comparator.comparing(v -> filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }

    @Override
    public int findHistoryByRestaurantCount(long restaurantId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations" + " WHERE restaurant_id = :restaurantId AND confirmed = false AND date < :currTime");
        nativeQuery.setParameter("restaurantId", restaurantId);
        nativeQuery.setParameter("currTime", currentTimestamp);

        return nativeQuery.getResultList().size();
    }


    @Override
    public Optional<Reservation> findById(long id) {
        return Optional.ofNullable(em.find(Reservation.class, id));
    }

    @Override
    public List<Reservation> findFilteredReservations(int page, int amountOnPage, Long userId, Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate, Boolean confirmed) {
        Query nativeQuery = findFilteredReservationsQuery(userId, restaurantId, fromDate, toDate, confirmed);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);

        return collectReservations(page, amountOnPage, nativeQuery);
    }

    @Override
    public int findFilteredReservationsCount(Long userId, Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate, Boolean confirmed) {
        return findFilteredReservationsQuery(userId, restaurantId, fromDate, toDate, confirmed).getResultList().size();
    }

    private Query findFilteredReservationsQuery(Long userId, Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate, Boolean confirmed) {
        Timestamp fromTimestamp = (fromDate != null) ? Timestamp.valueOf(fromDate) : JpaUtils.MIN_TIMESTAMP;
        Timestamp toTimestamp = (toDate != null) ? Timestamp.valueOf(toDate) : JpaUtils.MAX_TIMESTAMP;

        String userPart = (userId != null) ? " AND user_id = :userId" : "";
        String restaurantPart = (restaurantId != null) ? " AND restaurant_id = :restaurantId" : "";
        String confirmedPart = (confirmed != null) ? " AND confirmed = :confirmed": "";


        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations" + " WHERE date BETWEEN :fromTimestamp AND :toTimestamp" + confirmedPart + userPart + restaurantPart + " ORDER BY date ASC");

        if (userId != null) {
            nativeQuery.setParameter("userId", userId);
        }
        if (restaurantId != null) {
            nativeQuery.setParameter("restaurantId", restaurantId);
        }
        if (confirmed != null) {
            nativeQuery.setParameter("confirmed", confirmed);
        }

        nativeQuery.setParameter("fromTimestamp", fromTimestamp);
        nativeQuery.setParameter("toTimestamp", toTimestamp);
        return nativeQuery;

    }

    private List<Reservation> collectReservations(int page, int amountOnPage, Query query) {

        List<Long> filteredIds = JpaUtils.getFilteredIds(page, amountOnPage, query);
        final TypedQuery<Reservation> typedQuery = em.createQuery("from Reservation where id IN :filteredIds", Reservation.class);
        typedQuery.setParameter("filteredIds", filteredIds);

        return typedQuery.getResultList().stream().sorted(Comparator.comparing(v -> filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }
}
