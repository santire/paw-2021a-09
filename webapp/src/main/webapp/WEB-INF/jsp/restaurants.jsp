<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %> 
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<sc:templateLayout>
  <jsp:body>
    <main>
      <section>
        <div class="container mt-4">
          <c:choose>
            <c:when test="${empty restaurants}">
              <h2 class="display-5">No hay restaurantes</h2>
            </c:when>
            <c:otherwise>
            <div class="row row-cols-1 row-cols-md-4 row-cols-lg-6">
                <c:forEach var="restaurant" items="${restaurants}" >
                  <div class="col mb-4">
                    <sc:restaurantCard
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
