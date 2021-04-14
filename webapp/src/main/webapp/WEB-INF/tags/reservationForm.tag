<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%> <%@ taglib
prefix="form" uri="http://www.springframework.org/tags/form" %> <%@ taglib
prefix ="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>

<c:url value="/restaurant/${restaurantId}" var="postFormUrl" />

<div class="card border-0" style="max-width: 300px">
  <div class="card-body">
    <form:form
      modelAttribute="reservationForm"
      action="${postFormUrl}"
      method="post"
    >
      <form:errors path="email" cssStyle="color: red;" element="p" />
      <form:label path="email">
        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text"><i class="fa fa-envelope"></i></span>
          </div>
          <form:input
            type="text"
            path="email"
            cssClass="form-control"
            aria-label="Ingrese su email"
          />
        </div>
      </form:label>

      <form:errors path="date" cssStyle="color: red;" element="p" />
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
            cssClass="form-control"
            aria-label="Seleccione horario para su reserva"
          />
          <div class="input-group-append">
            <span class="input-group-text">:00</span>
          </div>
        </div>
      </form:label>

      <form:errors path="quantity" cssStyle="color: red;" element="p" />
      <form:label path="quantity">
        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text"><i class="fa fa-users"></i></span>
          </div>
          <form:input
            type="number"
            min="1"
            max="12"
            path="quantity"
            cssClass="form-control"
            aria-label="Cantidad de gente"
          />
          <div class="input-group-append">
            <span class="input-group-text">personas</span>
          </div>
        </div>
      </form:label>

      <input
        type="submit"
        class="btn btn-outline-secondary btn-block"
        value="Reservar"
      />
    </form:form>
  </div>
</div>
