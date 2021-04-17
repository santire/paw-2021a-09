<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix ="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

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
      <c:url value="/restaurant/${restaurantId}" var="postFormUrl"/>
      <form:form modelAttribute="reservationForm" action="${postFormUrl}" method="post">
        <h5 class="card-title">${restaurant.getName()}</h5>
        <p class="card-text"><medium class="text-muted">${restaurant.getAddress()}</medium></p>
        <p class="card-text"><medium class="text-muted">${restaurant.getPhoneNumber()}</medium></p>

        <form:errors path="email" cssStyle="color: red;" element="p"/>
        <form:label path="email">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text"><i class="fa fa-envelope"></i></span>
            </div>
            <form:input type="text" path="email" cssClass="form-control" aria-label="Ingrese su email" />
          </div>
        </form:label>

        <form:errors path="date" cssStyle="color: red;" element="p"/>
        <form:label path="date">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text"><i class="fa fa-clock-o"></i></span>
            </div>
            <form:input type="number" min="0" max="23" path="date" cssClass="form-control" aria-label="Seleccione horario para su reserva"/>
            <div class="input-group-append">
              <span class="input-group-text">:00</span>
            </div>
          </div>
        </form:label>

        <form:errors path="quantity" cssStyle="color: red;" element="p"/>
        <form:label path="quantity">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text"><i class="fa fa-users"></i></span>
            </div>
            <form:input type="number" min="1" max="12" path="quantity" cssClass="form-control" aria-label="Cantidad de gente"/>
            <div class="input-group-append">
              <span class="input-group-text"><spring:message code="restaurants.partySize" /></span>
            </div>
          </div>
        </form:label>

        </div>
        <input
        type="submit"
        class="btn btn-outline-secondary btn-block"
        value='<spring:message code="restaurants.bookNow" />' 
        />
      </div>
      </form:form>
    </div>
  </div>
</div>
</div>
<div class="restaurant-separator">
</div>

<div class="container border-top mt-1">
  <h3 class="pt-2">Menu</h3>
  <c:choose>
    <c:when test="${ menu.size() == 0 }">
      <h2 class="display-5"><spring:message code="restauants.menuNotAvailable" /></h2>
    </c:when>
    <c:otherwise>
      <ul class="list-group list-group-flush">
        <c:forEach var="item" items="${menu}" >
          <li class="list-group-item">
            <span class="col-md-10 mr-auto pull-left">${item.getName()}</span>
            <span class="col-md-2 pl-5 ml-auto pull-right"><fmt:formatNumber value="${item.getPrice()}" type="currency" maxFractionDigits="0" /></span>
          </li>
        </c:forEach>
      </ul>
    </c:otherwise>
  </c:choose>
</div>
