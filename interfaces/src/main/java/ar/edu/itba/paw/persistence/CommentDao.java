package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommentDao {
    // CREATE
    Comment addComment(User user, Restaurant restaurant, String comment, LocalDate date);

    // READ
    Optional<Comment> findById(long id);
    Optional<Comment> findByUserAndRestaurantId(long userId, long restaurantId);
    List<Comment> findByRestaurant(int page, int amountOnPage, long restaurantId);
    int findByRestaurantCount(long restaurantId);

    // DESTROY
    boolean deleteComment(long id);
}
