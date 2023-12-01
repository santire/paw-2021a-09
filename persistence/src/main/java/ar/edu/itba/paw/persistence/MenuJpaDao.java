package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.persistence.utils.JpaUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MenuJpaDao implements MenuDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<MenuItem> getMenuItemById(Long menuItemId) {
        TypedQuery<MenuItem> query = em.createQuery("from MenuItem r WHERE  r.id = :menuItemId", MenuItem.class);

        query.setParameter("menuItemId", menuItemId);

        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<MenuItem> getMenuByRestaurantId(int page, int amountOnPage, Long restaurantId) {
        Query nativeQuery = findMenuQuery(restaurantId);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);

        return collectMenu(page, amountOnPage, nativeQuery);
    }

    @Override
    public int getMenuByRestaurantIdCount(Long restaurantId) {
        return findMenuQuery(restaurantId).getResultList().size();
    }

    @Override
    public MenuItem addItemToRestaurant(Restaurant restaurant, MenuItem item) {
        MenuItem newItem = new MenuItem(item.getName(), item.getDescription(), item.getPrice());
        newItem.setRestaurant(restaurant);
        restaurant.addMenuItem(newItem);

        em.persist(newItem);
        return newItem;
    }

    @Override
    public void deleteItemById(long menuId) {
        Query query = em.createQuery("delete MenuItem where id = :id");
        query.setParameter("id", menuId);
        query.executeUpdate();
    }

    private Query findMenuQuery(Long restaurantId) {
        Query nativeQuery = em.createNativeQuery("SELECT menu_item_id FROM menu_items WHERE restaurant_id = :restaurantId ORDER BY menu_item_id DESC");
        nativeQuery.setParameter("restaurantId", restaurantId);
        return nativeQuery;
    }

    private List<MenuItem> collectMenu(int page, int amountOnPage, Query query) {
        List<Long> filteredIds = JpaUtils.getFilteredIds(page, amountOnPage, query);
        final TypedQuery<MenuItem> typedQuery = em.createQuery("from MenuItem where id IN :filteredIds", MenuItem.class);
        typedQuery.setParameter("filteredIds", filteredIds);

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }
        return typedQuery.getResultList().stream().sorted(Comparator.comparing(v -> filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }


}
