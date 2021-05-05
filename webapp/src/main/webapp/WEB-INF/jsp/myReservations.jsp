<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<sc:templateLayout>
    <jsp:body>
        <main>
            <section>
                <div class="container mt-4">
                    <c:choose>
                        <c:when test="${userHasReservations}">
                            <h2 class="display-5"><spring:message code="myReservations.title" /></h2>
                                <c:forEach var="reservation" items="${reservations}">
                                    <div class="row mt-5">
                                        <sc:userReservationCard
                                                reservation="${reservation}"/>
                                    </div>
                                </c:forEach>
                                <div class="mx-auto">
                                    <sc:pagination baseUrl="/reservations" pages="${maxPages}"/>
                                </div>
                        </c:when>
                        <c:otherwise>
                            <h2 class="display-5 text-center mt-5"><spring:message code="myReservations.noReservations" /></h2>
                            <div class="justify-content-center mt-5">
                                <a class="text-decoration-none" href="<c:url value="/restaurants"/>">
                                    <input type="submit" class="btn btn-outline-secondary btn-block w-50 mt-3 px-0 mx-auto" value='<spring:message code="navbar.browse" />'>
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>
        </main>
    </jsp:body>
</sc:templateLayout>
