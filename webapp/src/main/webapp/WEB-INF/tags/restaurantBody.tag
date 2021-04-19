<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix ="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<%@attribute name="restaurant" required="true" type="ar.edu.itba.paw.model.Restaurant"%>

<div class="container">
<div class="mb-3 my-2" style="max-height: 450px;">
  <div class="row no-gutters">
    <div class="col-md-6 mx-auto pb-3">
      <c:url value="/resources/images/restaurants/${restaurant.getId()}.jpg" var="imgUrl"/>
      <img src="${imgUrl}" class="card-img img-fluid px-1 pr-md-5 border-right" alt="${name}">
    </div>
    <div class="card border-0 col-md-6 my-auto mx-auto" style="max-width: 300px;">
      <div class="card-body px-auto mb-auto">
      <h5 class="card-title">${restaurant.getName()}</h5>
      <p class="card-text"><medium class="text-muted">${restaurant.getAddress()}</medium></p>
      <p class="card-text"><medium class="text-muted">${restaurant.getPhoneNumber()}</medium></p>
      </div>
      </div>
    </div>
  </div>
</div>
</div>
<div class="row row-cols-1 row-cols-lg-2 w-75 mx-auto">
  <div class="order-2 order-lg-1">
    <h3 class="pb-1 text-center border-bottom">Menu</h3>
    <c:choose>
      <c:when test="${ restaurant.getMenu().size() == 0 }">
      <h2 class="display-5 text-center"><spring:message code="restaurants.menuNotAvailable" /></h2>
      </c:when>
      <c:otherwise>
        <ul class="list-group list-group-flush">
          <sc:menu menu="${restaurant.getMenu()}" />
        </ul>
      </c:otherwise>
    </c:choose>
  </div>
  <div class="order-1 order-lg-2">
    <h3 class="pb-1 text-center border-bottom">
      <spring:message code="restaurants.reservation"/>
    </h3>
      <sc:reservationForm/> 
    </div>
  </div>
</div>

