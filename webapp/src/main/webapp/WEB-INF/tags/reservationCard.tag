<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@attribute name="reservation" required="true" type="ar.edu.itba.paw.model.Reservation"%>

<div class="row">
    <div class="card">
        <div class="row">
            <div class="col border d-flex align-items-center justify-content-center">
                    <c:choose>
                        <c:when test="${reservation.getRestaurant().getMaybeProfileImage().isPresent()}">
                            <c:url value="data:image/jpg;base64,${reservation.getRestaurant().getMaybeProfileImage().get().getImageEnconded()}" var="imgUrl"/>
                        </c:when>
                        <c:otherwise>
                            <c:url value="/resources/images/noimage.jpg" var="imgUrl"/>
                        </c:otherwise>
                    </c:choose>
                    <img
                            src="${imgUrl}"
                            class="restaurant-img card-img img-fluid img-thumbnail rounded card-img-top"
                            alt="..."
                    />
            </div>
            <div class="col border d-flex align-items-center justify-content-center">
                <div class="p-2 bd-highlight">${reservation.getRestaurant().getName()}</div>
                <div class="p-2 bd-highlight">${reservation.getRestaurant().getAddress()}</div>
                <div class="p-2 bd-highlight">${reservation.getRestaurant().getPhoneNumber()}</div>
            </div>
            <div class="col border d-flex align-items-center justify-content-center">
                <div class="p-2 bd-highlight">${reservation.getQuantity()} people</div>
                <div class="p-2 bd-highlight">${reservation.getDate()}</div>
            </div>
        </div>
    </div>
    <c:url value="/reservations/${reservation.getId()}/modify" var="modifyReservationPath"/>
    <c:url value="/reservations/${reservation.getId()}/cancel" var="cancelReservationPath"/>
    <form action="${cancelReservationPath}" method="post">
        <input type="submit" class="btn btn-danger text-white" value=<spring:message code="myReservations.cancelReservation"/> >
    </form>
</div>

