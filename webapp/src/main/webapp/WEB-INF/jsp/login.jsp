<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<sc:templateLayout>
    <jsp:body>


        <c:if test="${error}">
            <div class="alert alert-danger" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <strong><spring:message code="hello.login.error"></spring:message></strong>
            </div>
        </c:if>


        <form class="row g-3" method="post">
            <div class="col-md-3">
            </div>
            <div class="col-md-6">
                <h2 class="display-5">Log in</h2>
                <hr>
                <label for="inputEmail4" class="form-label"><spring:message code="hello.register.userForm.email"></spring:message></label>
                <input name="email" type="email" class="form-control" id="inputEmail4">
            </div>
            <div class="col-md-3">
            </div>
            <div class="col-md-3">
            </div>
            <div class="col-md-6">
                <label for="inputPassword4" class="form-label"><spring:message code="hello.register.userForm.password"></spring:message></label>
                <input name="password" type="password" class="form-control" id="inputPassword4">
            </div>
            <div class="col-md-3">
            </div>
            <div class="col-md-3">
            </div>
            <div class="col-6">
                <div class="form-check">
                    <input class="form-check-input" name="rememberme" type="checkbox" id="gridCheck">
                    <label class="form-check-label" for="gridCheck">
                        <spring:message code="hello.login.rememberme"></spring:message>
                    </label>
                </div>
            </div>
            <div class="col-md-3">
            </div>
            <div class="col-md-3">
            </div>
            <div class="col-6">
                <button type="submit" class="btn btn-primary"><spring:message code="hello.login.signin"></spring:message></button>
            </div>
            <div class="col-md-3">
            </div>
        </form>

    </jsp:body>
</sc:templateLayout>



