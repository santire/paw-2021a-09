<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@attribute name="imgUrl" required="true" type="java.lang.String"%>
<%@attribute name="restaurant" required="true" type="ar.edu.itba.paw.model.Restaurant"%>

<div class="card">
  <img
    src="${imgUrl}"
    class="img-thumbnail rounded card-img-top"
    alt="..."
  />
  <div class="card-body">
    <h5 class="card-title">${restaurant.getName()}</h5>
    <%-- <p class="card-text">${restaurant.getDescription()}</p> --%>
    <a href="<c:url value="/restaurant/${restaurant.getId()}"/>" class="btn btn-outline-secondary btn-block">ver mas</a>
  </div>
</div>

