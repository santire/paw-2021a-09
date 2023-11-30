package ar.edu.itba.paw.webapp.forms;

import javax.validation.constraints.NotNull;

public class LikeForm {
    @NotNull
    private Long restaurantId;

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
