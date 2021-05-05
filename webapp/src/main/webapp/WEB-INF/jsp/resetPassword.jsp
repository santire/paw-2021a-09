
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<c:url value="/forgot-password" var="postFormUrl"/>

<sc:templateLayout simpleTopBar="true">
  <jsp:body>
    <c:if test="${not empty param.expiredToken}">
        <div class="alert alert-danger" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <strong><spring:message code="errors.expiredToken"></spring:message></strong>
        </div>
    </c:if>
    <h2 class="text-center mt-5"><spring:message code="user.forgotPassword.title" /></h2>
    <div class="card border-0 mx-auto w-75">
      <div class="card body border-0">
        <form:form
           modelAttribute="emailForm"
           action="${postFormUrl}"
           method="post"
        >
           <div class="row row-cols-1 row-cols-lg-2 justify-content-center">
              <div>
                  <form:label class="px-3 mx-auto w-100" path="email">
                    <spring:message code="Email" />:
                    <form:input class=" form-control mx-auto w-100 px-1 mx-auto w-100 input-group-text text-left" type="text" path="email"/>
                  </form:label>
                  <form:errors path="email" class="px-3 text-danger" element="p"/>
              </div>
          </div>
          <div>
            <input 
            type="submit"
            class="btn btn-outline-secondary btn-block w-50 mt-3 px-0 mx-auto"
            value='<spring:message code="user.forgotPassword.button" />'/>
          </div>
            <div class="text-center mt-3">
                <spring:message code="register.alreadyHaveAnAccountQuestion"/> <a href="<c:url value="/login"/>"><spring:message code="register.clickHere"/></a>
            </div>
        </form:form>
      </div>
    </div>

  </jsp:body>
</sc:templateLayout>
