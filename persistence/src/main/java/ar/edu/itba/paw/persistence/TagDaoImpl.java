package ar.edu.itba.paw.persistence;

import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.model.Tags;

@Repository
public class TagDaoImpl implements TagDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDaoImpl(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<Tags> getTagsByRestaurantId(long restaurantId) {
        List<Integer> tagIds = jdbcTemplate.query(
                    "SELECT tag_id FROM restaurant_tags"
                    +
                    " WHERE restaurant_id = ?"
                    , (r,n)->r.getInt("tag_id"), restaurantId);
       List<Tags> tags = tagIds.stream().map(i -> Tags.valueOf(i)).collect(Collectors.toList());
       return tags;
    }

}
