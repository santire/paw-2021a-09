<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<sc:templateLayout>
    <jsp:body>
        <main>
            <section>
                <div class="container mt-4">
                    <h2 class="display-5 mt-5"><spring:message code="restaurant.manage.title"/> ${restaurant.getName()}</h2>
                    <ul class="nav nav-pills nav-fill navtop mt-5">
                        <li class="nav-item">
                            <a class="nav-link active"  href="#reservations" data-toggle="pill" role="tab"><spring:message code="restaurant.manage.confirmedTab"/></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#pending" data-toggle="pill" role="tab"><spring:message code="restaurant.manage.pendingTab"/></a>
                        </li>
                    </ul>
                    <div class="tab-content mt-5">
                        <div class="tab-pane fade show active" role="tabpanel" id="reservations">
                            <c:choose>
                                <c:when test="${restaurantHasReservations}">
                                    <h2 class="display-5 text-center mt-5"><spring:message code="restaurant.manage.reservationsTitle" /></h2>
                                    <c:forEach var="reservation" items="${reservations}">
                                        <div class="row mt-5">
                                            <sc:reservationCard
                                                    reservation="${reservation}"
                                                    isOwner="${isOwner}"
                                            />
                                        </div>
                                    </c:forEach>
                                    <c:url value="/restaurant/${restaurantId}/manage" var="url"/>
                                    <div class="mx-auto">
                                        <sc:pagination baseUrl="${url}" pages="${maxPages}"/>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <h2 class="display-5 text-center mt-5"><spring:message code="restaurant.manage.noReservations" /></h2>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="tab-pane fade show active" role="tabpanel" id="pending">

                        </div>
                    </div>
                </div>
            </section>
        </main>
    </jsp:body>
</sc:templateLayout>
