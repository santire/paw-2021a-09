package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    // CREATE
    public Comment addComment(long userId, long restaurantId, String comment);

    // READ
    public Optional<Comment> findById(long commentId);
    public Optional<Comment> findByUserAndRestaurantId(long userId, long restaurantId);
    public int findByRestaurantPageCount(int amountOnPage, long restaurantId);
    public List<Comment> findByRestaurant(int page, int amountOnPage, long restaurantId);

    // DESTROY
    public boolean deleteComment(long id);
}
