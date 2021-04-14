<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix ="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="menu" required="true" type="java.util.List"%>


<div class="row row-cols-1 row-cols-lg-2">
  <c:forEach var="item" items="${menu}" >
    <li class="list-group-item border-0">
      <span class="col-md-10 mr-auto pull-left">${item.getName()}</span>
      <span class="col-md-2 pl-5 ml-auto pull-right"><fmt:formatNumber value="${item.getPrice()}" type="currency" maxFractionDigits="0" /></span>
      <c:if test="${ item.getDescription().length() > 3 }">
        <p class="col-md-10 pull-left text-muted">${item.getDescription()}</p>
      </c:if>
    </li>
  </c:forEach>
</div>
