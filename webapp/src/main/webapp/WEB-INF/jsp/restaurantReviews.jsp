<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<sc:templateLayout>
    <jsp:body>
        <main>
            <c:url value="/resources/images/resto1.jpg" var="restaurantImageUrl" />
            <c:choose>
                <c:when test="${userMadeComment}">
                    <sc:restaurantBodyReviews
                            restaurant="${restaurant}"
                            times="${times}"
                            reviews="${reviews}"
                            userMadeComment="${userMadeComment}"
                            userReview="${userReview}"
                            hasOnceReserved="${hasOnceReserved}"
                            isOwner="${isTheOwner}"
                    />
                </c:when>
                <c:otherwise>
                    <sc:restaurantBodyReviews
                            restaurant="${restaurant}"
                            times="${times}"
                            reviews="${reviews}"
                            userMadeComment="${userMadeComment}"
                            hasOnceReserved="${hasOnceReserved}"
                    />
                </c:otherwise>
            </c:choose>
        </main>
        </main>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function(){
                $("#dateInput").datepicker({
                    "format": "dd-mm-yyyy",
                });
            });
        </script>
    </jsp:body>
</sc:templateLayout>
