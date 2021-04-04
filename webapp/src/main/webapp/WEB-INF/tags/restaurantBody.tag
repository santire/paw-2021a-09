<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@attribute name="imgUrl" required="true" type="java.lang.String"%>
<%@attribute name="restaurant" required="true" type="ar.edu.itba.paw.model.Restaurant"%>

<div class="container">
<div class="mb-3 my-2" style="max-height: 450px;">
  <div class="row no-gutters">
    <div class="col-md-6 mx-auto pb-3">
      <img src="${imgUrl}" class="card-img img-fluid px-1 pr-md-5 border-right" alt="${name}">
    </div>
    <div class="card border-0 col-md-6 my-auto mx-auto" style="max-width: 300px;">
      <div class="card-body px-auto mb-auto">
        <h5 class="card-title">${restaurant.getName()}</h5>
        <p class="card-text"><medium class="text-muted">${restaurant.getAddress()}</medium></p>
        <p class="card-text"><medium class="text-muted">${restaurant.getPhoneNumber()}</medium></p>
        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text"><i class="fa fa-clock-o"></i></span>
          </div>
          <input type="number" min="0" max="23" class="form-control" aria-label="Seleccione horario para su reserva">
          <div class="input-group-append">
            <span class="input-group-text">:00</span>
          </div>
        </div>
        <a 
        class="btn btn-outline-secondary btn-block">
        Reservar Hoy 
        </a>
      </div>
    </div>
  </div>
</div>
</div>
<div style="min-height: 15px; display: block;">
</div>
<div class="container border-top mt-1">
  <h3 class="pt-2">Menu</h3>
  <ul class="list-group list-group-flush">
    <li class="list-group-item">
    <span class="col-md-10 mr-auto pull-left">Cras justo odio</span>
    <span class="col-md-2 pl-5 ml-auto pull-right">$250</span>
    </li>
    <li class="list-group-item">
    <span class="col-md-10 mr-auto pull-left">Pizza con salame</span>
    <span class="col-md-2 pl-5 ml-auto pull-right">$1250</span>
    </li>
    <li class="list-group-item">
    <span class="col-md-10 mr-auto pull-left">Dapibus ac facilisis in</span>
    <span class="col-md-2 pl-5 ml-auto pull-right">$250</span>
    </li>
    <li class="list-group-item">
    <span class="col-md-10 mr-auto pull-left">Morbi leo risus</span>
    <span class="col-md-2 pl-5 ml-auto pull-right">$250</span>
    </li>
    <li class="list-group-item">
    <span class="col-md-10 mr-auto pull-left">Porta ac consectetur ac</span>
    <span class="col-md-2 pl-5 ml-auto pull-right">$250</span>
    </li>
    <li class="list-group-item">
    <span class="col-md-10 mr-auto pull-left">Vestibulum at eros</span>
    <span class="col-md-2 pl-5 ml-auto pull-right">$250</span>
    </li>
  </ul>
</div>
