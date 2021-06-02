<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@attribute name="reservation" required="true" type="ar.edu.itba.paw.model.Reservation"%>

<div class="card border-0 mx-auto" style="width: 100%">
    <div class="row">
        <div class="col-md-4 border align-items-center justify-content-center border-0" style="width: 100%">

            <%--IMAGE COLUMN --%>
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
                        class="restaurant-img img-fluid img-thumbnail"
                        alt="..."
                />
            </a>
        </div>

        <%-- RESTAURANT INFORMATION --%>
        <div class="col-md-4 border align-items-center justify-content-center border-0">
            <span class="block">
                <div class="p-2 bd-highlight text-muted">${reservation.getRestaurant().getName()}</div>
                <div class="p-2 bd-highlight text-muted">${reservation.getRestaurant().getAddress()}</div>
                <div class="p-2 bd-highlight text-muted">${reservation.getRestaurant().getPhoneNumber()}</div>
                <div class="p-2 bd-highlight"><spring:message code="reservation.card.reservationFor"/> ${reservation.getQuantity()}
                    <spring:message code="reservation.card.people"/></div>
                <div class="p-2 bd-highlight"><spring:message code="reservation.card.date"/>: ${reservation.getDate().toLocalDate()}</div>
                <div class="p-2 bd-highlight"><spring:message code="reservation.card.time"/>: ${reservation.getDate().toLocalTime()}</div>
            </span>
        </div>

        <%-- RESERVATION COLUMN --%>
        <c:url value="/reservations/${reservation.getId()}/cancel" var="cancelReservationPath"/>
        <div class="col-md-4 border align-items-center justify-content-center border-0">
            <span class="block">
                <c:choose>
                    <c:when test="${reservation.isConfirmed()}">
                        <h5 class="mb-5 mx-auto" style="color: darkgreen">
                            <spring:message code="reservation.card.confirmed"/>
                            <i class="fa fa-question-circle" style="color: #282d32; margin-left: 1em" data-toggle="tooltip" data-placement="top" title="<spring:message code="reservation.card.confirmed.tooltip"/>" ></i>
                        </h5>
                    </c:when>
                    <c:otherwise>
                        <h5 class="mb-5" style="color: goldenrod">
                           <%-- <i class="bi bi-question-circle" data-toggle="tooltip" data-placement="top" title="Tooltip on top"></i>--%>
                            <spring:message code="reservation.card.pending"/>
                        <i class="fa fa-question-circle" style="color: #282d32; margin-left: 1em" data-toggle="tooltip" data-placement="top" title="<spring:message code="reservation.card.pending.tooltip"/>" ></i>
                        </h5>
                    </c:otherwise>
                </c:choose>
              <%--   <form class="mt-4" action="${cancelReservationPath}" method="post">
                    <input type="submit" class="btn btn-danger text-white" value="<spring:message code="myReservations.cancelReservation"/>" >
                </form>--%>
                <button type="button" class="btn btn-danger text-white" data-toggle="modal" data-target="#cancellationModal${reservation.getId()}">
                    <spring:message code="myReservations.cancelReservation"/>
                </button>
            </span>
        </div>
    </div>
    <hr style="height:2px;border-width:0;color:gray;background-color:gray">
</div>

<!-- Modal -->
<div class="modal fade" id="cancellationModal${reservation.getId()}" tabindex="-1" role="dialog" aria-labelledby="cancellationModal" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="cancellation"><spring:message code="restaurant.manage.confirmation"/></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <c:url value="/reservations/${reservation.getId()}/cancel" var="cancelReservationPath"/>
            <form action="${cancelReservationPath}" method="post">
                <div class="modal-body">
                    <p><spring:message code="reservation.modal.confirmCancellation"/></p>
                </div>
                <div class="modal-footer">
                    <input type="submit" class="btn btn-danger text-white" value="<spring:message code="restaurant.manage.confirmCancellation"/> ">
                    <button type="button" class="btn btn-secondary text-white" data-dismiss="modal"><spring:message code="restaurant.manage.modal.goBack"/></button>
                </div>
            </form>
        </div>
    </div>
</div>
