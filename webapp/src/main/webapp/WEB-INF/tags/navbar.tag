<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@attribute name="user" required="true" type="ar.edu.itba.paw.model.User"%>

<header>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom">
    <a class="navbar-brand ml-2 font-weight-bold" href="/"
      ><span id="burgundy">Gourme</span><span id="orange">table</span></a
    >
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
            Categorias
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <a class="dropdown-item" href="#">Italiana</a>
            <a class="dropdown-item" href="#">Parrilla</a>
            <a class="dropdown-item" href="#">Asiatica</a>
          </div>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="/restaurants">Ver todos</a>
        </li>
      </ul>

      <form class="nav-item form-inline bg-light rounded my-2 my-lg-0">
        <span class="input-group">
          <input
            class="search-bar form-control mr-sm-2 input-lg"
            type="search"
            placeholder="Search"
            aria-label="Search"
          />
            <button class="btn my-2 my-sm-0" type="submit">
            <span class="fa fa-search text-muted"></span>
          </button>
        </span>
      </form>
      <ul class="navbar-nav ml-auto">
        <c:choose>
            <c:when test="${empty user}">
                <ul class="navbar-nav mr-auto mt-2 mt-lg-0 pl-3">
                  <li class="nav-item">
                    <a class="nav-link" href="/register">Registrarse</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="/login">Log in</a>
                  </li>
                </ul>
            </c:when>
            <c:otherwise>
                <ul class="navbar-nav mr-auto mt-2 mt-lg-0 pl-3">
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
                      <c:out value="${user.getName()}"/>
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                      <a class="dropdown-item" href="/">Log Out</a>
                    </div>
                </ul>
            </c:otherwise>
        </c:choose>
      </ul>
    </div>
  </nav>
</header>
