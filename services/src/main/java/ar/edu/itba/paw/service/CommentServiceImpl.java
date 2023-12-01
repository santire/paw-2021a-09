package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Comment;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;

    // CREATE

    @Override
    @Transactional
    public Comment addComment(long userId, long restaurantId, String comment) {
        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        LocalDate currentDate = LocalDate.now();

        return commentDao.addComment(user, restaurant, comment, currentDate);
    }

    @Override
    @Transactional
    public List<Comment> findComments(int page, int amountOnPage, Long userId, Long restaurantId) {
        return commentDao.findFilteredComments(page, amountOnPage, userId, restaurantId, true);
    }

    @Override
    @Transactional
    public int findCommentsCount(Long userId, Long restaurantId) {
        return commentDao.findFilteredCommentsCount(userId, restaurantId);
    }

    @Override
    @Transactional
    public Optional<Comment> findById(long commentId) {
        return commentDao.findById(commentId);
    }

    @Override
    @Transactional
    public void deleteComment(long id) {
        commentDao.deleteComment(id);
    }
}
