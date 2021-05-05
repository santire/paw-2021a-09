<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix ="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@attribute name="menu" required="true" type="java.util.List"%>



<div class="row row-cols-1 row-cols-lg-1 mx-auto">
  <c:forEach var="item" items="${menu}" >
  <c:url value="/restaurant/${restaurantId}/delete/${item.getId()}" var="deletePath"/>
    <li class="list-group-item border-0">
      <span class="col-md-10 mr-auto pull-left">
        <c:choose>
          <c:when test="${not empty isTheOwner}">
            <form action="${deletePath}" method="post">
              <button class="ml-0 pl-0 btn b-0 text-danger"type="submit" ><span class="fa fa-trash"/></button> <c:out value="${item.getName()}"/>
            </form>
          </c:when>
          <c:otherwise>
            <c:out value="${item.getName()}"/>
          </c:otherwise>
        </c:choose>
      </span>
      <span class="col-md-2 pl-5 ml-auto pull-right">$<fmt:formatNumber value="${item.getPrice()}" maxFractionDigits="0" /></span>
      <c:if test="${ item.getDescription().length() > 3 }">
        <p class="col-md-10 pull-left text-muted">${item.getDescription()}</p>
      </c:if>
    </li>
  </c:forEach>
</div>
