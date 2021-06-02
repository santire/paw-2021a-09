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
               <h5><spring:message code="reservation.history.card.restaurantInformation"/></h5>
                <div class="p-2 bd-highlight text-muted">${reservation.getRestaurant().getName()}</div>
                <div class="p-2 bd-highlight text-muted">${reservation.getRestaurant().getAddress()}</div>
                <div class="p-2 bd-highlight text-muted">${reservation.getRestaurant().getPhoneNumber()}</div>
            </span>
    </div>

    <%-- RESERVATION COLUMN --%>
    <c:url value="/reservations/${reservation.getId()}/cancel" var="cancelReservationPath"/>
    <div class="col-md-4 border align-items-center justify-content-center border-0">
            <span class="block">
              <h5><spring:message code="reservation.history.card.reservationInformation"/></h5>
              <div class="p-2 bd-highlight"><spring:message code="reservation.card.reservationFor"/> ${reservation.getQuantity()}
                    <spring:message code="reservation.card.people"/></div>
                <div class="p-2 bd-highlight"><spring:message code="reservation.card.date"/>: ${reservation.getDate().toLocalDate()}</div>
                <div class="p-2 bd-highlight"><spring:message code="reservation.card.time"/>: ${reservation.getDate().toLocalTime()}</div>
            </span>
    </div>
  </div>
  <hr style="height:2px;border-width:0;color:gray;background-color:gray">
</div>