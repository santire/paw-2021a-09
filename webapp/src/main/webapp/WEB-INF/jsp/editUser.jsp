<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>


<sc:templateLayout>
    <jsp:body>

        <c:url value="/user/edit" var="path"/>
        <form:form modelAttribute="updateUserForm" action="${path}?edit-username" method="post" >
            <div>
                <form:errors path="username" cssStyle="color: red;" element="p"/>
                <form:label path="first_name">
                    Username:
                    <form:input type="text" path="username" placeholder="${user.username}"/>
                </form:label>
            </div>
            <div>
                <input type="submit" value="Save"/>
            </div>
        </form:form>

        <form:form modelAttribute="updateUserForm" action="${path}?edit-password" method="post">
            <div>
                <form:errors path="password" cssStyle="color: red;" element="p"/>
                <form:label path="password">
                    Password:
                    <form:input type="password" path="password" placeholder="********"/>
                </form:label>
            </div>
            <div>
                <input type="submit" value="Save"/>
            </div>
        </form:form>

        <form:form modelAttribute="updateUserForm" action="${path}?edit-first-name" method="post" >
            <div>
                <form:errors path="first_name" cssStyle="color: red;" element="p"/>
                <form:label path="first_name">
                    First Name:
                    <form:input type="text" path="first_name" placeholder="${user.first_name}"/>
                </form:label>
            </div>
            <div>
                <input type="submit" value="Save"/>
            </div>
        </form:form>

        <form:form modelAttribute="updateUserForm" action="${path}?edit-last-name" method="post" >
            <div>
                <form:errors path="last_name" cssStyle="color: red;" element="p"/>
                <form:label path="last_name">
                    Family Name:
                    <form:input type="text" path="last_name" placeholder="${user.last_name}"/>
                </form:label>
            </div>
            <div>
                <input type="submit" value="Save"/>
            </div>
        </form:form>

        <!--
        <form:form modelAttribute="updateUserForm" action="${path}?edit-email" method="post" >
            <div>
                <form:errors  name="emailError" cssStyle="color: red;" element="p"/>
                <form:errors path="email" cssStyle="color: red;" element="p"/>
                <form:label path="email">
                    E-Mail:
                    <form:input type="text" path="email" placeholder="${user.email}"/>
                </form:label>
            </div>
            <div>
                <input type="submit" value="Save"/>
            </div>
        </form:form>
         -->

        <form:form modelAttribute="updateUserForm" action="${path}?edit-phone" method="post" >
            <div>
                <form:errors path="phone" cssStyle="color: red;" element="p"/>
                <form:label path="phone">
                    Phone Number:
                    <form:input type="text" path="phone" placeholder="${user.phone}"/>
                </form:label>
            </div>
            <div>
                <input type="submit" value="Save"/>
            </div>
        </form:form>




        My restaurants:

        <c:forEach var="restaurant" items="${restaurants}" >
            <div class="col mb-4">
                    ${restaurant.name}
                <a href="<c:url value="/restaurant/${restaurant.id}/edit"/>" class="btn btn-secondary btn-lg"><span class="glyphicon glyphicon-home"></span>Edit</a>

                <!-- Button trigger modal -->
                <button type="button" class="btn btn-secondary" data-toggle="modal" data-target="#exampleModal">
                    Delete
                </button>
                <!-- Modal -->
                <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="exampleModalLabel">Delete restaurant</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                You cant undo this action
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                <a href="<c:url value="/restaurant/${restaurant.id}/delete"/>" class="btn btn-danger">Confirm</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>


    </jsp:body>
</sc:templateLayout>



