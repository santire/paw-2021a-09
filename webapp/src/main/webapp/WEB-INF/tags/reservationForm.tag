<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<%@ taglib prefix ="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@attribute name="times" required="true" type="java.util.List"%>
<c:url value="/restaurant/${restaurantId}" var="postFormUrl" />

<div class="card border-0 pl-2 mx-auto w-75">
  <div class="card-body">
    <form:form
      modelAttribute="reservationForm"
      action="${postFormUrl}"
      method="post"
    >
      <div class="row row-cols-1">
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
              readonly="true"
            />
          </div>
        </form:label>
        <form:errors path="email" class="px-3 text-danger" element="p"/>
      </div>

      <div class="row row-cols-1 row-cols-lg-2">

        <div class="col-lg-5 mx-0 px-0 pr-5">
            <form:label path="date">
              <spring:message code="restaurant.date.placeholder" var="datePlaceholder"/>
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text"><i class="fa fa-calendar-o"></i></span>
                  <form:input
                          id="datepicker"
                          type="text"
                          placeholder='${datePlaceholder}'
                          class="form-control"
                          path="date"
                          autocomplete="off"
                          required="true"
                  />
                </div>
                </div>
            </form:label>
            <form:errors path="date" class="px-3 text-danger" element="p"/>
        </div>


        <div class="col-lg-4 mx-0 px-0 pr-1">
          <form:label path="time" >
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text"><i class="fa fa-clock-o"></i></span>
              </div>
              <form:select cssClass="browser-default custom-select" path="time" items="${times}"/>
            </div>
          </form:label>
          <form:errors path="time" class="px-3 text-danger" element="p"/>
        </div>

        <div class="col-lg-3 mx-0 px-0">
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
          <form:errors path="quantity" class="px-3 text-danger" element="p"/>
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