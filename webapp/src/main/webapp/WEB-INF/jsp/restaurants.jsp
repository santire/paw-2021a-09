<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %> 
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<sc:templateLayout>
  <jsp:body>
    <main>
      <section>
        <div class="mx-auto mt-4 w-75">
          <c:choose>
            <c:when test="${empty restaurants}">
              <div class="row row-cols-1 row-cols-md-3">
                <div class="col-md-3">
                  <sc:filter />
                </div>
                <div class="row col-md-9 mx-auto my-auto text-center">
                  <c:choose>
                    <c:when test="${userIsSearching}">
                    <h2 class="display-5 w-100 p-0"><spring:message code="restaurants.search.noRestaurantsFound"/>'<c:out value="${searchString}"/>'</h2>
                    </c:when>
                    <c:otherwise>
                      <h2 class="display-5 w-100 p-0"><spring:message code="restaurants.noRestaurantsFound" /></h2>
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>
            </c:when>
            <c:otherwise>
              <c:choose>
                <c:when test="${userIsSearching}">
                  <h2 class="display-5"><spring:message code="restaurants.search.restaurantsFound"/>${searchString}</h2>
                </c:when>
                <c:otherwise>
                  <h2 class="display-5 mt-5"><spring:message code="restaurants.allRestaurants"/></h2>
                </c:otherwise>
              </c:choose>
              <div class="row row-cols-1 row-cols-md-3">
                <div class="col-md-3">
                  <sc:filter />
                </div>
                <div class="row row-cols-1 row-cols-md-3 col-md-9 ">
                  <c:forEach var="restaurant" items="${restaurants}" >
                    <div class="p-2 mb-4">
                      <sc:browseRestaurantCard
                              restaurant="${restaurant}"
                      />
                    </div>
                  </c:forEach>
                </div>
                <div class="mx-auto">
                  <sc:pagination baseUrl="/restaurants" pages="${maxPages}"/>
                </div>
             </div>

            </c:otherwise>
          </c:choose>

        </div>
      </section>
    </main>
  </jsp:body>
</sc:templateLayout> 
