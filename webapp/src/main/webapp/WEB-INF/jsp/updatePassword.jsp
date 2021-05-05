<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<c:url value="/reset-password" var="postFormUrl">
<c:param name="token" value="${param.token}"/>
</c:url>

<sc:templateLayout simpleTopBar="true">
  <jsp:body>

    <h2 class="text-center mt-5"><spring:message code="user.updatePassword.title" /></h2>
    <div class="card border-0 mx-auto w-50 mt-4">
      <div class="card body border-0">
        <form:form
           modelAttribute="passwordForm"
           action="${postFormUrl}"
           method="post"
        >
           <div class="row row-cols-1 row-cols-lg-1 justify-content-center">
              <div>
                  <form:label class="px-3 mx-auto w-100" path="password">
                    <spring:message code="Password" />:
                    <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="password" path="password"/>
                  </form:label>
                  <form:errors path="password" class="px-3 text-danger" element="p"/>
              </div>
              <div>
                  <form:label class="px-3 mx-auto w-100" path="repeatPassword">
                    <spring:message code="RepeatPassword" />:
                    <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="password" path="repeatPassword"/>
                  </form:label>
                  <form:errors path="repeatPassword" class="px-3 text-danger" element="p"/>
              </div>
          </div>
          <div>
            <input type="submit"
            class="btn btn-outline-secondary btn-block w-100 mt-3 px-0 mx-auto"
            value='<spring:message code="user.updatePassword.updateButton" />'/>
          </div>
        </form:form>
      </div>
    </div>

  </jsp:body>
</sc:templateLayout>
