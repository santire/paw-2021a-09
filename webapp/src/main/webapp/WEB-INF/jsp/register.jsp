<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<c:url value="/register" var="postFormUrl"/>

<sc:templateLayout simpleTopBar="true">
  <jsp:body>

    <h2 class="text-center mt-4"><spring:message code="hello.register.title" /></h2>
    <div class="card border-0 mx-auto w-75">
      <div class="card body border-0">
        <form:form
           modelAttribute="userForm"
           action="${postFormUrl}"
           method="post"
        >
           <div class="row row-cols-1 row-cols-lg-2 justify-content-center">
              <div>
                  <form:label class="px-3 mx-auto w-100" path="username">
                    <spring:message code="Username" />:
                    <form:input class=" form-control mx-auto w-100 px-1 mx-auto w-100 input-group-text text-left" type="text" path="username"/>
                  </form:label>
                  <form:errors path="username" class="px-3 text-danger" element="p"/>
              </div>
          </div>

          <div class="row row-cols-1 row-cols-lg-2 justify-content-center">
              <div>
                  <form:label class="px-3 mx-auto w-100" path="password">
                    <spring:message code="Password" />:
                    <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="password" path="password"/>
                  </form:label>
                  <form:errors path="password" class="px-3 text-danger" element="p"/>
              </div>
              <div>
                  <form:label class="px-3 mx-auto w-100" path="repeatPassword">
                    <spring:message code="Password" />:
                    <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="password" path="repeatPassword"/>
                  </form:label>
                  <form:errors path="repeatPassword" class="px-3 text-danger" element="p"/>
              </div>
          </div>

          <div class="row row-cols-1 row-cols-lg-2">
            <div class="mx-auto">
                <form:label class="px-3 mx-auto w-100" path="firstName">
                  <spring:message code="FirstName" />:
                  <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="text" path="firstName"/>
                </form:label>
                <form:errors path="firstName" class="px-3 text-danger" element="p"/>
            </div>
            <div class="mx-auto">
                <form:label class="px-3 mx-auto w-100" path="lastName">
                  <spring:message code="FamilyName" />:
                  <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="text" path="lastName"/>
                </form:label>
                <form:errors path="lastName" class="px-3 text-danger" element="p"/>
            </div>
          </div>

          <div class="row row-cols-1 row-cols-lg-2">
            <div>
                <form:label class="px-3 mx-auto w-100" path="email">
                  <spring:message code="email" />:
                  <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="text" path="email"/>
                </form:label>
                <form:errors path="email" class="px-3 text-danger" element="p"/>
                <form:errors path="emailInUse" class="px-3 text-danger" element="p"/>
            </div>
            <div>
                <form:label class="px-3 mx-auto w-100" path="phone">
                  <spring:message code="PhoneNumber" />:
                  <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="text" path="phone"/>
                </form:label>
                <form:errors path="phone" class="px-3 text-danger" element="p"/>
            </div>
          </div>

          <div>
            <input type="submit" class="btn btn-outline-secondary btn-block w-100 mt-3 px-0 mx-auto" value='<spring:message code="SignUp" />'/>
            <c:if test="${not empty tokenError}">
              <p class="px-3 text-danger"><spring:message code="errors.tokenError"/></p>
            </c:if>
          </div>
        </form:form>
        <div>
          <spring:message code="register.alreadyHaveAnAccountQuestion"/><a href="<c:url value="/login"/>"><spring:message code="register.clickHere"/></a>
        </div>
      </div>
    </div>

  </jsp:body>
</sc:templateLayout>
