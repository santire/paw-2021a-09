<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gourmetable</title>
</head>
<body>
<c:url value="/registerRestaurant" var="path"/>
<form:form modelAttribute="RestaurantForm" action="${path}" method="post">
    <div>
        <form:errors path="name" cssStyle="color: red;" element="p"/>
        <form:label path="name">
            Restaurant Name:
            <form:input type="text" path="name"/>
        </form:label>
    </div>
    <div>
        <form:errors path="address" cssStyle="color: red;" element="p"/>
        <form:label path="address">
            Address:
            <form:input type="text" path="address"/>
        </form:label>
    </div>
    <div>
        <form:errors path="phoneNumber" cssStyle="color: red;" element="p"/>
        <form:label path="phoneNumber">
            Phone Number:
            <form:input type="text" path="phoneNumber"/>
        </form:label>
    </div>
    <div>
        <input type="submit" value="Register your restaurant!"/>
    </div>
</form:form>
</body>
</html>
