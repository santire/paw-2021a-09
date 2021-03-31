<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
  <body>
    <h2>Please register</h2>
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
        <input type="submit" value="Register!"/>
      </div>
    </form:form>
  </body>
</html>
