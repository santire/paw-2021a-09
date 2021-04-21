<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<%@ taglib prefix ="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="/restaurant/${restaurantId}" var="postFormUrl" />

<div class="card border-0 pl-2 mx-auto w-75">
  <div class="card-body">
    <form:form
      modelAttribute="reservationForm"
      action="${postFormUrl}"
      method="post"
    >
      <div class="row row-cols-1">
        <form:errors path="email" cssStyle="color: red;" element="p" />
        <form:label path="email">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text"><i class="fa fa-envelope"></i></span>
            </div>

            <c:set value="" var="emailPlaceholder"/>
            <c:if test="${not empty loggedUser}">
              <c:set value="${loggedUser.getEmail()}" var="emailPlaceholder"/>
            </c:if>
            <form:input
              type="text"
              path="email"
              value="${emailPlaceholder}"
              cssClass="form-control"
              aria-label="Ingrese su email"
            />
          </div>
        </form:label>
      </div>

      <div class="row row-cols-1 row-cols-lg-2">
        <div class="col-lg-7 mx-0 px-0 pr-1">
          <form:label path="date">
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text"><i class="fa fa-clock-o"></i></span>
              </div>
              <form:input
                type="number"
                min="0"
                max="23"
                path="date"
                value="18"
                cssClass="form-control text-right"
                aria-label="Seleccione horario para su reserva"
              />
              <div class="input-group-append">
                <span class="input-group-text">:00</span>
              </div>
            </div>
          </form:label>
          <form:errors path="date" cssStyle="color: red;" element="p" />
        </div>
        <div class="col-lg-5 mx-0 px-0">
          <form:label path="quantity">
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text"><i class="fa fa-users"></i></span>
              </div>
              <form:input
                type="number"
                min="1"
                max="12"
                value="2"
                path="quantity"
                cssClass="form-control text-right"
                aria-label="Cantidad de gente"
              />
            </div>
          </form:label>
          <form:errors path="quantity" cssStyle="color: red;" element="p" />
        </div>
      </div>

      <input
        type="submit"
        class="btn btn-outline-secondary btn-block w-100 px-0 mx-auto"
        style="width: 100%;"
        value='<spring:message code="restaurants.bookNow" />' 
      />
    </form:form>
  </div>
</div>