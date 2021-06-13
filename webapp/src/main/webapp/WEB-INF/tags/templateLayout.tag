<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%> 
<%@ taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@attribute name="simpleTopBar" required="false" type="java.lang.Boolean"%>


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
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.css"
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
    <link rel="icon" href="<c:url value="/favicon.ico"/>" type="image/x-icon">
    <link rel="shortcut icon" href="<c:url value="/favicon.ico"/>" type="image/x-icon">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.7.1/css/bootstrap-datepicker3.standalone.min.css" />

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/rateYo/2.3.2/jquery.rateyo.min.css">


    <title>Gourmetable</title>
  </head>
  <body>
    <sc:navbar hidden="${simpleTopBar}"/>

    <div id="wrap">
      <div id="body" class="page-content">
        <jsp:doBody />
      </div>
    </div>

    <jsp:include page="/WEB-INF/jsp/footer.jsp" />

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.bundle.min.js"></script>
    <script src="<c:url value="/resources/owlcarousel/owl.carousel.min.js"/>"></script>
    <script src="<c:url value="/resources/js/carousel.js"/>"></script>
    <script src="<c:url value="/resources/js/alertDissapear.js"/>"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/js/bootstrap-select.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.7.1/js/bootstrap-datepicker.min.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/rateYo/2.3.2/jquery.rateyo.min.js"></script>

    <script src="https://unpkg.com/gijgo@1.9.13/js/gijgo.min.js" type="text/javascript"></script>
    <link href="https://unpkg.com/gijgo@1.9.13/css/gijgo.min.css" rel="stylesheet" type="text/css" />

    <script src="<c:url value="/resources/js/selectConfig.js"/>"></script>

    <script src="<c:url value="/resources/js/rateYo.js"/>"></script>

    <script type="text/javascript">
      document.getElementById('reset').onclick= function() {
        document.getElementById('image').value = '';
      };
    </script>

    <script>
      $('#datepicker').datepicker({
        uiLibrary: 'bootstrap4',
        format: "dd/mm/yyyy",
        maxDate: function() {
          var date = new Date();
          date.setDate(date.getDate()+7);
          return new Date(date.getFullYear(), date.getMonth(), date.getDate());
        },
        minDate: function() {
          var date = new Date();
          date.setDate(date.getDate());
          return new Date(date.getFullYear(), date.getMonth(), date.getDate());
        },
        showOtherMonths: false
      });
    </script>
    <style>
      span.input-group-append {
        display: none;
      }
    </style>


  </body>
</html>
