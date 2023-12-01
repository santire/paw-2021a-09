package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommentDao {
    Comment addComment(User user, Restaurant restaurant, String comment, LocalDate date);
    Optional<Comment> findById(long id);
    List<Comment> findFilteredComments(int page, int amountOnPage, Long userId, Long restaurantId, boolean desc);
    int findFilteredCommentsCount(Long userId, Long restaurantId);
    void deleteComment(long id);
}
