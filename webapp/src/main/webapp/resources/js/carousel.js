$(document).ready(function () {
  $(".owl-carousel").owlCarousel({
    loop: false,
    nav: true,
    margin: 10,
    navText: [
      '<i class="fa fa-angle-left" aria-hidden="true"></i>',
      '<i class="fa fa-angle-right" aria-hidden="true"></i>',
    ],
    responsiveClass: true,
    responsive: {
      0: {
        items: 1,
        nav: false,
      },
      600: {
        items: 3,
      },
      1000: {
        items: 5,
      },
    },
  });
});
