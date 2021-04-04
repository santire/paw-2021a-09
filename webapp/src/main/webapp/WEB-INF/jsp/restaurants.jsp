<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %> <%@taglib
prefix="sc" tagdir="/WEB-INF/tags" %>

<sc:templateLayout>
  <jsp:body>
    <main>
      <section>
        <div class="container">
          <div class="row">
            <c:forEach var="restaurant" items="${restaurants}" >
              <div class="p-3 col-lg-2 col-md-3 col-4 col-12 my-15">
                <sc:restaurantCard
                  imgUrl="/resources/images/resto1.jpg"
                  restaurant="${restaurant}"
                />
              </div>
            </c:forEach>
          </div>
        </div>
      </section>
    </main>
  </jsp:body>
</sc:templateLayout>
