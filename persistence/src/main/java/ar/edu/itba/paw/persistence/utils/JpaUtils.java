package ar.edu.itba.paw.persistence.utils;

import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JpaUtils {
    public static final Timestamp MIN_TIMESTAMP = Timestamp.valueOf("1970-01-01 00:00:00");
    public static final Timestamp MAX_TIMESTAMP = Timestamp.valueOf("9999-12-31 00:00:00");

    private JpaUtils() {
    }

    public static List<Long> getFilteredIds(int page, int amountOnPage, Query query) {
        query.setFirstResult((page - 1) * amountOnPage);
        query.setMaxResults(amountOnPage);
        System.out.println(String.format("page: %d, amount: %d", page, amountOnPage));
        @SuppressWarnings("unchecked")
        List<Long> filteredIds = (List<Long>) query.getResultList().stream().map(e -> Long.valueOf(e.toString()))
                .collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }
        return filteredIds;
    }
}
