package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comment addComment(long userId, long restaurantId, String comment);
    Optional<Comment> findById(long commentId);
    List<Comment> findComments(int page, int amountOnPage, Long userId, Long restaurantId);
    int findCommentsCount(Long userId, Long restaurantId);
    void deleteComment(long id);
}
