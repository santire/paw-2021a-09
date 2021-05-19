<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@attribute name="hidden" required="true" type="java.lang.Boolean"%>

<c:set value="" var="hide"/>
<c:if test="${hidden eq true}">
  <c:set value="d-none" var="hide"/>
</c:if>

<header>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom">
    <a class="navbar-brand ml-2 font-weight-bold" href="<c:url value="/"/>">
      <img src="<c:url value="/resources/images/logo.svg"/>" height="40" width="40" class="pb-2 pr-1" \><span id="burgundy">Gourme</span><span id="orange">table</span>
    </a>
      <button
        class="navbar-toggler"
        type="button"
        data-toggle="collapse"
        data-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item ${hide}">
          <a class="nav-link" href="<c:url value="/restaurants"/>"><spring:message code="navbar.browse" /></a>
        </li>
        <c:if test="${!empty loggedUser}">
          <li class="nav-item ${hide}">
            <a class="nav-link" href="<c:url value="/reservations"/>"><spring:message code="navbar.myReservations" /></a>
          </li>
        </c:if>
      </ul>

      <form id="search-bar" class="nav-item ${hide} bg-light rounded mx-auto my-2 my-lg-0" action="<c:url value="/restaurants"/>">
        <span class="input-group">
        <c:choose>
          <c:when test="${userIsSearching}">
            <input
              class="search-bar form-control mr-auto input-lg"
              type="search"
              placeholder='<spring:message code="navbar.search" />'
              aria-label="Search"
              name="search"
              value="${searchString}"
            />
          </c:when>
          <c:otherwise>
            <input
              class="search-bar form-control mr-auto input-lg"
              type="search"
              placeholder='<spring:message code="navbar.search" />'
              aria-label="Search"
              name="search"
            />
          </c:otherwise>
        </c:choose>
            <button class="btn my-2 my-sm-0 ml-auto" type="submit">
            <span class="fa fa-search text-muted"></span>
          </button>
        </span>
      </form>
      <div class="navbar-nav ml-auto">
        <c:choose>
            <c:when test="${empty loggedUser}">
                <ul class="navbar-nav mt-2 mt-lg-0 pl-3">
                  <li class="nav-item ${hide}">
                    <a class="nav-link" href="<c:url value="/register"/>"><spring:message code="navbar.register" /></a>
                  </li>
                  <li class="nav-item ${hide}">
                    <a class="nav-link" href="<c:url value="/login"/>"><spring:message code="navbar.login" /></a>
                  </li>
                </ul>
            </c:when>
            <c:otherwise>
                <ul class="navbar-nav mt-2 mt-lg-0 pl-3">
                  <li class="nav-item ${hide}">
                    <span class="navbar-text"></span>
                  </li>
                  <li class="nav-item ${hide} dropdown">
                    <a
                      class="nav-link dropdown-toggle"
                      href="#"
                      id="navbarDropdown"
                      role="button"
                      data-toggle="dropdown"
                      aria-haspopup="true"
                      aria-expanded="false"
                    >
                      <c:out value="${loggedUser.getName()}"/>
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                      <h6 class="dropdown-header"><spring:message code="navbar.restaurants"/></h6>
                      <a class="dropdown-item" href="<c:url value="/register/restaurant"/>"><spring:message code="navbar.registerRestaurant" /></a>
                      <a class="dropdown-item" href="<c:url value="/restaurants/user/${loggedUser.getId()}"/>"><spring:message code="navbar.myRestaurants" /></a>
                      <div class="dropdown-divider"></div>
                      <h6 class="dropdown-header"><spring:message code="navbar.account"/></h6>
                      <a class="dropdown-item" href="<c:url value="/user/edit"/>"><spring:message code="navbar.updateInfo" /></a>
                      <div class="dropdown-divider"></div>
                      <a class="dropdown-item" href="<c:url value="/logout"/>">
                        <spring:message code="navbar.logout" />
                      </a>
                    </div>
                  </li>
                </ul>

            </c:otherwise>
        </c:choose>

      </div>
    </div>
  </nav>
</header>
