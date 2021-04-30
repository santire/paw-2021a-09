<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %> 
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@attribute name="page" required="true" type="java.lang.Integer"%>
<%@attribute name="pages" required="true" type="java.lang.Integer"%>
<%@attribute name="baseUrl" required="true" type="java.lang.String"%>

<c:set var="prevDisabled" value=""/>
<c:set var="nextDisabled" value=""/>

<c:if test="${empty param.page or param.page le 1}">
  <c:set var="prevDisabled" value="disabled"/>
</c:if>
<c:if test="${page ge pages}">
  <c:set var="nextDisabled" value="disabled"/>
</c:if>

<div class="container">
<nav aria-label="...">
  <ul class="pagination justify-content-center">
    <li class="page-item ${prevDisabled}">
      <c:url value="${baseUrl}" var="prevPage">
        <c:if test="${not empty param.search}">
          <c:param name="search" value="${param.search}"/>
        </c:if>
        <c:param name="page" value="${param.page-1}"/>
      </c:url>
      <a class="page-link" href="${prevPage}" tabindex="-1" aria-disabled="true">
        <span class="" aria-hidden="true">&laquo;</span>
      </a>
    </li>
    <c:forEach begin="1" end="${pages}" var="i" varStatus="loop">
      <c:url value="${baseUrl}" var="currPage">
        <c:if test="${not empty param.search}">
          <c:param name="search" value="${param.search}"/>
        </c:if>
        <c:param name="page" value="${i}"/>
      </c:url>
      <c:choose>
        <c:when test="${empty param.page and i eq 1}">
          <li class="page-item active" aria-current="page">
            <a class="page-link bg-secondary" href="${currPage}">${i}<span class="sr-only">(current)</span></a>
          </li>
        </c:when>
        <c:when test="${param.page eq i}">
          <li class="page-item active" aria-current="page">
            <a class="page-link bg-secondary" href="${currPage}">${i}<span class="sr-only">(current)</span></a>
          </li>
        </c:when>
        <c:otherwise>
          <li class="page-item">
            <a class="page-link text-orange" href="${currPage}">${i}</a>
          </li>
        </c:otherwise>
      </c:choose>
    </c:forEach>
    <li class="page-item ${nextDisabled}">
      <c:url value="${baseUrl}" var="nextPage">
        <c:if test="${not empty param.search}">
          <c:param name="search" value="${param.search}"/>
        </c:if>
        <c:param name="page" value="${param.page + 1}"/>
      </c:url>
      <a class="page-link" href="${nextPage}">
        <span class="" aria-hidden="true">&raquo;</span>
      </a>
    </li>
  </ul>
</nav>
</div>
