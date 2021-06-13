$(function () {
  $("#rateYo").rateYo({
    starWidth: "30px",
    halfStar: true,
    rating: document.getElementById("rating").value,
  });

  $("#rateYo")
    .rateYo()
    .on("rateyo.set", function (e, data) {
      document.getElementById("rating").value = data.rating;
      document.getElementById("ratingForm").submit();
    });

});


function openFacebook(){
    url = document.getElementById("linkFacebook").getAttribute("value")
    url = url.match(/^https?:/) ? url : '//' + url;
    window.open(url, '_blank');
}
function openInstagram(){
    url = document.getElementById("linkInstagram").getAttribute("value")
    url = url.match(/^https?:/) ? url : '//' + url;
    window.open(url, '_blank');
}
function openTwitter(){
    url = document.getElementById("linkTwitter").getAttribute("value")
    url = url.match(/^https?:/) ? url : '//' + url;
    window.open(url, '_blank');
}

