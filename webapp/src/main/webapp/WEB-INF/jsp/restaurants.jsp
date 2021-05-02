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
                  <h2 class="display-5 mt-5"><spring:message code="restaurants.allRestaurants"/></h2>
                </c:otherwise>
              </c:choose>


              <div class="row row-cols-1 row-cols-md-3">
                <div class="col-md-3">
                  <div class="card mt-2 p-0 mx-0">
                    <div class="card-body">
                      <div class="border-bottom pb-2 ml-2">
                        <h4 id="burgundy">Filters</h4>
                      </div>


                      <label>Tags</label>

                      <form>

                      <input type="hidden" id="seatchinput" name="search" value="${searchString}">

                      <c:forEach var="id" items="${tags.keySet()}" >
                        <c:choose>
                          <c:when test="${ tagsChecked.contains(id) == true}">
                            <div class="form-check">
                              <input class="form-check-input" name="tags" type="checkbox" value="${id}" id="flexCheckChecked" checked>
                              <label class="form-check-label" for="flexCheckChecked">
                                <spring:message code="restaurant.tag.${id}"/>
                              </label>
                            </div>
                          </c:when>
                          <c:otherwise>
                            <div class="form-check">
                              <input class="form-check-input" name="tags" type="checkbox" value="${id}" id="flexCheckDefault">
                              <label class="form-check-label" for="flexCheckDefault">
                                <spring:message code="restaurant.tag.${id}"/>
                              </label>
                            </div>
                          </c:otherwise>
                        </c:choose>
                      </c:forEach>

                        <hr>

                        <label for="minprice">min Price:</label>
                        <input value="${minPrice}" type="number" id="minprice" name="min" min="0">

                        <label for="maxprice">min Price:</label>
                        <input value="${maxPrice}" type="number" id="maxprice" name="max" min="0">

                        <hr>

                        <div class="form-group">
                        <label for="exampleFormControlSelect3">Order by</label>
                        <select name="sort" class="form-control" id="exampleFormControlSelect3">
                          <option value="namedesc">name desc</option>
                          <option value="nameasc">name asc</option>
                          <option value="populardesc">popular desc</option>
                          <option value="popularasc">popular asc</option>
                          <option value="reservationsdesc">hot desc</option>
                          <option value="reservationsasc">hot asc</option>
                          <option value="pricedesc">price desc</option>
                          <option value="priceasc">price asc</option>
                        </select>
                      </div>



                      <input
                              type="submit"
                              class="btn btn-outline-secondary btn-block w-100 px-0 mx-auto"
                              style="width: 100%;"
                              value='Search'
                      />

                      </form>


                    </div>
                  </div>
                </div>
                <div class="row row-cols-1 row-cols-md-3 col-md-9 ">
                  <c:forEach var="restaurant" items="${restaurants}" >
                    <div class="p-2 mb-4">
                      <sc:restaurantCard
                              restaurant="${restaurant}"
                      />
                    </div>
                  </c:forEach>
                </div>
                <c:url value="/restaurants" var="url"/>
                <div class="mx-auto">
                  <sc:pagination baseUrl="${url}" pages="${maxPages}"/>
                </div>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </section>
    </main>
  </jsp:body>
</sc:templateLayout>
