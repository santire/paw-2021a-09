<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %> 
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<sc:templateLayout>
  <jsp:body>
    <main>
      <c:url value="/resources/images/resto1.jpg" var="restaurantImageUrl" />
      <sc:restaurantBody 
        imgUrl="${restaurantImageUrl}"
        restaurant="${restaurant}"
        />
    </main>
  </jsp:body>
</sc:templateLayout>
