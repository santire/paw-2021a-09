<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<%@ taglib prefix ="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="/restaurant/${restaurantId}/menu" var="menuPostFormUrl" />

<div class="card border-0 pl-2 mx-auto w-75">
  <div class="card-body">
    <form:form
      modelAttribute="menuItemForm"
      action="${menuPostFormUrl}"
      method="post"
    >
       <div class="row row-cols-1 px-4 justify-content-center">
          <div class="w-100 mx-auto">
              <input type="hidden" name="page" value="${param.page}">
              <form:label class="w-100 mx-auto" path="name">
                <spring:message code="register.restaurant.MenuName" />:
                <form:input 
                  class="form-control" 
                  type="text" 
                  path="name"
                />
              </form:label>
              <form:errors path="name" class="text-danger" element="p"/>
          </div>
          <div class="w-100 mx-auto">
              <form:label class="w-100 mx-auto" path="description">
                <spring:message code="register.restaurant.MenuDescription" />:
                <form:input
                  class="form-control"
                  type="text"
                  path="description"/>
              </form:label>
              <form:errors path="description" class="text-danger" element="p"/>
          </div>
          <div class="w-100 mx-auto">
            <form:label class="w-100 mx-auto" path="price">
              <spring:message code="register.restaurant.MenuPrice" />:
              <form:input
              class="form-control"
              type="number"
              step="0.01"
              path="price"/>
            </form:label>
            <form:errors path="price" class="text-danger" element="p"/>
          </div>
      </div>

      <input
        type="submit"
        class="btn btn-outline-secondary btn-block w-100 px-0 mx-auto"
        style="width: 100%;"
        value='<spring:message code="MenuItemSubmit" />' 
      />
    </form:form>
  </div>
</div>
