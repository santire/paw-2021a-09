<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %> 
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<sc:templateLayout>
  <jsp:body>
    <main>
      <sc:restaurantBody 
        imgUrl="/resources/images/resto1.jpg"
        restaurant="${restaurant}"
        />
    </main>
  </jsp:body>
</sc:templateLayout>
