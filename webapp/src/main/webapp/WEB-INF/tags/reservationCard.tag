<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<%@attribute name="reservation" required="true" type="ar.edu.itba.paw.model.Reservation"%>
<%@attribute name="isOwner" required="true" type="java.lang.Boolean"%>

    <div class="card border-0">
        <div class="row row-cols-1 row-cols-md-3">
            <div class="col border d-flex align-items-center justify-content-center border-0">
                    <c:choose>
                        <c:when test="${not empty reservation.getRestaurant().getProfileImage()}">
                            <c:url value="data:image/jpg;base64,${reservation.getRestaurant().getProfileImage().getImageEnconded()}" var="imgUrl"/>
                        </c:when>
                        <c:otherwise>
                            <c:url value="/resources/images/noimage.jpg" var="imgUrl"/>
                        </c:otherwise>
                    </c:choose>
                <c:url value="/restaurant/${reservation.getRestaurant().getId()}" var="restaurantPage"/>
                    <a href="${restaurantPage}">
                        <img
                                src="${imgUrl}"
                                class="restaurant-img card-img img-fluid img-thumbnail rounded card-img-top"
                                alt="..."
                        />
                    </a>
            </div>
            <div class="col border d-flex align-items-center justify-content-center border-0">
                <span class="block">
                    <div class="p-2 bd-highlight text-muted">${reservation.getRestaurant().getName()}</div>
                    <div class="p-2 bd-highlight text-muted">${reservation.getRestaurant().getAddress()}</div>
                    <div class="p-2 bd-highlight text-muted">${reservation.getRestaurant().getPhoneNumber()}</div>
                    <div class="p-2 bd-highlight"><spring:message code="reservation.card.reservationFor"/> ${reservation.getQuantity()}
                        <spring:message code="reservation.card.people"/></div>
                    <div class="p-2 bd-highlight"><spring:message code="reservation.card.date"/>: ${reservation.getDate()}</div>
                </span>
        </div>
        <div class="col border d-flex align-items-center justify-content-center border-0">
            <c:url value="/reservations/${reservation.getId()}/modify" var="modifyReservationPath"/>
            <c:url value="/reservations/${reservation.getId()}/cancel" var="cancelReservationPath"/>
            <span class="block">
                    <c:choose>
                        <c:when test="${reservation.isConfirmed()}">
                            <h5 class="mb-5 mx-auto" style="color: darkgreen"><spring:message code="reservation.card.confirmed"/></h5>
                        </c:when>
                        <c:otherwise>
                            <h5 class="mb-5" style="color: goldenrod"><spring:message code="reservation.card.pending"/></h5>
                        </c:otherwise>
                    </c:choose>

                    <c:choose>
                        <c:when test="${isOwner}">
                            <c:if test="${!reservation.isConfirmed()}">
                                <c:url value="/reservations/${restaurant.getId()}/${reservation.getId()}/confirm" var="confirmationPath"/>
                                <form action="${confirmationPath}" method="post">
                                    <input type="submit" class="btn btn-success text-white" value="<spring:message code="restaurant.manage.confirmReservation"/> ">
                                </form>
                            </c:if>

                            <button type="button" class="btn btn-danger text-white" data-toggle="modal" data-target="#rejectionModal${reservation.getId()}">
                                <spring:message code="myReservations.cancelReservation"/>
                            </button>
                           <%-- <c:url value="/reservations/${restaurant.getId()}/${reservation.getId()}/cancel" var="cancelationPath"/>
                            <form action="${cancelationPath}" method="post">
                                <input type="submit" class="btn btn-danger text-white" value="<spring:message code="myReservations.cancelReservation"/> ">
                            </form>--%>
                        </c:when>
                        <c:otherwise>
                            <%--                            CHANGE RESERVATION QUANTITY--%>
<%--                            <span class="align-text-top">--%>
<%--                                <h5><spring:message code="myReservations.change"/></h5>--%>
<%--                            </span>--%>
<%--                            <form action="${modifyReservationPath}" class="mt-3" method="post">--%>
<%--                                <div class="input-group mb-3 d-inline" >--%>
<%--                                    <div class="input-group-prepend ">--%>
<%--                                        <span class="input-group-text d-inline"><i class="fa fa-users"></i></span>--%>
<%--                                        <input type="number" min="1" max="12" value="${reservation.getQuantity()}" name="quantity">--%>
<%--                                    </div>--%>
<%--                                </div>--%>
<%--                                <input type="submit" class="btn btn-primary text-white mt-3" value="<spring:message code="myReservations.modify"/>">--%>
<%--                            </form>--%>
                        </c:otherwise>
                    </c:choose>
                </span>
        </div>
        <c:choose>
            <c:when test="${!isOwner}">
                <form class="mt-4" action="${cancelReservationPath}" method="post">
                    <input type="submit" class="btn btn-danger text-white" value="<spring:message code="myReservations.cancelReservation"/>" >
                </form>
            </c:when>
            <c:otherwise>
                <h5 class="mt-4"><spring:message code="reservation.card.contactInformation"/></h5>
                <p>${reservation.getUser().getFirstName()} ${reservation.getUser().getLastName()} - ${reservation.getUser().getPhone()}</p>
            </c:otherwise>
        </c:choose>
        <hr style="height:2px;border-width:0;color:gray;background-color:gray">

        <!-- Modal -->
        <div class="modal fade" id="confirmationModalCenter${reservation.getId()}" tabindex="-1" role="dialog" aria-labelledby="confirmationModalCenterTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="confirmationModalLongTitle"><spring:message code="restaurant.manage.confirmation"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>

                    <c:url value="/reservations/${restaurant.getId()}/${reservation.getId()}/cancel" var="cancelationPath"/>
                    <form action="${cancelationPath}" method="post">
                        <div class="modal-body">
                            <p><spring:message code="restaurant.manage.modal.requestMessage"/></p>
                            <div class="input-group">
                                <textarea class="form-control" aria-label="With textarea" name="cancellationMessage" id="cancellationMessage"></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <input type="submit" class="btn btn-danger text-white" value="<spring:message code="restaurant.manage.confirmCancellation"/> ">
                            <button type="button" class="btn btn-secondary text-white" data-dismiss="modal"><spring:message code="restaurant.manage.modal.goBack"/></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <c:if test="${!isOwner}">
        <form class="mt-4" action="${cancelReservationPath}" method="post">
            <input type="submit" class="btn btn-danger text-white" value="<spring:message code="myReservations.cancelReservation"/>" >
        </form>
    </c:if>
    <hr style="height:2px;border-width:0;color:gray;background-color:gray">
</div>


<!-- Modal -->
<div class="modal fade" id="rejectionModal${reservation.getId()}" tabindex="-1" role="dialog" aria-labelledby="rejectionModal" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="rejection"><spring:message code="restaurant.manage.confirmation"/></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <c:url value="/reservations/${restaurant.getId()}/${reservation.getId()}/cancel" var="cancelationPath"/>
            <form action="${cancelationPath}" method="post">
                <div class="modal-body">
                    <p><spring:message code="restaurant.manage.modal.requestMessage"/></p>
                    <div class="input-group">
                        <textarea class="form-control" aria-label="With textarea" name="cancellationMessage" id="rejectionMessage"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <input type="submit" class="btn btn-danger text-white" value="<spring:message code="restaurant.manage.confirmCancellation"/> ">
                    <button type="button" class="btn btn-secondary text-white" data-dismiss="modal"><spring:message code="restaurant.manage.modal.goBack"/></button>
                </div>
            </form>
        </div>
    </div>
</div>



