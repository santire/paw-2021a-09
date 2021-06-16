<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix ="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<%@attribute name="restaurant" required="true" type="ar.edu.itba.paw.model.Restaurant"%>
<%@attribute name="times" required="true" type="java.util.List"%>
<%@attribute name="reviews" required="true" type="java.util.List"%>
<%@attribute name="userMadeComment" required="true" type="java.lang.Boolean"%>
<%@attribute name="userReview" required="false" type="ar.edu.itba.paw.model.Comment"%>
<%@attribute name="hasOnceReserved" required="false" type="java.lang.Boolean"%>
<%@attribute name="isOwner" required="false" type="java.lang.Boolean"%>


<div class="container">
    <div class="mb-5 my-2" style="max-height: 450px;">
        <div class="row no-gutters mt-5">
            <div class="col-md-7 mx-auto pb-3">
                <c:choose>
                    <c:when test="${not empty restaurant.getProfileImage()}" >
                        <c:url value="data:image/jpg;base64,${restaurant.getProfileImage().getImageEnconded()}" var="imgUrl"/>
                    </c:when>
                    <c:otherwise>
                        <c:url value="/resources/images/noimage.jpg" var="imgUrl"/>
                    </c:otherwise>
                </c:choose>
                <img src="${imgUrl}" class=" restaurant-img card-img img-fluid px-1 pr-md-5 border-right" alt="${name}" >
            </div>
            <div class="card border-0 col-md-5 my-auto mx-auto" style="">
                <div class="card-body px-auto mb-auto">
                    <div class="d-inline">
                        <h3 class="d-inline card-title">${restaurant.getName()}</h3>
                        <c:if test="${not empty isTheOwner}">
                            <h5 class="float-right">
                                <div>
                                    <a href="<c:url value="/restaurant/${restaurantId}/edit"/>">
                                        <p class="col-9 m-0 p-0 text-secondary">
                                            <i class="col-9 ml-3 p-1 text-muted fa fa-edit fa-2x" aria-hidden="true"></i>
                                        </p>
                                    </a>
                                </div>
                            </h5>
                        </c:if>
                    </div>

                    <div class="row row-cols-6 mt-3 m-0 p-0">
                        <p class="m-0 b-0 card-text"><medium class="text-muted">${restaurant.getRating()}<i class="text-muted fa fa-star checked pl-1"></i></medium></p>
                        <sc:like likeCount="${restaurant.getLikes()}"/>
                    </div>


                    <p class="card-text"><medium class="text-muted">${restaurant.getAddress()}</medium></p>
                    <p class="card-text"><medium class="text-muted">${restaurant.getPhoneNumber()}</medium></p>


                    <div class="row row-cols-3 mb-3 text-center">
                        <c:forEach var="tag" items="${restaurant.getTags()}">
                           <a style="text-decoration:none" href="${pageContext.request.contextPath}/restaurants?tags=${tag.getValue()}"> <div class="card-text border border rounded mr-2 mb-2"><medium class="text-muted">
                                <spring:message code="restaurant.tag.${tag.getValue()}"/>
                            </medium></div></a>
                        </c:forEach>
                    </div>


                    <c:if test="${not empty loggedUser}">
                        <c:if test="${not isTheOwner}">
                            <p class="card-text"><medium class="text-muted"><spring:message code="restaurant.rateTheRestaurant" /></medium></p>

                            <c:url value="/restaurant/${restaurantId}/rate" var="restUrl"/>
                            <form:form
                                    modelAttribute="ratingForm"
                                    action="${restUrl}"
                                    method="post"
                                    id="ratingForm">



                                <div id="rateYo"></div>


                                <form:input
                                        type="hidden"
                                        min="0"
                                        max="5"
                                        value="${userRatingToRestaurant}"
                                        path="rating"
                                        id="rating"
                                />
                            </form:form>

                        </c:if>
                    </c:if>

                    <c:if test="${facebook}"><i id="linkFacebook" onclick="javascript:openFacebook()" value="${restaurant.facebook}" class="fa fa-facebook"></i></c:if>
                    <c:if test="${twitter}"><i id="linkTwitter" onclick="javascript:openTwitter()" value="${restaurant.twitter}" class="fa fa-twitter"></i></c:if>
                    <c:if test="${instagram}"><i id="linkInstagram" onclick="javascript:openInstagram()" value="${restaurant.instagram}" class="fa fa-instagram"></i></c:if>


                    <c:if test="${not empty isTheOwner}">
                        <div class="mt-4">
                            <a href="<c:url value="/restaurant/${restaurant.getId()}/manage/confirmed"/>" class="btn btn-outline-secondary btn-block mt-4"><spring:message code="restaurant.reservation.button" /></a>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<%--RESERVATIONS, COMMENTS AND MENU--%>
<div class="row row-cols-1 row-cols-lg-2 w-75 mx-auto">

    <%--  COMMENTS AND MENU--%>
    <div class="order-2 order-lg-1">

        <ul class="nav nav-pills nav-fill navtop">
            <li class="nav-item">
            <h5 class="pb-1 text-center">
                <a class="nav-link"  href="<c:url value="/restaurant/${restaurant.getId()}"/>"><spring:message code="restaurant.menu.title"/></a>
            </h5>
            </li>
            <li class="nav-item">
            <h5 class="pb-1 text-center">
                <a class="nav-link active" href="#"><spring:message code="restaurant.reviews.title"/></a>
            </h5>
            </li>
        </ul>

        <c:choose>
            <%--WHEN USER IS LOGGED--%>
            <c:when test="${not empty loggedUser}">
                <c:choose>
                    <%--WHEN USER ALREADY MADE A COMMENT--%>
                    <c:when test="${userMadeComment}">
                        <h3 class="d-flex justify-content-center mt-2"><spring:message code="restaurant.reviews.yourReview"/></h3>
                        <div class="card p-3">
                            <div class="d-flex justify-content-between">
                                <div class="user d-flex flex-row align-items-center">
                                    <span>
                                        <small class="font-weight-bold text-primary">${userReview.getUser().getUsername()}</small>
                                        <small class="font-weight-bold" style="word-break: break-word"><c:out value="${userReview.getUserComment()}"/></small>
                                    </span>
                                </div>
                                <div class="d-flex justify-content-end align-items-center">
                                    <small class="float-right mr-4 ml-3" style="white-space: nowrap">${userReview.getDate()}</small>
                                    <button type="button" class="close"  data-toggle="modal" data-target="#deleteComment" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                    <!-- Modal -->
                                    <div class="modal fade" id="deleteComment" tabindex="-1" role="dialog" aria-labelledby="deleteComment" aria-hidden="true">
                                        <div class="modal-dialog modal-dialog-centered" role="document">
                                            <div class="modal-content">
                                                <div class="modal-body d-flex justify-content-center">
                                                    <spring:message code="restaurant.reviews.delete.confirmation"/>
                                                </div>
                                                <div class="modal-footer d-flex justify-content-center">
                                                    <button type="button" class="btn btn-secondary text-white mx-auto" data-dismiss="modal"><spring:message code="general.cancel"/></button>
                                                    <c:url value="/restaurant/${restaurant.getId()}/reviews/${userReview.getId()}/delete/" var="deleteReviewPath"/>
                                                    <form class="mx-auto"  action="${deleteReviewPath}" method="post">
                                                        <button type="submit" class="btn btn-danger text-white mx-auto"><spring:message code="restaurant.edit.deleteConfirmation"/></button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <hr  class="mt-5 mb-4" style="height:2px;border-width:0;color:gray;background-color:gray">
                    </c:when>
                    <%--USER NEVER MADE A COMMENT--%>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${hasOnceReserved and not isTheOwner}">
                                <h5 class="display-5 text-center mt-4 mb-5"><spring:message code="restaurant.reviews.create.title"/></h5>
                                <c:url value="/restaurant/${restaurant.getId()}/reviews" var="addReviewPath"/>
                                <form:form
                                modelAttribute="commentForm"
                                action="${addReviewPath}"
                                method="post">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text">${loggedUser.getUsername()}</span>
                                        </div>
                                        <form:textarea class="form-control" style="resize: none" cssStyle="word-wrap: break-word" path="review" aria-label="Review"></form:textarea>
                                    </div>
                                    <form:errors class="text-danger" element="p" path="review"/>
                                    <div class="mt-2 d-flex justify-content-end">
                                        <button class="btn btn-outline-warning"><spring:message code="restaurant.reviews.create.send"/></button>
                                    </div>
                                </form:form>
                                <hr style="height:2px;border-width:0;color:gray;background-color:gray">
                            </c:when>
                            <c:otherwise>
                                <c:if test="${!isTheOwner}">
                                    <p class="d-flex justify-content-center font-italic font-weight-light">
                                        <spring:message code="restaurant.reviews.cannotReview"/>
                                    </p>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <%--WHEN THERE ARE NO REVIEWS--%>
                    <c:when test="${empty reviews}">
                        <h2 class="display-5 text-center mt-4 mb-5"><spring:message code="restaurant.reviews.noReviews"/></h2>
                    </c:when>
                    <%--WHEN THERE ARE MORE REVIEWS--%>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${userMadeComment && reviews.size()==1}">
                                <h3 class="d-flex justify-content-center mb-4"><spring:message code="restaurant.reviews.noMoreReviews"/></h3>
                            </c:when>
                            <c:otherwise>
                                <h3 class="d-flex justify-content-center mb-4"><spring:message code="restaurant.reviews.otherCustomers"/></h3>
                                <c:forEach var="review" items="${reviews}">
                                 <sc:review review="${review}"></sc:review>
                                </c:forEach>
                                <div class="mx-auto">
                                  <sc:pagination baseUrl="/restaurant/${restaurant.getId()}/reviews" pages="${maxPages}"/>
                                </div>

                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </c:when>

            <%--WHEN ANONYMOUS USER--%>
            <c:otherwise>
                <c:choose>
                    <%--WHEN THERE ARE NO REVIEWS--%>
                    <c:when test="${empty reviews}">
                        <h2 class="display-5 text-center mt-4 mb-5"><spring:message code="restaurant.reviews.noReviews"/></h2>
                    </c:when>
                    <%--WHEN THERE ARE MORE REVIEWS--%>
                    <c:otherwise>
                        <h3 class="d-flex justify-content-center"><spring:message code="restaurant.reviews.customersReview"/></h3>
                        <c:forEach var="review" items="${reviews}">
                            <sc:review review="${review}"></sc:review>
                        </c:forEach>
                        <div class="mx-auto">
                          <sc:pagination baseUrl="/restaurant/${restaurant.getId()}/reviews" pages="${maxPages}"/>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>

    </div>

    <%--  RESERVATIONS // ADD MENU--%>
    <div class="order-1 order-lg-2">
        <c:choose>
            <c:when test="${isTheOwner}">
                <h3 class="pb-1 text-center ">
                    <spring:message code="restaurants.menuAdd"/>
                </h3>
                <sc:menuItemForm/>
            </c:when>
            <c:otherwise>
                <h3 class="pb-1 text-center">
                    <spring:message code="restaurants.reservation"/>
                </h3>
                <c:choose>
                    <c:when test="${not empty loggedUser}">
                        <sc:reservationForm
                                times="${times}"
                        />
                    </c:when>
                    <c:otherwise>
                        <h5 class="text-center mt-5">
                            <a href="<c:url value="/login"/>">
                                <spring:message code="general.toUser.login"></spring:message>
                            </a>
                            <spring:message code="general.or"/>
                            <a href="<c:url value="/register"/>">
                                <spring:message code="general.toUser.register"></spring:message>
                            </a>
                            <spring:message code="general.toMakeReservations"/>.
                        </h5>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
    </div>
</div>




</div>

