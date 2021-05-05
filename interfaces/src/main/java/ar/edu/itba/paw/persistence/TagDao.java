package ar.edu.itba.paw.persistence;

import java.util.List;

import ar.edu.itba.paw.model.Tags;

public interface TagDao {

    public List<Tags> getTagsByRestaurantId(long restaurantId);
}
