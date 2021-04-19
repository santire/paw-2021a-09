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
                    <h2><spring:message code="error.404.title" /></h2>
                    <div class="error-details">
                        <spring:message code="error.404.details" />
                    </div>
                </c:if>
                <c:if test= "${code == '403'}">
                    <h2><spring:message code="error.403.title" /></h2>
                    <div class="error-details">
                        <spring:message code="error.403.details" />
                    </div>
                </c:if>
                <c:if test= "${code == '400'}">
                    <h2><spring:message code="error.400.title" /></h2>
                    <div class="error-details">
                        <spring:message code="error.400.details" />
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
                    <a href="<c:url value="/"/>" class="btn btn-secondary btn-lg"><span class="glyphicon glyphicon-home"></span><spring:message code="error.home" /> </a>
                </div>
            </div>
        </div>
        <div class="col-md-3">
        </div>
    </div>

    </jsp:body>
</sc:templateLayout>

