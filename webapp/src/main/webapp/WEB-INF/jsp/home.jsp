<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<sc:templateLayout>
  <jsp:body>
    <main>
      <section>
        <c:if test="${madeReservation}">
          <div class="alert alert-success" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <strong><spring:message code="home.reservation.noRestaurantsFound" /></strong> <c:out value='<spring:message code="home.reservations.confirmationMessage" />'/>
          </div>
        </c:if>
        <div class="container my-2">
          <c:choose>
            <c:when test="${empty popularRestaurants}">
              <h2 class="display-5"><spring:message code="home.noHighlightsFound" /></h2>
            </c:when>
            <c:otherwise>
              <h2 class="display-5" ><spring:message code="home.highlightRestaurant" /></h2>
              <div class="row mb-5">
                <div class="owl-carousel owl-theme">
                  <c:forEach var="restaurant" items="${popularRestaurants}" >
                    <c:url value="/resources/images/resto1.jpg" var="restaurantImageUrl" />
                    <sc:restaurantCard
                      restaurant="${restaurant}"
                    />
                  </c:forEach>
                </div>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </section>
    </main>
  </jsp:body>
</sc:templateLayout>
