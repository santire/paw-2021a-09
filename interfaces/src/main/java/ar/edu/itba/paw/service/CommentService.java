package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    // CREATE
    Comment addComment(long userId, long restaurantId, String comment);

    // READ
    Optional<Comment> findById(long commentId);
    int findByRestaurantCount( long restaurantId);
    List<Comment> findByRestaurant(int page, int amountOnPage, long restaurantId);

    // DESTROY
    boolean deleteComment(long id);
}
