<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@attribute name="reservation" required="true" type="ar.edu.itba.paw.model.Reservation"%>

<div class="card border-0 mx-auto w-100">
    <div class="row row-cols-1 row-cols-md-3">

        <%-- IMAGE COLUMN --%>
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
                        class="restaurant-img img-fluid img-thumbnail"
                        alt="..."
                />
            </a>
        </div>

            <%-- CUSTOMER INFORMATION COLUMN --%>
        <div class="col border d-flex align-items-center justify-content-center border-0">
            <span class="block">
                <h5 class="p-2 bd-highlight"><spring:message code="reservation.card.contactInformation"/></h5>
                <p class="p-2">${reservation.getUser().getFirstName()} ${reservation.getUser().getLastName()} - ${reservation.getUser().getPhone()}</p>
                <div class="p-2 bd-highlight text-muted"><spring:message code="reservation.card.reservationFor"/> ${reservation.getQuantity()}
                    <spring:message code="reservation.card.people"/></div>
                <div class="p-2 bd-highlight text-muted"><spring:message code="reservation.card.date"/>: ${reservation.getDate().toLocalDate()}</div>
                <div class="p-2 bd-highlight text-muted"><spring:message code="reservation.card.time"/>: ${reservation.getDate().toLocalTime()}</div>
            </span>
        </div>

            <%-- RESERVATION COLUMN --%>
        <div class="col border d-flex align-items-center justify-content-center border-0">
            <span class="block">
                <c:choose>
                    <c:when test="${reservation.isConfirmed()}">
                        <h5 class="mb-5 mx-auto" style="color: darkgreen"><spring:message code="reservation.card.confirmed"/></h5>
                        <button type="button" class="btn btn-danger text-white" data-toggle="modal" data-target="#cancellationModal${reservation.getId()}">
                            <spring:message code="myReservations.cancelReservation"/>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <h5 class="mb-5" style="color: goldenrod"><spring:message code="reservation.card.pending"/></h5>
                        <c:url value="/reservations/${restaurant.getId()}/${reservation.getId()}/confirm" var="confirmationPath"/>
                        <form action="${confirmationPath}" method="post">
                            <input type="submit" class="btn btn-success text-white" value="<spring:message code="restaurant.manage.confirmReservation"/> ">
                        </form>
                        <c:url value="/reservations/${restaurant.getId()}/${reservation.getId()}/reject" var="rejectReservationPath"/>
                        <form class="mt-4" action="${rejectReservationPath}" method="post">
                            <input type="submit" class="btn btn-danger text-white" value="<spring:message code="restaurant.manage.rejectReservation"/>">
                        </form>
                    </c:otherwise>
                </c:choose>
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

            <c:url value="/reservations/${restaurant.getId()}/${reservation.getId()}/cancel" var="cancelationPath"/>
            <form action="${cancelationPath}" method="post">
                <div class="modal-body">
                    <p><spring:message code="restaurant.manage.modal.requestMessage"/>:</p>
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
