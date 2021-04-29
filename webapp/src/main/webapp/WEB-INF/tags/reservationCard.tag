<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<%@attribute name="reservation" required="true" type="ar.edu.itba.paw.model.Reservation"%>
<%@attribute name="isOwner" required="true" type="java.lang.Boolean"%>
<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
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
                <c:url value="/reservations/${reservation.getId()}/modify" var="modifyReservationPath"/>
                <c:url value="/reservations/${reservation.getId()}/cancel" var="cancelReservationPath"/>
                <span class="block">
                    <c:choose>
                        <c:when test="${isOwner}">
                            <button type="button" class="btn btn-danger text-white" data-toggle="modal" data-target="#confirmationModalCenter">
                                Cancel Reservation
                            </button>
                        </c:when>
                        <c:otherwise>
                            <span class="align-text-top">
                                <h5>Change your reservation</h5>
                            </span>
                            <form action="${modifyReservationPath}" class="mt-3" method="post">
                                <div class="input-group mb-3 d-inline" >
                                    <div class="input-group-prepend ">
                                        <span class="input-group-text d-inline"><i class="fa fa-users"></i></span>
                                        <input type="number" min="1" max="12" value="${reservation.getQuantity()}" name="quantity">
                                    </div>
                                </div>
                                <input type="submit" class="btn btn-primary text-white mt-3" value="Modify" >
                            </form>
                            <div id="datetime"></div>
<%--                            <script type="text/javascript">
                                $("#datetime").datetimepicker();
                            </script>--%>

                        </c:otherwise>
                    </c:choose>
                </span>
            </div>
        </div>
        <c:if test="${!isOwner}">
            <form class="mt-4" action="${cancelReservationPath}" method="post">
                <input type="submit" class="btn btn-danger text-white" value="Cancel Reservation" >
            </form>
        </c:if>
        <hr style="height:2px;border-width:0;color:gray;background-color:gray">
    </div>

<!-- Modal -->
<div class="modal fade" id="confirmationModalCenter" tabindex="-1" role="dialog" aria-labelledby="confirmationModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="confirmationModalLongTitle">Confirmation</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <c:url value="/reservations/${restaurant.getId()}/${reservation.getId()}/cancel" var="cancelationPath"/>
            <form action="${cancelationPath}" method="post">
                <div class="modal-body">
                    <p>Send a cancellation message to customer explaining the reasons for cancellation</p>
                    <div class="input-group">
                        <textarea class="form-control" aria-label="With textarea" name="cancellationMessage" id="cancellationMessage"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <input type="submit" class="btn btn-danger text-white" value="Confirm Cancellation">
                    <button type="button" class="btn btn-secondary text-white" data-dismiss="modal">Go Back</button>
                </div>
            </form>
        </div>
    </div>
</div>

