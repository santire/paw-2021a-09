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
            <c:when test="${empty restaurants}">
              <c:choose>
                <c:when test="${userIsSearching}">
                <h2 class="display-5"><spring:message code="restaurants.search.noRestaurantsFound"/>'<c:out value="${searchString}"/>'</h2>
                </c:when>
                <c:otherwise>
                  <h2 class="display-5"><spring:message code="restaurants.noRestaurantsFound" /></h2>
                </c:otherwise>
              </c:choose>
            </c:when>
            <c:otherwise>
              <c:choose>
                <c:when test="${userIsSearching}">
                  <h2 class="display-5"><spring:message code="restaurants.search.restaurantsFound"/>${searchString}</h2>
                </c:when>
                <c:otherwise>
                  <h2 class="display-5"><spring:message code="restaurants.allRestaurants"/></h2>
                </c:otherwise>
              </c:choose>



            <div class="row">
              <div class="col-3">


                  <c:forEach var="id" items="${tagsSelected}" >
                    <div class="row"><a href="${pageContext.request.contextPath}/restaurants-remove-tag?path=${pageContext.request.queryString}&removetag=${id}" type="button" class="btn btn-link"><spring:message code="restaurant.tag.${id}"/></a></div>

                  </c:forEach>


                <hr>

                <form method="post">
                  <div class="form-group">
                    <label for="exampleFormControlSelect1">Tags</label>
                    <select class="form-control" id="exampleFormControlSelect1" name="tag" onchange="this.form.submit();">
                      <option selected>Tags</option>
                      <c:forEach var="id" items="${tags.keySet()}" >
                        <option value="${id}"> <spring:message code="restaurant.tag.${id}"/></option>
                      </c:forEach>
                    </select>
                  </div>

                  <label for="customRange1" class="form-label">min price</label>
                  <input type="range" class="form-range" id="customRange1">

                  <label for="customRange1" class="form-label">max price</label>
                  <input type="range" class="form-range" id="customRange2">

                </form>

              </div>


            <div class="col-9">

              <div class="row row-cols-1 row-cols-md-4 row-cols-lg-4">

                <c:forEach var="restaurant" items="${restaurants}" >
                  <div class="col mb-4">
                    <sc:restaurantCard
                            restaurant="${restaurant}"
                    />
                  </div>
                </c:forEach>

              </div>

            </div>
            </div>

            </c:otherwise>
          </c:choose>
        </div>
      </section>
    </main>
  </jsp:body>
</sc:templateLayout>
