package ar.edu.itba.paw.webapp.forms;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentForm {


    @Size(min = 1, max = 144)
    @NotEmpty
    @NotNull
    private String message;
    @NotNull
    private Long userId;
    @NotNull
    private Long restaurantId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
