<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@attribute name="reservation" required="true" type="ar.edu.itba.paw.model.Reservation"%>

<%--<div class="row">--%>
    <div class="card border-0">
        <div class="row">
            <div class="col border d-flex align-items-center justify-content-center border-0">
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
            <div class="col border d-flex align-items-center justify-content-center border-0">
                <span class="block">
                    <div class="p-2 bd-highlight text-muted">${reservation.getRestaurant().getName()}</div>
                    <div class="p-2 bd-highlight text-muted">${reservation.getRestaurant().getAddress()}</div>
                    <div class="p-2 bd-highlight text-muted">${reservation.getRestaurant().getPhoneNumber()}</div>
                    <div class="p-2 bd-highlight">Reservation for ${reservation.getQuantity()} people</div>
                    <div class="p-2 bd-highlight">Date: ${reservation.getDate()}</div>
                </span>
            </div>
            <div class="col border d-flex align-items-center justify-content-center border-0">
                <span class="block">
                    <span class="align-text-top">
                        <h5>Change your reservation</h5>
                    </span>
                    <c:url value="/reservations/${reservation.getId()}/modify" var="modifyReservationPath"/>
                    <c:url value="/reservations/${reservation.getId()}/cancel" var="cancelReservationPath"/>
                    <form action="${modifyReservationPath}" method="post">
                        <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                <span class="input-group-text"><i class="fa fa-users"></i></span>
                            </div>
                            <input type="number" min="1" max="12" value="${reservation.getQuantity()}" name="quantity">
                        </div>
                        <input type="submit" class="btn btn-primary text-white" value="Modify" >
                    </form>
                </span>
            </div>
        </div>
        <form class="mt-4" action="${cancelReservationPath}" method="post">
            <input type="submit" class="btn btn-danger text-white" value="Cancel Reservation" >
        </form>
        <hr style="height:2px;border-width:0;color:gray;background-color:gray">

    </div>

<%--</div>--%>

