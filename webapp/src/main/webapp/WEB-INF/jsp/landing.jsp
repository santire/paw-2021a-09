<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Gourmetable</title>
</head>
<body>
<%--<form action="${pageContext.request.contextPath}\search">
    <label>Search:
    <input type="text" name="find"/>
    </label>
    <input type="submit"/>
</form>--%>

<c:url value="${pageContext.request.contextPath}\search" var="path"/>
<form:form modelAttribute="SearchForm" action="${path}">
    <form:label path="search">Search a restaurant</form:label>
    <form:input path="search" type="text"/>
    <form:errors path="search" cssClass="formError"/>
</form:form>
</body>
</html>
