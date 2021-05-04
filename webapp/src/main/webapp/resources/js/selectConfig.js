$(function () {
  $("select").selectpicker({
    countSelectedText: function (i, n) {
      return "" + i + " tags";
    },
  });
});
