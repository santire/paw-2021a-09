package ar.edu.itba.paw.webapp.utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class PageUtils {
    private static URI makePageUrl(UriInfo uriInfo, int value) {
        return uriInfo.getAbsolutePathBuilder().queryParam("page", value).build();
    }

    private static int maxPages(int totalCount, int amountOnPage) {
        int pageAmount = (int) Math.ceil((double) totalCount / amountOnPage);
        return pageAmount <= 0 ? 1 : pageAmount;
    }

    public static Response paginatedResponse(Object body, UriInfo uriInfo, int currentPage, int amountOnPage, int totalAmount) {
        int maxPages = maxPages(totalAmount, amountOnPage);

        int nextPage = currentPage + 1;
        if (nextPage > maxPages) {
            nextPage = maxPages;
        }
        if (nextPage < 1) {
            nextPage = 1;
        }
        int prevPage = currentPage - 1;
        if (prevPage < 1) {
            prevPage = 1;
        }
        if (prevPage > maxPages) {
            prevPage = maxPages;
        }


        return Response.ok(body)
                .header("X-Total-Count", totalAmount)
                .link(makePageUrl(uriInfo, 1), "first")
                .link(makePageUrl(uriInfo, maxPages), "last")
                .link(makePageUrl(uriInfo, prevPage), "prev")
                .link(makePageUrl(uriInfo, nextPage), "next")
                .build();

    }
}
