package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CommentJpaDao implements CommentDao {

    @PersistenceContext
    private EntityManager em;

    // CREATE

    @Override
    public Comment addComment(User user, Restaurant restaurant, String comment, LocalDate date){
        final Comment userComment = new Comment(comment, date);
        userComment.setUser(user);
        userComment.setRestaurant(restaurant);
        em.persist(userComment);
        return userComment;
    }

    // READ

    @Override
    public Optional<Comment> findById(long id){
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public Optional<Comment> findByUserAndRestaurantId(long userId, long restaurantId){
        Query nativeQuery = em.createNativeQuery(
                "SELECT comment_id FROM comments WHERE user_id = :userId and restaurant_id = :restaurantId ORDER BY date ASC");
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("restaurantId", restaurantId);

        @SuppressWarnings("unchecked")
        Long filteredId = (Long)nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString())).findFirst().orElse(null);
        if (filteredId == null) {
            return Optional.empty();
        }
        TypedQuery<Comment> query = em.createQuery("from Comment where id = :filteredId", Comment.class);
        query.setParameter("filteredId", filteredId);
        return query.getResultList().stream().findFirst();
    }


    @Override
    public List<Comment> findByRestaurant(int page, int amountOnPage, long restaurantId){
        Query nativeQuery = em.createNativeQuery(
                "SELECT comment_id FROM comments"
                        +
                        " WHERE restaurant_id = :restaurantId"
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

        final TypedQuery<Comment> query = em.createQuery("from Comment where id IN :filteredIds",
                Comment.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted(Comparator.comparing(v->filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }

    public int findByRestaurantPageCount(int amountOnPage, long restaurantId){
        Query nativeQuery = em.createNativeQuery(
                "SELECT comment_id FROM comments"
                        +
                        " WHERE restaurant_id = :restaurantId"
        );
        nativeQuery.setParameter("restaurantId", restaurantId);

        int amountOfRestaurants = nativeQuery.getResultList().size();
        int pageAmount = (int) Math.ceil((double) amountOfRestaurants / amountOnPage);

        return pageAmount <= 0 ? 1 : pageAmount;
    }


    // DESTROY

    @Override
    public boolean deleteComment(long commentId){
        Optional<Comment> maybeComment = findById(commentId);
        if (maybeComment.isPresent()) {
            em.remove(maybeComment.get());
            return true;
        }
        return false;
    }
}
