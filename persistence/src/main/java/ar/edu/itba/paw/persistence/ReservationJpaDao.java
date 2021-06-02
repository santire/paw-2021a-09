package ar.edu.itba.paw.persistence;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.model.Image;
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
        final Reservaton reservation = new Reservation(user,restaurant,date,quantity);
        em.persist(reservation);
        return reservation;
    }


    @Override
    public List<Reservation> findConfirmedByRestaurant(int page, int amountOnPage, Restaurant restaurant) {
        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations WHERE restaurant_id LIKE ?1");
        nativeQuery.setParameter(1, "%" + restaurant.getId() + "%");
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);
        @SuppressWarnings("unchecked")
        List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString()))
                .collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Reservation> query = em.createQuery("from Reservation where id IN :filteredIds",
                Restaurant.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();
    }

    @Override
    public int findConfirmedByRestaurantPagesCount(int amountOnPage, Restarurant restaurant {
        Query nativeQuery = em.createNativeQuery("SELECT reservation_id FROM reservations WHERE restaurant_id LIKE ?1");
        nativeQuery.setParameter(1, "%" + restaurant.getId() + "%");

        int amountOfRestaurants = nativeQuery.getResultList().size();
        int pageAmount = (int) Math.ceil((double) amountOfRestaurants / amountOnPage);

        return pageAmount <= 0 ? 1 : pageAmount;
    }

    public Optional<Reservation> modifyReservation(Reservation reservation, LocalDateTime date, long quantity);
   


    // TODO: refactor this to receive Restaurant entity not id
    @Override
    public boolean cancelReservation(Reservation reservation) {
        Optional<Restaurant> maybeRestaurant = reservation.getRestaurant();
        if (maybeRestaurant.isPresent()) {
            em.remove(maybeRestaurant.get());
            return true;
        }
        return false;
    }

    @Override
    public Reservation (User user, Restaurant restaurant, LocalDateTime date, long quantity){
        final Reservaton reservation = new Reservation(user,restaurant,date,quantity);
        em.persist(reservation);
        return reservation;
    }

}
