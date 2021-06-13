<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>


<c:url value="/restaurant/${restaurant.getId()}/edit" var="postFormUrl"/>
<sc:templateLayout >
  <jsp:body>
        <div class="card mx-auto w-50 mt-4 mb-4">
            <h2 class="text-center mt-4"><spring:message code="hello.restaurant.edit.title" /></h2>
            <div class="card body border-0">
                <form:form
                        modelAttribute="RestaurantForm"
                        action="${postFormUrl}"
                        method="post"
                        enctype="multipart/form-data"
                >
                    <div class="row row-cols-1 row-cols-lg-2 justify-content-center">
                        <div>
                            <form:label class="px-3 mx-auto w-100" path="name">
                                <spring:message code="register.restaurant.RestaurantName" />:
                                <form:input
                                        class=" form-control mx-auto w-100 px-1 mx-auto w-100 input-group-text text-left"
                                        value="${restaurant.getName()}"
                                        type="text"
                                        path="name"/>
                            </form:label>
                            <form:errors path="name" class="px-3 text-danger" element="p"/>

                            <form:label class="px-3 mx-auto w-100" path="address">
                                <spring:message code="Address" />:
                                <form:input
                                        class="px-1 mx-auto w-100 input-group-text text-left"
                                        value="${restaurant.getAddress()}"
                                        type="text"
                                        path="address"/>
                            </form:label>
                            <form:errors path="address" class="px-3 text-danger" element="p"/>

                            <form:label class="px-3 mx-auto w-100" path="phoneNumber">
                                <spring:message code="PhoneNumber" />:
                                <form:input
                                        class="px-1 mx-auto w-100 input-group-text text-left"
                                        value="${restaurant.getPhoneNumber()}"
                                        type="text"
                                        path="phoneNumber"/>
                            </form:label>
                            <form:errors path="phoneNumber" class="px-3 text-danger" element="p"/>

                            <hr style="height:2px;border-width:0;color:gray;background-color:gray">

                            <i class="px-3 mx-auto w-100"><spring:message code="restaurant.socialmedia" /></i>

                            <form:label class="px-3 mx-auto w-100" path="facebook">
                                <spring:message code="restaurant.socialmedia.facebook" />:
                                <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="text" placeholder="www.facebook.com/myprofile" path="facebook"  value="${restaurant.facebook}"/>
                            </form:label>
                            <form:errors path="facebook" class="px-3 text-danger" element="p"/>

                            <form:label class="px-3 mx-auto w-100" path="instagram">
                                <spring:message code="restaurant.socialmedia.instagram" />:
                                <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="text" placeholder="www.instagram.com/myprofile"  path="instagram" value="${restaurant.instagram}"/>
                            </form:label>
                            <form:errors path="instagram" class="px-3 text-danger" element="p"/>

                            <form:label class="px-3 mx-auto w-100" path="twitter">
                                <spring:message code="restaurant.socialmedia.twitter" />:
                                <form:input class="px-1 mx-auto w-100 input-group-text text-left" type="text" placeholder="www.twitter.com/myprofile"  path="twitter" value="${restaurant.twitter}"/>
                            </form:label>
                            <form:errors path="twitter" class="px-3 text-danger" element="p"/>

                            <hr style="height:2px;border-width:0;color:gray;background-color:gray">

                            <div class="container">
                                <spring:message code="restaurant.edit.tags" />
                                <form:errors path="tags" class="px-3 text-danger" element="p"/>
                                <div class="row">
                                    <c:forEach var="id" items="${tags.keySet()}" >
                                        <c:choose>
                                            <c:when test="${ tagsChecked.contains(id) == true}">
                                                <div class="col-md-4">
                                                    <div class="form-check">
                                                        <form:checkbox path="tags" class="form-check-input" name="tags" value="${id}" id="flexCheckChecked" checked="checked"/>
                                                        <label class="form-check-label" for="flexCheckChecked">
                                                            <spring:message code="restaurant.tag.${id}"/>
                                                        </label>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="col-md-4">
                                                    <div class="form-check">
                                                        <form:checkbox path="tags" class="form-check-input" name="tags" value="${id}" id="flexCheckDefault"/>
                                                        <label class="form-check-label" for="flexCheckDefault">
                                                            <spring:message code="restaurant.tag.${id}"/>
                                                        </label>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </div>
                            </div>

                            <hr style="height:2px;border-width:0;color:gray;background-color:gray">

                            <form:label class="px-3 mx-auto w-100 mt-3" path="profileImage">
                                <b><spring:message code="restaurant.edit.profileImage"/></b>
                            </form:label>
                            <div class="input-group">
                                <div class="col-md-6">
                                    <form:input class="px-3 mx-auto w-100" type="file" path="profileImage" id="image"/>
                                </div>
                                <div class="col-md-6">
                                    <form:button  id="reset" type="button" class="align-self-center">
                                        <spring:message code="general.cancel"/>
                                    </form:button>
                                </div>
                            </div>
                            <form:errors path="profileImage" class="px-3 text-danger" element="p"/>

                        </div>
                    </div>
                    <div>
                        <input type="submit" class="btn btn-outline-secondary btn-block w-50 mt-3 px-0 mx-auto" value='<spring:message code="restaurant.editButton" />'>
                    </div>
                </form:form>

                <c:url value="/restaurant/${restaurant.getId()}/delete" var="deletePath"/>
                <div class="row row-cols-1 row-cols-lg-2 justify-content-center px-3">
                    <button type="button" class="btn btn-danger w-50 mt-3 px-5 mx-auto text-white" data-toggle="modal" data-target="#confirmationModalCenter">
                        <spring:message code="restaurant.edit.delete" />
                    </button>
                </div>
                <div class="row row-cols-1 row-cols-lg-2 justify-content-center">
                    <p class="text-danger"><spring:message code="restaurant.edit.deleteWarning" /></p>
                </div>

            </div>

            <!-- Modal -->
            <div class="modal fade" id="confirmationModalCenter" tabindex="-1" role="dialog" aria-labelledby="confirmationModalCenterTitle" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="confirmationModalLongTitle"><spring:message code="restaurant.edit.ModalTitle" /></h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <spring:message code="restaurant.edit.confirmationModalMesagge" />
                        </div>
                        <form action="${deletePath}" method="post">
                            <div class="modal-footer">
                                <input type="submit" class="btn btn-danger text-white" value=<spring:message code="restaurant.edit.deleteConfirmation" />>
                                <button type="button" class="btn btn-secondary text-white" data-dismiss="modal"><spring:message code="restaurant.edit.cancel" /></button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

        </div>


  </jsp:body>
</sc:templateLayout>
