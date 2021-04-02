<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@attribute name="imgUrl" required="true" type="java.lang.String"%>
<%@attribute name="name" required="true" type="java.lang.String"%>
<%@attribute name="description" required="true" type="java.lang.String"%>
<%@attribute name="restaurantId" required="true" type="java.lang.Integer"%>

<div class="card">
  <img
    src="${imgUrl}"
    class="img-thumbnail rounded card-img-top"
    alt="..."
  />
  <div class="card-body">
    <h5 class="card-title">${name}</h5>
    <p class="card-text">${description}</p>
    <a href="/restaurant/${restaurantId}" class="btn btn-outline-secondary btn-block">ver mas</a>
  </div>
</div>

