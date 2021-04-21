<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url value="/login" var="postFormUrl"/>

<sc:templateLayout simpleTopBar="true">

    <jsp:body>
        <c:if test="${error}">
            <div class="alert alert-danger" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <strong><spring:message code="hello.login.error"></spring:message></strong>
            </div>
        </c:if>

        <h2 class="text-center mt-4"><spring:message code="hello.login.signin" /></h2>

        <div class="card border-0 mx-auto w-75">
          <div class="card body border-0">
            <form
               action="${postFormUrl}"
               method="post"
            >
               <div class="row row-cols-1 row-cols-lg-1 justify-content-center">
                  <div>
                      <label class="px-3 mx-auto w-100" for="email">
                        <spring:message code="email" />:
                        <input class=" form-control mx-auto w-100 px-1 mx-auto w-100 input-group-text text-left" name="email" type="email" id="email">
                      </label>
                  </div>
                  <div>
                      <label class="px-3 mx-auto w-100" for="password">
                        <spring:message code="Password" />:
                        <input name="password" class="px-1 mx-auto w-100 input-group-text text-left" type="password" id="password">
                      </label>
                  </div>
                  <div class="form-check pl-3">
                        <input class="form-check-input mx-0 pr-2" name="rememberme" type="checkbox" id="gridCheck">
                        <label class="form-check-label mx-0 ml-4" for="gridCheck">
                            <spring:message code="hello.login.rememberme"></spring:message>
                        </label>
                  </div>
                  <div>
                    <input type="submit" class="btn btn-outline-secondary btn-block w-100 mt-3 px-0 mx-auto" value='<spring:message code="hello.login.signin" />'>
                  </div>
              </div>
            </form>
          </div>
        </div>
    </jsp:body>
</sc:templateLayout>



