<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>


<sc:templateLayout simpleTopBar="true">
  <jsp:body>
    <c:if test="${not empty invalidToken}">
        <div class="alert alert-danger" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <strong><spring:message code="errors.invalidToken"></spring:message></strong>
        </div>
    </c:if>
    <h2 class="text-center mt-5"><spring:message code="user.requestPassword.title" /></h2>
    <h3 class="text-center mt-4"><spring:message code="user.requestPassword.content" /></h3>
  </jsp:body>
</sc:templateLayout>
