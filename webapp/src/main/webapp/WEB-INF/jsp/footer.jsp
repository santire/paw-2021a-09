<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div id="footer" class="footer-dark bg-dark border-top" >
  <footer class="footer">
    <div class="container">
      <div class="row">
        <div class="container">
          <div class="row">
            <div class="item text col-md-6 mr-auto">
              <h3><spring:message code="footer.weAre" /></h3>
              <ul class="list">
                <li><p>Gianfranco Marchetti</p></li>
                <li><p>Manuel Luque</p></li>
                <li><p>Santiago Burgos</p></li>
                <li><p>Santiago Reyes</p></li>
              </ul>
            </div>
            <div class="col-md-6">
              <div class="col-md-6 item text ml-auto">
                <h3><spring:message code="footer.ContactUs" /></h3>
                <p><a href="mailto:gourmetablewebapp@gmail.com" style="color:aliceblue">gourmetablewebapp@gmail.com</a></p>

                <h3 class="mt-4"><spring:message code="footer.language" /></h3>
                <form>
                  <div class="btn-group">
                    <button class="btn btn-secondary text-white" type="submit" name="lang" value="en"> En</button>
                    <button class="btn btn-secondary text-white" type="submit" name="lang" value="es"> Es</button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>

        <%--
        <div class="col item social mx-auto justify-content-center">
          <a href="#"><i class="fa fa-facebook text-muted"></i></a
          ><a href="#"><i class="fa fa-instagram text-muted"></i></a
          ><a href="#"><i class="fa fa-twitter text-muted"></i></a
          ><a href="#"><i class="fa fa-snapchat text-muted"></i></a>
        </div> --%>
      </div>
    </div>
  </footer>
</div>
