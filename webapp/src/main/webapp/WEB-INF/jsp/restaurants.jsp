<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %> 
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<sc:templateLayout>
  <jsp:body>
    <main>
      <section>
        <div class="container">
          <c:choose>
            <c:when test="${empty restaurants}">
              <h2 class="display-5">No hay restaurantes</h2>
            </c:when>
            <c:otherwise>
              <div class="row">
                <c:forEach var="restaurant" items="${restaurants}" >
                  <div class="p-3 col-lg-2 col-md-3 col-4 col-12 my-15">
                    <c:url value="/resources/images/resto1.jpg" var="restaurantImageUrl" />
                    <sc:restaurantCard
                      imgUrl="${restaurantImageUrl}"
                      restaurant="${restaurant}"
                    />
                  </div>
                </c:forEach>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </section>
    </main>
  </jsp:body>
</sc:templateLayout>
