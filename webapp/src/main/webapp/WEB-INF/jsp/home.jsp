<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<sc:templateLayout>
  <jsp:body>
    <main>
      <section>
        <div class="container my-2">
          <c:choose>
            <c:when test="${empty popularRestaurants}">
              <h2 class="display-5">No hay restaurantes destacados</h2>
            </c:when>
            <c:otherwise>
              <h2 class="display-5">Restaurantes destacados</h2>
              <div class="owl-carousel owl-theme">
                <c:forEach var="restaurant" items="${popularRestaurants}" >
                  <c:url value="/resources/images/resto1.jpg" var="restaurantImageUrl" />
                  <sc:restaurantCard
                    imgUrl="${restaurantImageUrl}"
                    restaurant="${restaurant}"
                  />
                </c:forEach>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </section>
    </main>
  </jsp:body>
</sc:templateLayout>
