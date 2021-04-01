<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<sc:templateLayout>
  <jsp:body>
    <main>
      <section>
        <div class="container my-2">
          <h2 class="display-5">Restaurantes destacados</h2>
          <div class="owl-carousel owl-theme">
            <sc:restaurantCard restaurantId="1" name="Kith" imgUrl="/resources/images/resto1.jpg" description="Description"/>
            <sc:restaurantCard restaurantId="1" name="Kith" imgUrl="/resources/images/resto1.jpg" description="Description"/>
            <sc:restaurantCard restaurantId="1" name="Kith" imgUrl="/resources/images/resto1.jpg" description="Description"/>
            <sc:restaurantCard restaurantId="1" name="Kith" imgUrl="/resources/images/resto1.jpg" description="Description"/>
            <sc:restaurantCard restaurantId="1" name="Kith" imgUrl="/resources/images/resto1.jpg" description="Description"/>
            <sc:restaurantCard restaurantId="1" name="Kith" imgUrl="/resources/images/resto1.jpg" description="Description"/>
            <sc:restaurantCard restaurantId="1" name="Kith" imgUrl="/resources/images/resto1.jpg" description="Description"/>
            <sc:restaurantCard restaurantId="1" name="Kith" imgUrl="/resources/images/resto1.jpg" description="Description"/>
            <sc:restaurantCard restaurantId="1" name="Kith" imgUrl="/resources/images/resto1.jpg" description="Description"/>
            <sc:restaurantCard restaurantId="1" name="Kith" imgUrl="/resources/images/resto1.jpg" description="Description"/>
            <sc:restaurantCard restaurantId="1" name="Kith" imgUrl="/resources/images/resto1.jpg" description="Description"/>
          </div>
        </div>
      </section>
    </main>
  </jsp:body>
</sc:templateLayout>
