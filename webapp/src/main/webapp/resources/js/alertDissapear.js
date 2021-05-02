window.setTimeout(function () {
  $(".alert-dissapear")
    .fadeTo(500, 0)
    .slideUp(500, function () {
      $(this).remove();
    });
}, 5000);
