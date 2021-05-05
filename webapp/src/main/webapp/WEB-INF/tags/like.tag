<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="likeCount" required="true" type="java.lang.String"%>
<div class="align-items-center mx-1">
    <c:choose>
        <c:when test="${not empty loggedUser}">
            <c:if test="${userLikesRestaurant}">
                <form action="${pageContext.request.contextPath}/restaurant/${restaurant.getId()}/dislike" method="post">
                    <button type="submit" class="text-decoration-none p-0 m-0 btn btn-link my-auto text-muted"><c:out value="${likeCount}"/><i class="text-danger fa fa-heart pl-1"></i></button>
                </form>
            </c:if>
            <c:if test="${!userLikesRestaurant}">
                <form action="${pageContext.request.contextPath}/restaurant/${restaurant.getId()}/like" method="post">
                    <button type="submit" class=" text-decoration-none p-0 m-0 btn btn-link my-auto text-muted"><c:out value="${likeCount}"/><i class="text-muted fa fa-heart pl-1"></i></button>
                </form>
            </c:if>
        </c:when>
        <c:otherwise>
            <form>
                <button disabled class="text-decoration-none p-0 m-0 btn btn-link my-auto text-muted"><c:out value="${likeCount}"/><i class="text-muted fa fa-heart pl-1"></i></button>
            </form>
        </c:otherwise>
    </c:choose>
</div>

