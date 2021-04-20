<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@attribute name="hidden" required="true" type="java.lang.Boolean"%>


<header>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom">
    <a class="navbar-brand ml-2 font-weight-bold" href="<c:url value="/"/>">
      <img src="<c:url value="/resources/images/logo.svg"/>" height="40" width="40" class="pb-2 pr-1" \><span id="burgundy">Gourme</span><span id="orange">table</span>
    </a>
    <c:choose>
      <c:when test="${hidden eq true}">
        <div style="visibility: hidden;">
      </c:when>
      <c:otherwise>
        <div>
      </c:otherwise>
    </c:choose>
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

    </div>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item">
          <a class="nav-link" href="<c:url value="/restaurants"/>"><spring:message code="navbar.browse" /></a>
        </li>
        <li class="nav-item dropdown" style="visibility: hidden;">
          <a
            class="nav-link dropdown-toggle"
            href="#"
            id="navbarDropdown"
            role="button"
            data-toggle="dropdown"
            aria-haspopup="true"
            aria-expanded="false"
          >
            Categorias
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <a class="dropdown-item" href="#">Italiana</a>
            <a class="dropdown-item" href="#">Parrilla</a>
            <a class="dropdown-item" href="#">Asiatica</a>
          </div>
        </li>
      </ul>

      <form class="nav-item bg-light rounded mx-auto my-2 my-lg-0" action="<c:url value="/restaurants"/>">
        <span class="input-group">
          <input
            class="search-bar form-control mr-auto input-lg"
            type="search"
            placeholder='<spring:message code="navbar.search" />'
            aria-label="Search"
            name="search"
          />
            <button class="btn my-2 my-sm-0 ml-auto" type="submit">
            <span class="fa fa-search text-muted"></span>
          </button>
        </span>
      </form>
      <div class="navbar-nav ml-auto">
        <c:choose>
            <c:when test="${empty loggedUser}">
                <ul class="navbar-nav mt-2 mt-lg-0 pl-3">
                  <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/register"/>"><spring:message code="navbar.register" /></a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/login"/>"><spring:message code="navbar.login" /></a>
                  </li>
                </ul>
            </c:when>
            <c:otherwise>
                <ul class="navbar-nav mt-2 mt-lg-0 pl-3">
                  <li class="nav-item">
                    <span class="navbar-text"></span>
                  </li>
                  <li class="nav-item dropdown">
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
                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                      <a class="dropdown-item" href="<c:url value="/logout"/>"><spring:message code="navbar.logout" /></a>
                    </div>
                </ul>
            </c:otherwise>
        </c:choose>
      </div>
    </div>
  </nav>
</header>
