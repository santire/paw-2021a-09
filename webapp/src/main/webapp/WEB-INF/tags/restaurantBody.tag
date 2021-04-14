<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix ="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@attribute name="restaurant" required="true" type="ar.edu.itba.paw.model.Restaurant"%>
<%@attribute name="menu" required="true" type="java.util.List"%>

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
        <input
        onclick="$('#reservation-tab').tab('show')"
        class="btn btn-outline-secondary btn-block"
        value="Reservar Hoy" 
        />
      </div>
    </div>
  </div>
</div>
</div>

<ul class="nav nav-tabs pt-2" id="myTab" role="tablist">
  <li class="nav-item">
    <a class="nav-link active text-secondary" id="menu-tab" data-toggle="tab" href="#menu" role="tab" aria-controls="menu" aria-selected="true">Menu</a>
  </li>
  <li class="nav-item">
    <a class="nav-link text-secondary" id="reservation-tab" data-toggle="tab" href="#reservation" role="tab" aria-controls="reservation" aria-selected="false">Reservation</a>
  </li>
</ul>
<div class="tab-content" id="myTabContent">
  <div class="tab-pane fade show active" id="menu" role="tabpanel" aria-labelledby="menu-tab">
    <c:choose>
      <c:when test="${ menu.size() == 0 }">
        <h2 class="display-5">Menu no disponible</h2>
      </c:when>
      <c:otherwise>
        <ul class="list-group list-group-flush">
          <sc:menu menu="${menu}" />
        </ul>
      </c:otherwise>
    </c:choose>
  </div>
  <div class="tab-pane fade" id="reservation" role="tabpanel" aria-labelledby="reservation-tab" data-spy="reservation2" data-target="#reservation2"></div>
   <sc:reservationForm/> 
</div>
