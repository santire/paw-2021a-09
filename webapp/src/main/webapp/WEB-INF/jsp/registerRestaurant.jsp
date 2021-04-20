<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gourmetable</title>
</head>
<body>
<c:url value="/register/restaurant" var="path"/>
<form:form modelAttribute="RestaurantForm" action="${path}" method="post" enctype="multipart/form-data">
    <div>
        <form:errors path="name" cssStyle="color: red;" element="p"/>
        <form:label path="name">
            <spring:message code="register.restaurant.RestaurantName" />:    
            <form:input type="text" path="name"/>
        </form:label>
    </div>
    <div>
        <form:errors path="address" cssStyle="color: red;" element="p"/>
        <form:label path="address">
            <spring:message code="Address" />:
            <form:input type="text" path="address"/>
        </form:label>
    </div>
    <div>
        <form:errors path="phoneNumber" cssStyle="color: red;" element="p"/>
        <form:label path="phoneNumber">
            <spring:message code="PhoneNumber" />:
            <form:input type="text" path="phoneNumber"/>
        </form:label>
    </div>
    <div>
        <form:label path="profileImage">Select an image to upload</form:label>
        <form:input type="file" path="profileImage"/>
    </div>
    <div>
        <input type="submit" value="Register your restaurant!"/>
    </div>
</form:form>
</body>
</html>
