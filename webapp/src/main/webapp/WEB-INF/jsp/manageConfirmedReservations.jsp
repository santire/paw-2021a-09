<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<sc:templateLayout>
    <jsp:body>
        <main>
            <section>
                <div class="container mt-4">
                    <a href="<c:url value="/restaurant/${restaurant.getId()}"/>">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                            <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
                        </svg>
                        <p class="d-inline">${restaurant.getName()}</p>
                    </a>
                    <h2 class="display-5 mt-5"><spring:message code="restaurant.manage.title"/> ${restaurant.getName()}</h2>
                    <ul class="nav nav-pills nav-fill navtop mt-5">
                        <li class="nav-item">
                            <a class="nav-link active"  href="#"><spring:message code="restaurant.manage.confirmedTab"/></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<c:url value="/restaurant/${restaurantId}/manage/pending"/>"> <spring:message code="restaurant.manage.pendingTab"/></a>
                        </li>
                    </ul>
                    <div class="tab-content mt-5">
                        <c:choose>
                            <c:when test="${restaurantHasConfirmedReservations}">
                                <c:forEach var="reservation" items="${confirmedReservations}">
                                    <div class="row mt-5">
                                        <sc:ownerReservationCards
                                                reservation="${reservation}"/>
                                    </div>
                                </c:forEach>
                                <div class="mx-auto">
                                    <sc:pagination baseUrl="/restaurant/${restaurant.getId()}/manage/confirmed" pages="${maxPages}"/>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <h2 class="display-5 text-center mt-5"><spring:message code="restaurant.manage.noReservations" /></h2>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </section>
        </main>
    </jsp:body>
</sc:templateLayout>
