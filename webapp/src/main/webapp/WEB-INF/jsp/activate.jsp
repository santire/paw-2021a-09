
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>


<sc:templateLayout simpleTopBar="true">
  <jsp:body>
    <h2 class="text-center mt-4"><spring:message code="home.activate.title" /></h2>
    <h3 class="text-center mt-4"><spring:message code="home.activate.content" /></h3>
  </jsp:body>
</sc:templateLayout>
