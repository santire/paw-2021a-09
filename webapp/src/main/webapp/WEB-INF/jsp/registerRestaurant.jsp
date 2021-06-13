<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url value="/register/restaurant" var="postFormUrl"/>
<sc:templateLayout >
  <jsp:body>
    <div class="card mx-auto w-50 mt-4 mb-4">
        <div class="card body">
            <h2 class="text-center mt-4 mb-4 card-title"><spring:message code="hello.register.restaurant.title" /></h2>

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
                    <form:input class=" form-control mx-auto w-100 px-1 mx-auto w-100 input-group-text text-left" type="text" required="true"  path="name"/>
                  </form:label>
                  <form:errors path="name" class="px-3 text-danger" element="p"/>

                  <form:label class="px-3 mx-auto w-100" path="address">
                    <spring:message code="Address" />:
                    <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="text" required="true" path="address"/>
                  </form:label>
                  <form:errors path="address" class="px-3 text-danger" element="p"/>

                <form:label class="px-3 mx-auto w-100" path="phoneNumber">
                  <spring:message code="PhoneNumber" />:
                  <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="text" required="true" minlength="8" path="phoneNumber"/>
                </form:label>
                <form:errors path="phoneNumber" class="px-3 text-danger" element="p"/>

                  <hr style="height:2px;border-width:0;color:gray;background-color:gray">

                <form:label class="px-3 mx-auto w-100" path="profileImage">
                    <spring:message code="restaurant.register.profileImage"/>
                </form:label>
                  <div class="input-group">
                      <div class="col-md-6">
                          <form:input class="px-3 mx-auto w-100 align-self-center" type="file" path="profileImage" id="image"/>
                      </div>
                      <div class="col-md-6">
                          <form:button  id="reset" type="button" class="align-self-center">
                              <spring:message code="general.cancel"/>
                          </form:button>
                      </div>
                  </div>
                  <form:errors path="profileImage" class="px-3 text-danger" element="p"/>


                  <hr style="height:2px;border-width:0;color:gray;background-color:gray">

                  <spring:message code="restaurant.socialmedia" />

                  <form:label class="px-3 mx-auto w-100" path="facebook">
                      <spring:message code="restaurant.socialmedia.facebook" />:
                      <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="text" placeholder="www.facebook.com/myprofile" path="facebook"/>
                  </form:label>
                  <form:errors path="facebook" class="px-3 text-danger" element="p"/>

                  <form:label class="px-3 mx-auto w-100" path="instagram">
                      <spring:message code="restaurant.socialmedia.instagram" />:
                      <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="text" placeholder="www.instagram.com/myprofile" path="instagram"/>
                  </form:label>
                  <form:errors path="instagram" class="px-3 text-danger" element="p"/>

                  <form:label class="px-3 mx-auto w-100" path="twitter">
                      <spring:message code="restaurant.socialmedia.twitter" />:
                      <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="text" placeholder="www.twitter.com/myprofile" path="twitter"/>
                  </form:label>
                  <form:errors path="twitter" class="px-3 text-danger" element="p"/>

                  <hr style="height:2px;border-width:0;color:gray;background-color:gray">

                  <div class="container">
                      <spring:message code="restaurant.edit.tags" />
                      <form:errors path="tags" class="px-3 text-danger" element="p"/>
                      <div class="row">
                          <c:forEach var="id" items="${tags.keySet()}" >
                                      <div class="col-md-4">
                                          <div class="form-check">
                                              <form:checkbox path="tags" class="form-check-input" name="tags" value="${id}" id="flexCheckDefault"/>
                                              <label class="form-check-label" for="flexCheckDefault">
                                                  <spring:message code="restaurant.tag.${id}"/>
                                              </label>
                                          </div>
                                      </div>
                          </c:forEach>
                      </div>
                  </div>
              </div>
          </div>
          <div>
            <input type="submit" class="btn btn-outline-secondary btn-block w-50 mt-3 px-0 mx-auto mb-4 mt-4" value='<spring:message code="hello.register.restaurant.submit" />'>
          </div>
        </form:form>
      </div>
    </div>

  </jsp:body>
</sc:templateLayout>
