<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div>
    <c:choose>
        <c:when test="${not empty loggedUser}">
            <c:if test="${rated}">
                User rating to this restaurant is: ${userRatingToRestaurant}
                <c:set var="path" value="${pageContext.request.contextPath}/restaurant/rate/update/${restaurant.getId()}"/>
                <form action="${path}" method="post">
                    <label for="ratedRating" class="control-label">Rate This</label>
                    <input id="ratedRating" name="rating" class="rating-loading" value="${restaurant.getRating()}" data-min="0" data-max="5" data-step="1">
                </form>
            </c:if>
            <c:if test="${!rated}">
                <c:set var="path" value="${pageContext.request.contextPath}/restaurant/rate/set/${restaurant.getId()}"/>
                <form action="${path}" method="post">
                    <label for="unratedRating" class="control-label">Rate This</label>
                    <input id="unratedRating" name="rating" class="rating rating-loading" data-min="0" data-max="5" data-step="1">
                </form>
            </c:if>
        </c:when>

        <c:when test="${!loggedUser}">
            <label for="displayOnlyRating" class="control-label"></label>
            <input id="displayOnlyRating" name="displayOnlyRating" value="${restaurant.getRating()}" class="rating-loading">
            <script>
                $(document).ready(function(){
                    $('displayOnlyRating').rating({displayOnly: true, step: 1});
                });
            </script>
        </c:when>
    </c:choose>
</div>
