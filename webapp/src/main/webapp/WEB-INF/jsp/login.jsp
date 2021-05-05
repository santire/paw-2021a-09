<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

        <h2 class="text-center mt-5"><spring:message code="hello.login.signin" /></h2>


            <div class="card border-0 mx-auto w-75 mt-4">
              <div class="card body border-0">
              <div class="row">
              <div class="col-md-3"></div>
              <div class="col-md-6">
                <form
                   action="${postFormUrl}"
                   method="post"
                >
                   <div class="row row-cols-1 row-cols-lg-1 justify-content-center">
                      <div>
                          <label class="px-3 mx-auto w-100" for="email">
                            <spring:message code="Email" />:
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
                      <div class="px-3">
                        <spring:message code="login.forgotPassword"/> <a href="<c:url value="/forgot-password"/>"><spring:message code="register.clickHere"/></a>
                      </div>
                      <div>
                        <input type="submit"
                        class="btn btn-outline-secondary btn-block w-100 mt-3 px-0 mx-auto"
                        value='<spring:message code="hello.register.userForm.submit" />'>
                      </div>
                      <c:if test="${not empty tokenError}">
                        <p class="px-3 text-danger"><spring:message code="errors.tokenError"/></p>
                      </c:if>
                  </div>
                </form>
                <div>
                  <spring:message code="login.needAnAccount"/> <a href="<c:url value="/register"/>"><spring:message code="login.createAnAccount"/></a>
                </div>
              </div>

              </div>
              </div>

            </div>
        </div>

        </div>
    </jsp:body>
</sc:templateLayout>



