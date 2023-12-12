package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.utils.JpaUtils;
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


    @Override
    public Comment addComment(User user, Restaurant restaurant, String comment, LocalDate date) {
        final Comment userComment = new Comment(comment, date);
        userComment.setUser(user);
        userComment.setRestaurant(restaurant);
        em.persist(userComment);
        return userComment;
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public List<Comment> findFilteredComments(int page, int amountOnPage, Long userId, Long restaurantId, boolean desc) {
        Query nativeQuery = findCommentsQuery(userId, restaurantId, desc);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);

        return collectComments(page, amountOnPage, nativeQuery);
    }

    @Override
    public int findFilteredCommentsCount(Long userId, Long restaurantId) {
        return findCommentsQuery(userId, restaurantId, true).getResultList().size();
    }

    @Override
    public void deleteComment(long commentId) {
        Optional<Comment> maybeComment = findById(commentId);
        maybeComment.ifPresent(comment -> em.remove(comment));
    }

    private Query findCommentsQuery(Long userId, Long restaurantId, boolean desc) {
        String userPart = (userId != null) ? " AND user_id = :userId" : "";
        String restaurantPart = (restaurantId != null) ? " AND restaurant_id = :restaurantId" : "";
        String orderPart = desc ? "DESC" : "ASC";

        // WHERE true is necessary so that query still works regardless of user_id/restaurant_id
        // as those parts start with " AND..."
        Query nativeQuery = em.createNativeQuery("SELECT comment_id FROM comments WHERE true" + userPart + restaurantPart + " ORDER BY date " + orderPart + ", comment_id " + orderPart);

        if (userId != null) {
            nativeQuery.setParameter("userId", userId);
        }
        if (restaurantId != null) {
            nativeQuery.setParameter("restaurantId", restaurantId);
        }
        return nativeQuery;
    }

    private List<Comment> collectComments(int page, int amountOnPage, Query query) {
        List<Long> filteredIds = JpaUtils.getFilteredIds(page, amountOnPage, query);
        final TypedQuery<Comment> typedQuery = em.createQuery("from Comment where id IN :filteredIds", Comment.class);
        typedQuery.setParameter("filteredIds", filteredIds);

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        return typedQuery.getResultList().stream().sorted(Comparator.comparing(v -> filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }
}
