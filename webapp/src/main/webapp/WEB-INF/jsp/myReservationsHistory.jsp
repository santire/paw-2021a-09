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
                            <a href="<c:url value="/reservations"/>">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                                    <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
                                </svg>
                                <p class="d-inline"><spring:message code="navbar.myReservations"/> </p>
                            </a>
                            <h2 class="display-5 mt-4"><spring:message code="myReservations.history.title" /></h2>
                            <c:forEach var="reservation" items="${reservations}">
                                <div class="row mt-5">
                                    <sc:userReservationCardHistory
                                            reservation="${reservation}"/>
                                </div>
                            </c:forEach>
                            <div class="mx-auto">
                                <sc:pagination baseUrl="/reservations/history" pages="${maxPages}"/>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <a href="<c:url value="/reservations"/>">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                                    <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
                                </svg>
                                <p class="d-inline"><spring:message code="navbar.myReservations"/> </p>
                            </a>
                            <h2 class="display-5 text-center mt-5"><spring:message code="myReservations.history.noReservations" /></h2>
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
