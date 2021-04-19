<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<sc:templateLayout>

    <jsp:body>

    <div class="row">
        <div class="col-md-3">
        </div>
        <div class="col-md-6">
            <div class="error-template">
                <c:if test= "${code == '404'}">
                    <h2>404 Not Found</h2>
                    <div class="error-details">
                        Sorry, an error has occured, Requested page not found!
                    </div>
                </c:if>
                <c:if test= "${code == '403'}">
                    <h2>403 Forbidden</h2>
                    <div class="error-details">
                        Sorry, you have not access for requested page!
                    </div>
                </c:if>
                <c:if test= "${code == '400'}">
                    <h2>400 Bad request</h2>
                    <div class="error-details">
                        Sorry, an error has occured!
                    </div>
                </c:if>
                <c:if test= "${code == '498'}">
                    <div class="error-details">
                        The user you are looking for doesnt exits
                    </div>
                </c:if>
                <c:if test= "${code == '499'}">
                    <div class="error-details">
                        The restaurant you are looking for doesnt exits
                    </div>
                </c:if>

                <div class="error-actions">
                    <a href="<c:url value="/"/>" class="btn btn-secondary btn-lg"><span class="glyphicon glyphicon-home"></span>Take Me Home </a>
                </div>
            </div>
        </div>
        <div class="col-md-3">
        </div>
    </div>

    </jsp:body>
</sc:templateLayout>

