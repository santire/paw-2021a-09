<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix ="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>


<%@attribute name="restaurant" required="true" type="ar.edu.itba.paw.model.Restaurant"%>

<div class="card"
<%-- hack to make whole class clickable without breaking style --%>
     onclick="window.location='<c:url value="/restaurant/${restaurant.getId()}"/>'"
     style="cursor: pointer;"
>
    <c:choose>
        <c:when test="${not empty restaurant.getProfileImage()}" >
            <c:url value="data:image/jpg;base64,${restaurant.getProfileImage().getImageEnconded()}" var="imgUrl"/>
        </c:when>
        <c:otherwise>
            <c:url value="/resources/images/noimage.jpg" var="imgUrl"/>
        </c:otherwise>
    </c:choose>
    <div class="embed-responsive embed-responsive-16by9">
       <img
                src="${imgUrl}"
                class="embed-responsive-item card-img img-fluid img-thumbnail rounded card-img-top"
                style="object-fit: contain;"
                alt="..."
        />
    </div>
    <div class="card-body d-flex flex-column">
        <div class="mt-auto justify-content-center">
            <h6 class="card-title text-break text-center" style="min-height: 35px;">${restaurant.getName()}</h6>

            <div class="card-text d-flex flex-wrap justify-content-center align-items-center text-center mx-auto " style="min-height: 100px;">
                <c:forEach items="${restaurant.getTags()}" var="tag">
                    <div class=" border border rounded mr-2 mb-2 text-muted">
                        &nbsp;<spring:message code="restaurant.tag.${tag.getValue()}"/>&nbsp;
                    </div>
                </c:forEach>
            </div>
            <div class="d-flex justify-content-center">
                <p class="bt-0 mt-0 text-secondary text-center mx-auto">${restaurant.getLikes()} <i class="fa fa-heart" aria-hidden="true"></i></p>
                <p class="m-0 b-0 card-text mx-auto"><medium class="text-muted">${restaurant.getRating()}<i class="text-muted fa fa-star checked pl-1"></i></medium></p>
            </div>

            <a href="<c:url value="/restaurant/${restaurant.getId()}"/>" class="btn btn-outline-secondary btn-block mt-3"><spring:message code="restaurants.seeMore" /></a>
        </div>
    </div>
</div>

