<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %> 
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<sc:templateLayout>
  <jsp:body>
    <main>
      <sc:restaurantBody 
        imgUrl="/resources/images/resto1.jpg"
        name="Kith"
        address="Calle Falsa 123"
        phoneNumber="11 2345-6789"
        rating="4.6"
        />
    </main>
  </jsp:body>
</sc:templateLayout>
