package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommentDao {
    // CREATE
    public Comment addComment(User user, Restaurant restaurant, String comment, LocalDate date);

    // READ
    public Optional<Comment> findById(long id);
    public Optional<Comment> findByUserAndRestaurantId(long userId, long restaurantId);
    public List<Comment> findByRestaurant(int page, int amountOnPage, long restaurantId);
    public int findByRestaurantPageCount(int amountOnPage, long restaurantId);

    // DESTROY
    public boolean deleteComment(long id);
}
