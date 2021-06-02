$(function () {
  $("my-select").selectpicker({
    countSelectedText: function (i, n) {
      return "" + i + " tags";
    },
  });



});

document.getElementById("maxprice").addEventListener('change', prices);
document.getElementById("minprice").addEventListener('change', prices);

function prices(){
  if(document.getElementById("maxprice").value <  document.getElementById("minprice").value) {
    document.getElementById("maxprice").min = document.getElementById("minprice").value;
  }



}
