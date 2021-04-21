<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<sc:templateLayout>
    <jsp:body>
        <main>
            <section>
                <div class="container mt-4">
                    <c:choose>
                        <c:when test="${userHasRestaurants}">
                            <h2 class="display-5"><spring:message code="myRestaurants.title" /></h2>
                            <div class="row row-cols-1 row-cols-md-4 row-cols-lg-5">
                                <c:forEach var="restaurant" items="${restaurants}" >
                                    <div class="col mb-4">
                                        <sc:restaurantCard
                                                restaurant="${restaurant}"
                                        />
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <h2 class="display-5"><spring:message code="myRestaurants.noRestaurants" /></h2>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>
        </main>
    </jsp:body>
</sc:templateLayout>
