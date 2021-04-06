<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%> 
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>

<html>
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css"
      integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l"
      crossorigin="anonymous"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
      integrity="sha512-SfTiTlX6kk+qitfevl/7LibUOeJWlt9rbyDn92a1DqWOw9vWG2MFoays0sgObmWazO5BQPiFucnnEAjpAB+/Sw=="
      crossorigin="anonymous"
    />
    <link rel="stylesheet" href="<c:url value="/resources/owlcarousel/owl.carousel.min.css"/>" />
    <link
      rel="stylesheet"
      href="<c:url value="/resources/owlcarousel/owl.theme.default.min.css"/>"
    />
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/styles.css"/>" />

    <title>Gourmetable</title>
  </head>
  <body>
    <sc:navbar user="${user}" />

    <div id="body" class="page-content">
      <jsp:doBody />
    </div>

    <jsp:include page="/WEB-INF/jsp/footer.jsp" />

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.bundle.min.js"></script>
    <script src="<c:url value="/resources/owlcarousel/owl.carousel.min.js"/>"></script>
    <script src="<c:url value="/resources/js/carousel.js"/>">
    </script>
  </body>
</html>
