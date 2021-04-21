<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:url value="/register/restaurant" var="postFormUrl"/>
<sc:templateLayout >
  <jsp:body>
    <h2 class="text-center mt-4"><spring:message code="hello.register.restaurant.title" /></h2>
    <div class="card border-0 mx-auto w-75">
      <div class="card body border-0">
        <form:form
           modelAttribute="RestaurantForm"
           action="${postFormUrl}"
           method="post"
           enctype="multipart/form-data"
        >
           <div class="row row-cols-1 row-cols-lg-2 justify-content-center">
              <div>
                  <form:label class="px-3 mx-auto w-100" path="name">
                    <spring:message code="register.restaurant.RestaurantName" />:
                    <form:input class=" form-control mx-auto w-100 px-1 mx-auto w-100 input-group-text text-left" type="text" path="name"/>
                  </form:label>
                  <form:errors path="name" class="px-3 text-danger" element="p"/>

                  <form:label class="px-3 mx-auto w-100" path="address">
                    <spring:message code="Address" />:
                    <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="text" path="address"/>
                  </form:label>
                  <form:errors path="address" class="px-3 text-danger" element="p"/>

                <form:label class="px-3 mx-auto w-100" path="phoneNumber">
                  <spring:message code="PhoneNumber" />:
                  <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="text" path="phoneNumber"/>
                </form:label>
                <form:errors path="phoneNumber" class="px-3 text-danger" element="p"/>

                <form:label class="px-3 mx-auto w-100" path="profileImage">
                    Select an image to upload 
                </form:label>
                <form:input class="px-3 mx-auto w-100" type="file" path="profileImage"/>
                <form:errors path="profileImage" class="px-3 text-danger" element="p"/>
              </div>
          </div>
          <div>
            <input type="submit" class="btn btn-outline-secondary btn-block w-50 mt-3 px-0 mx-auto" value='<spring:message code="hello.register.restaurant.submit" />'>
          </div>
        </form:form>
      </div>
    </div>

  </jsp:body>
</sc:templateLayout>
