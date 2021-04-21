<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
    <c:choose>
        <c:when test="${not empty loggedUser}">
            <c:if test="${userLikesRestaurant}">
                <form action="${pageContext.request.contextPath}/restaurant/${restaurant.getId()}/dislike" method="post">
                    <button type="submit" class="btn btn-link"><i class="text-danger fa fa-heart fa-2x"></i></button>
                </form>
            </c:if>
            <c:if test="${!userLikesRestaurant}">
                <form action="${pageContext.request.contextPath}/restaurant/${restaurant.getId()}/like" method="post">
                    <button type="submit" class="btn btn-link"><i class="text-muted fa fa-heart fa-2x"></i></button>
                </form>
            </c:if>
        </c:when>
    </c:choose>
</div>

