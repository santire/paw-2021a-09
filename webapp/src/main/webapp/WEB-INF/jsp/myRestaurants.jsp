<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<sc:templateLayout>
    <jsp:body>
        <main>
            <section>
                <div class="mx-auto w-75 mt-4">
                    <c:choose>
                        <c:when test="${userHasRestaurants}">
                            <h2 class="display-5"><spring:message code="myRestaurants.title" /></h2>
                            <div class="row row-cols-1 row-cols-md-4 row-cols-lg-5 mt-5">
                                <c:forEach var="restaurant" items="${restaurants}" >
                                    <div class="col mb-4">
                                        <sc:myRestaurantCard
                                                restaurant="${restaurant}"
                                        />
                                    </div>
                                </c:forEach>
                            </div>
                            <c:url value="/restaurants/user/${userId}" var="url"/>
                            <div class="mx-auto">
                                <sc:pagination baseUrl="/restaurants/user/${userId}" pages="${maxPages}"/>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <h2 class="display-5 text-center mt-5"><spring:message code="myRestaurants.noRestaurants" /></h2>
                            <div class="justify-content-center mt-5">
                                <a class="text-decoration-none" href="<c:url value="/register/restaurant"/>">
                                    <input type="submit" class="btn btn-outline-secondary btn-block w-50 mt-3 px-0 mx-auto" value='<spring:message code="navbar.registerRestaurant" />'>
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>
        </main>
    </jsp:body>
</sc:templateLayout>
