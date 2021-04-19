<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
    <c:choose>
        <c:when test="${loggedUser}">
            <c:if test="${userLikesRestaurant}">
                <form action="${pageContext.request.contextPath}/restaurant/dislike/${restaurant.getId()}" method="post">
                    <button type="submit" class="btn btn-link"><i class="fa fa-thumbs-down"></i></button>
                </form>
            </c:if>
            <c:if test="${!userLikesRestaurant}">
                <form action="${pageContext.request.contextPath}/restaurant/like/${restaurant.getId()}" method="post">
                    <button type="submit" class="btn btn-link"><i class="fa fa-thumbs-up"></i></button>
                </form>
            </c:if>
        </c:when>
    </c:choose>
</div>

