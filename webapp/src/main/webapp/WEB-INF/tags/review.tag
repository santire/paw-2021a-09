<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix ="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@attribute name="review" required="true" type="ar.edu.itba.paw.model.Comment"%>

<div class="card p-3 mb-4 mt-3">
    <div class="d-flex justify-content-between align-items-center">
        <div class="user d-flex flex-row align-items-center">
    <span>
        <small class="font-weight-bold text-primary">${review.getUser().getUsername()}</small>
        <small class="font-weight-bold" style="word-break: break-word"><c:out value="${review.getUserComment()}"/></small>
    </span>
        </div> <small class="ml-3" style="white-space: nowrap">${review.getDate()}</small>
    </div>
</div>

