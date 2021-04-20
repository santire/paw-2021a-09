<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@attribute name="restaurant" required="true" type="ar.edu.itba.paw.model.Restaurant"%>

<div class="card h-100" 
     <%-- hack to make whole class clickable without breaking style --%>
     onclick="window.location='<c:url value="/restaurant/${restaurant.getId()}"/>'"
     style="cursor: pointer;"
>
  <c:choose>
    <c:when test="${restaurant.getMaybeProfileImage().isPresent()}" >
      <c:url value="data:image/jpg;base64,${restaurant.getMaybeProfileImage().get().getImageEnconded()}" var="imgUrl"/>
    </c:when>
    <c:otherwise>
      <c:url value="/resources/images/noimage.jpg" var="imgUrl"/>
    </c:otherwise>
  </c:choose>
  <img
    src="${imgUrl}"
    class="img-thumbnail rounded card-img-top"
    alt="..."
  />
  <div class="card-body d-flex flex-column">
    <div class="mt-auto">
      <h6 class="card-title text-break text-center">${restaurant.getName()}</h6>
      <%-- <p class="card-text">${restaurant.getDescription()}</p> --%>
      <a href="<c:url value="/restaurant/${restaurant.getId()}"/>" class="btn btn-outline-secondary btn-block"><spring:message code="restaurants.seeMore" /></a>
    </div>
  </div>
</div>

