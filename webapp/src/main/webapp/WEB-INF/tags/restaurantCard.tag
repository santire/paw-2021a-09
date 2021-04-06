<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@attribute name="restaurant" required="true" type="ar.edu.itba.paw.model.Restaurant"%>

<div class="card h-100">
<c:url value="/resources/images/restaurants/${restaurant.getId()}.jpg" var="imgUrl"/>
  <img
    src="${imgUrl}"
    class="img-thumbnail rounded card-img-top"
    alt="..."
  />
  <div class="card-body d-flex flex-column">
    <div class="mt-auto">
      <h5 class="card-title">${restaurant.getName()}</h5>
      <%-- <p class="card-text">${restaurant.getDescription()}</p> --%>
      <a href="<c:url value="/restaurant/${restaurant.getId()}"/>" class="btn btn-outline-secondary btn-block">ver mas</a>
    </div>
  </div>
</div>

