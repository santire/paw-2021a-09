<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>


<sc:templateLayout>
    <jsp:body>

        Meter el edit de la foto

        <c:url value="/restaurant/${restaurant.getId()}/edit" var="path"/>
        <form:form modelAttribute="updateRestaurantForm" action="${path}?edit-restaurant-name" method="post" >
            <div>
                <form:errors path="name" cssStyle="color: red;" element="p"/>
                <form:label path="name">
                    Restaurant Name:
                    <form:input type="text" path="name" placeholder="${restaurant.name}"/>
                </form:label>
            </div>
            <div>
                <input type="submit" value="Save"/>
            </div>
        </form:form>

        <form:form modelAttribute="updateRestaurantForm" action="${path}?edit-restaurant-address" method="post" >
            <div>
                <form:errors path="address" cssStyle="color: red;" element="p"/>
                <form:label path="address">
                    Address:
                    <form:input type="text" path="address" placeholder="${restaurant.address}"/>
                </form:label>
            </div>
            <div>
                <input type="submit" value="Save"/>
            </div>
        </form:form>


        <form:form modelAttribute="updateRestaurantForm" action="${path}?edit-restaurant-phone" method="post" >
            <div>
                <form:errors path="phoneNumber" cssStyle="color: red;" element="p"/>
                <form:label path="phoneNumber">
                    Phone Number:
                    <form:input type="text" path="phoneNumber" placeholder="${restaurant.phoneNumber}"/>
                </form:label>
            </div>
            <div>
                <input type="submit" value="Save"/>
            </div>
        </form:form>



    </jsp:body>
</sc:templateLayout>
