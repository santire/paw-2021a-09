<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix ="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<%@attribute name="restaurant" required="true" type="ar.edu.itba.paw.model.Restaurant"%>

<div class="container">
  <div class="mb-5 my-2" style="max-height: 450px;">
    <div class="row no-gutters mt-5">
      <div class="col-md-7 mx-auto pb-3">
        <c:choose>
          <c:when test="${restaurant.getMaybeProfileImage().isPresent()}" >
            <c:url value="data:image/jpg;base64,${restaurant.getMaybeProfileImage().get().getImageEnconded()}" var="imgUrl"/>
          </c:when>
          <c:otherwise>
            <c:url value="/resources/images/noimage.jpg" var="imgUrl"/>
          </c:otherwise>
        </c:choose>
        <img src="${imgUrl}" class=" restaurant-img card-img img-fluid px-1 pr-md-5 border-right" alt="${name}" >
      </div>
      <div class="card border-0 col-md-5 my-auto mx-auto" style="">
        <div class="card-body px-auto mb-auto">
          <div class="d-inline">
          <h5 class="d-inline card-title">${restaurant.getName()}</h5>
          <h5 class="float-right"><sc:like/></h5>
          </div>
          <p class="card-text"><medium class="text-muted">${restaurant.getAddress()}</medium></p>
          <p class="card-text"><medium class="text-muted">${restaurant.getPhoneNumber()}</medium></p>
          <div class="row row-cols-2 m-0 p-0">
            <p class="col-3 m-0 p-0 text-secondary">${restaurant.getRating()}/10 <i class="fa fa-cutlery" aria-hidden="true"></i></p>
            <c:if test="${not empty isTheOwner}">
              <div>
                <a href="<c:url value="/restaurant/${restaurantId}/edit"/>">
                  <p class="col-9 m-0 p-0 text-secondary">
                    <spring:message code="restaurant.editButton"/><i class="col-9 m-0 p-1 text-muted fa fa-edit" aria-hidden="true"></i>
                  </p>
                </a>
              </div>
            </c:if>
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
    <c:choose>
      <c:when test="${isTheOwner}">
        <h3 class="pb-1 text-center border-bottom">
          <spring:message code="restaurants.menuAdd"/>
        </h3>
        <sc:menuItemForm/> 
      </c:when>
      <c:otherwise>
        <h3 class="pb-1 text-center border-bottom">
          <spring:message code="restaurants.reservation"/>
        </h3>
        <c:choose>
          <c:when test="${not empty loggedUser}">
            <sc:reservationForm/>
          </c:when>
          <c:otherwise>
            <h5 class="text-center mt-5">
              <a href="/login">
                <spring:message code="general.toUser.login"></spring:message>
              </a>
              <spring:message code="general.or"/>
              <a href="/register">
                <spring:message code="general.toUser.register"></spring:message>
              </a>
              <spring:message code="general.toMakeReservations"/>.
            </h5>
          </c:otherwise>
        </c:choose>
      </c:otherwise>
    </c:choose>
    </div>
  </div>
</div>

