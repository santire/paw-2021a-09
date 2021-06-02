<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<sc:templateLayout>
  <jsp:body>
    <main>
      <section>
        <spring:message code="home.reservations.confirmationMessage" var="confirmationMessage"/>
        <c:if test="${madeReservation}">
          <div class="alert alert-success alert-dissapear" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <strong><c:out value="${confirmationMessage}"/></strong> 
          </div>
        </c:if>
        <spring:message code="user.edit.confirmationMessage" var="confirmationMessage"/>
        <c:if test="${editedUser}">
          <div class="alert alert-success alert-dissapear" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <strong><c:out value="${confirmationMessage}"/></strong> 
          </div>
        </c:if>
        <div class=" p-0 m-0 overflow-hidden row d-none d-lg-flex">
          <div class="column" style="background-color:#bbb; opacity: 0.7;">
            <h2 style="vertical-align : middle;text-align:left;margin-top: 150px;margin-left:80px;font-size: 50px;"><spring:message code="home.phrase"></spring:message></h2>
          </div>
          <div class="column" style="background-color:#bbb; opacity: 0.7; max-height: 420px;">
            <img class="card-img-top img-fluid pull-right mr-5 pr-5" style="max-height: 380px; width: auto;" src="<c:url value="/resources/images/home_image.png"/>" alt="">
          </div>
        </div>
        <div class="mx-auto my-2 mt-5 w-75">

          <c:choose>
            <c:when test="${empty likedRestaurants}">
              <%--<h2 class="display-5"><spring:message code="home.noLikedRestaurantsFound" /></h2>--%>
            </c:when>
            <c:otherwise>
              <h2 class="display-5" ><spring:message code="home.likedRestaurants" /></h2>
              <div class="row mb-5">
                <div class="owl-carousel owl-theme">
                  <c:forEach var="restaurant" items="${likedRestaurants}" >
                    <c:url value="/resources/images/resto1.jpg" var="restaurantImageUrl" />
                    <sc:browseRestaurantCard
                            restaurant="${restaurant}"
                    />
                  </c:forEach>
                </div>
              </div>
            </c:otherwise>
          </c:choose>

          <c:choose>
            <c:when test="${empty hotRestaurants}">
              <%--<h2 class="display-5"><spring:message code="home.noLikedRestaurantsFound" /></h2>--%>
            </c:when>
            <c:otherwise>
              <h2 class="display-5" ><spring:message code="home.hotRestaurants" /></h2>
              <div class="row mb-5">
                <div class="owl-carousel owl-theme">
                  <c:forEach var="restaurant" items="${hotRestaurants}" >
                    <c:url value="/resources/images/resto1.jpg" var="restaurantImageUrl" />
                    <sc:browseRestaurantCard
                            restaurant="${restaurant}"
                    />
                  </c:forEach>
                </div>
              </div>
            </c:otherwise>
          </c:choose>

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
                    <sc:browseRestaurantCard
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
