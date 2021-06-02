package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.Sorting;
import ar.edu.itba.paw.model.Tags;
import ar.edu.itba.paw.model.User;

@Repository
public class ReservationJpaDao implements ReservationDao {

    @PersistenceContext
    private EntityManager em;

    // CREATE

    @Override
    public Reservation addReservation(User user, Restaurant restaurant, LocalDateTime date, long quantity){
        final Reservation reservation = new Reservation(date,quantity);
        reservation.setUser(user);
        reservation.setRestaurant(restaurant);
        em.persist(reservation);
        return reservation;
    }


    @Override
    public List<Reservation> findConfirmedByRestaurant(int page, int amountOnPage, long restaurantId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery(
                "SELECT reservation_id FROM reservations"
                +
                " WHERE restaurant_id = :restaurantId AND confirmed = true AND date >= :currTime"
                +
                " ORDER BY date ASC"
                );

        nativeQuery.setParameter("restaurantId", restaurantId);
        nativeQuery.setParameter("currTime", currentTimestamp);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);
        @SuppressWarnings("unchecked")
        List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString()))
                .collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Reservation> query = em.createQuery("from Reservation where id IN :filteredIds",
                Reservation.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted(Comparator.comparing(v->filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }

    @Override
    public int findConfirmedByRestaurantPageCount(int amountOnPage, long restaurantId, LocalDateTime currentTime){
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery(
                "SELECT reservation_id FROM reservations"
                +
                " WHERE restaurant_id = :restaurantId AND confirmed = true AND date >= :currTime"
                );
        nativeQuery.setParameter("restaurantId", restaurantId);
        nativeQuery.setParameter("currTime", currentTimestamp);

        int amountOfRestaurants = nativeQuery.getResultList().size();
        int pageAmount = (int) Math.ceil((double) amountOfRestaurants / amountOnPage);

        return pageAmount <= 0 ? 1 : pageAmount;
    }


    // TODO: refactor this to receive Restaurant entity not id
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
  @Deprecated
	public List<Reservation> findByUser(long userId) {
		return null;
	}


	@Override
	public List<Reservation> findByUser(int page, int amountOnPage, long userId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery(
                "SELECT reservation_id FROM reservations"
                +
                " WHERE user_id = :userId and date >= :currTimestamp"
                +
                " ORDER BY date ASC"
                );

        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("currTimestamp", currentTimestamp);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);
        @SuppressWarnings("unchecked")
        List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString()))
                .collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Reservation> query = em.createQuery("from Reservation where id IN :filteredIds",
                Reservation.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted(Comparator.comparing(v->filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
	}


	@Override
	public int findByUserPageCount(int amountOnPage, long userId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery(
                "SELECT reservation_id FROM reservations"
                +
                " WHERE user_id = :userId AND date >= :currentTimestamp"
                );
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("currentTimestamp", currentTimestamp);

        int amountOfRestaurants = nativeQuery.getResultList().size();
        int pageAmount = (int) Math.ceil((double) amountOfRestaurants / amountOnPage);

        return pageAmount <= 0 ? 1 : pageAmount;
	}

    @Override
    public List<Reservation> findByUserHistory(int page, int amountOnPage, long userId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery(
                "SELECT reservation_id FROM reservations"
                        +
                        " WHERE user_id = :userId and date < :currTimestamp"
                        +
                        " ORDER BY date ASC"
        );

        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("currTimestamp", currentTimestamp);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);
        @SuppressWarnings("unchecked")
        List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString()))
                .collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Reservation> query = em.createQuery("from Reservation where id IN :filteredIds",
                Reservation.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted(Comparator.comparing(v->filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }


    @Override
    public int findByUserHistoryPageCount(int amountOnPage, long userId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery(
                "SELECT reservation_id FROM reservations"
                        +
                        " WHERE user_id = :userId AND date < :currentTimestamp"
        );
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("currentTimestamp", currentTimestamp);

        int amountOfRestaurants = nativeQuery.getResultList().size();
        int pageAmount = (int) Math.ceil((double) amountOfRestaurants / amountOnPage);

        return pageAmount <= 0 ? 1 : pageAmount;
    }


	@Override
  @Deprecated
	public List<Reservation> findByRestaurant(long restaurantId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId) {
        Query nativeQuery = em.createNativeQuery(
                "SELECT reservation_id FROM reservations"
                +
                " WHERE restaurant_id = :restaurantId"
                +
                " ORDER BY date ASC"
                );

        nativeQuery.setParameter("restaurantId", restaurantId);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);
        @SuppressWarnings("unchecked")
        List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString()))
                .collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Reservation> query = em.createQuery("from Reservation where id IN :filteredIds",
                Reservation.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted(Comparator.comparing(v->filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
	}


	@Override
	public List<Reservation> findPendingByRestaurant(int page, int amountOnPage, long restaurantId,
			LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery(
                "SELECT reservation_id FROM reservations"
                +
                " WHERE restaurant_id = :restaurantId AND confirmed = false AND date >= :currTime"
                +
                " ORDER BY date ASC"
                );

        nativeQuery.setParameter("restaurantId", restaurantId);
        nativeQuery.setParameter("currTime", currentTimestamp);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);
        @SuppressWarnings("unchecked")
        List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString()))
                .collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Reservation> query = em.createQuery("from Reservation where id IN :filteredIds",
                Reservation.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted(Comparator.comparing(v->filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
	}


	@Override
	public int findByRestaurantPageCount(int amountOnPage, long restaurantId) {
        Query nativeQuery = em.createNativeQuery(
                "SELECT reservation_id FROM reservations"
                +
                " WHERE restaurant_id = :restaurantId"
                );
        nativeQuery.setParameter("restaurantId", restaurantId);

        int amountOfRestaurants = nativeQuery.getResultList().size();
        int pageAmount = (int) Math.ceil((double) amountOfRestaurants / amountOnPage);

        return pageAmount <= 0 ? 1 : pageAmount;
	}


	@Override
	public int findPendingByRestaurantPageCount(int amountOnPage, long restaurantId, LocalDateTime currentTime) {
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        Query nativeQuery = em.createNativeQuery(
                "SELECT reservation_id FROM reservations"
                +
                " WHERE restaurant_id = :restaurantId AND confirmed = false AND date >= :currTime"
                );
        nativeQuery.setParameter("restaurantId", restaurantId);
        nativeQuery.setParameter("currTime", currentTimestamp);

        int amountOfRestaurants = nativeQuery.getResultList().size();
        int pageAmount = (int) Math.ceil((double) amountOfRestaurants / amountOnPage);

        return pageAmount <= 0 ? 1 : pageAmount;
	}


	@Override
	public Optional<Reservation> findById(long id) {
		return Optional.ofNullable(em.find(Reservation.class, id));
	}


	@Override
  @Deprecated
	public Optional<Reservation> modifyReservation(long reservationId, LocalDateTime date, long quantity) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
  @Deprecated
  //Done by the service
	public boolean confirmReservation(long reservationId) {
		// TODO Auto-generated method stub
		return false;
	}

}
