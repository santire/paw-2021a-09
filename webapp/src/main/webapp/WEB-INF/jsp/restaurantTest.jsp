<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<body>
<h2><spring:message code="restaurants.reservation"/></h2>
<c:url value="/restaurant/${restaurantId}" var="postFormUrl"/>
<form:form modelAttribute="reservationForm" action="${postFormUrl}" method="post">
    <div>
        <form:errors path="quantity" cssStyle="color: red;" element="p"/>
        <form:label path="quantity">
            Quantity:
            <form:input type="text" path="quantity"/>
        </form:label>
    </div>
    <div>
        <form:errors path="date" cssStyle="color: red;" element="p"/>
        <form:label path="date">
            Date:
            <form:input type="text" path="date"/>
        </form:label>
    </div>



    <div>
        <input type="submit" value="Confirm"/>
    </div>
</form:form>
</body>
</html>
