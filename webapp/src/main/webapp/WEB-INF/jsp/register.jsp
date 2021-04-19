<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>


<sc:templateLayout>
  <jsp:body>

    <h2><spring:message code="hello.register.title" /></h2>
    <c:url value="/register" var="postFormUrl"/>
    <form:form modelAttribute="userForm" action="${postFormUrl}" method="post">
      <div>
        <form:errors path="username" cssStyle="color: red;" element="p"/>
        <form:label path="username">
          Username:
          <form:input type="text" path="username"/>
        </form:label>
      </div>
      <div>

        <form:errors path="password" cssStyle="color: red;" element="p"/>
        <form:label path="password">
          Password:
          <form:input type="password" path="password"/>
        </form:label>
      </div >
      <c:if test= "${error == 'password'}">
        <div style="color:red;">
          passwords doesnt match
        </div>
      </c:if>
      <div>
        <form:errors path="repeatPassword" cssStyle="color: red;" element="p"/>
        <form:label path="password">
          Repeat Password:
          <form:input type="password" path="repeatPassword"/>
        </form:label>
      </div>
      <div>
        <form:errors path="first_name" cssStyle="color: red;" element="p"/>
        <form:label path="first_name">
          First Name:
          <form:input type="text" path="first_name"/>
        </form:label>
      </div>
      <div>
        <form:errors path="last_name" cssStyle="color: red;" element="p"/>
        <form:label path="last_name">
          Family Name:
          <form:input type="text" path="last_name"/>
        </form:label>
      </div>
      <c:if test= "${error == 'email'}">
        <div style="color:red;">
          email already registered
        </div>
      </c:if>
      <div>
        <form:errors path="email" cssStyle="color: red;" element="p"/>
        <form:label path="email">
          E-Mail:
          <form:input type="text" path="email"/>
        </form:label>
      </div>
      <div>
        <form:errors path="phone" cssStyle="color: red;" element="p"/>
        <form:label path="phone">
          Phone Number:
          <form:input type="text" path="phone"/>
        </form:label>
      </div>
      <div>
        <input type="submit" value="Register!"/>
      </div>
    </form:form>



  </jsp:body>
</sc:templateLayout>




