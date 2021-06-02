<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<c:url value="/user/edit" var="postFormUrl"/>

<sc:templateLayout>
  <jsp:body>

  <spring:message code="user.edit.errorMessage" var="errorMessage"/>
  <c:if test="${somethingWrong}">
    <div class="alert alert-danger alert-dissapear" role="alert">
      <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      <strong><c:out value="${errorMessage}"/></strong> 
    </div>
  </c:if>

    <div class="card mx-auto w-50 mt-4 mb-4">
      <div class="card body my-auto">
          <h2 class="text-center mt-4 card-title"><spring:message code="user.edit.title" /></h2>
        <form:form
           modelAttribute="updateUserForm"
           action="${postFormUrl}"
           method="post"
        >
           <div class="row row-cols-1 row-cols-lg-2 justify-content-center">
               <div>
                   <form:label class="px-3 mx-auto w-100" path="username">
                       <spring:message code="Username" />:
                       <form:input
                               class="form-control mx-auto w-100 px-1 mx-auto w-100 input-group-text text-left"
                               type="text"
                               value="${loggedUser.getName()}"
                               required="true"
                               path="username"/>
                   </form:label>
                   <form:errors path="username" class="px-3 text-danger" element="p"/>

                   <form:label class="px-3 mx-auto w-100" path="firstName">
                       <spring:message code="FirstName" />:
                       <form:input
                               class="px-1 mx-auto w-100 input-group-text text-left"
                               value="${loggedUser.getFirstName()}"
                               type="text"
                               required="true"
                               path="firstName"/>
                   </form:label>
                   <form:errors path="firstName" class="px-3 text-danger" element="p"/>


                   <form:label class="px-3 mx-auto w-100" path="lastName">
                       <spring:message code="FamilyName" />:
                       <form:input
                               class="px-1 mx-auto w-100 input-group-text text-left"
                               type="text"
                               value="${loggedUser.getLastName()}"
                               required="true"
                               path="lastName"/>
                   </form:label>
                   <form:errors path="lastName" class="px-3 text-danger" element="p"/>

                   <form:label class="px-3 mx-auto w-100" path="phone">
                       <spring:message code="PhoneNumber" />:
                       <form:input
                               class="px-1 mx-auto w-100 input-group-text text-left"
                               type="text"
                               value="${loggedUser.getPhone()}"
                               required="true"
                               path="phone"/>
                   </form:label>
                   <form:errors path="phone" class="px-3 text-danger" element="p"/>
               </div>
            </div>
            <div>
                <input type="submit"
                       class="btn btn-outline-secondary btn-block w-50 mt-3 px-0 mx-auto mb-4 mt-4"
                       value='<spring:message code="user.edit.editButton" />'
                />
            </div>

        </form:form>
      </div>
    </div>

  </jsp:body>
</sc:templateLayout>
