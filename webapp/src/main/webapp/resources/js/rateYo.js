$(function () {



    $("#rateYo").rateYo({
        starWidth: "30px",
        fullStar: true,
        rating: document.getElementById("rating").value,
    });


    $("#rateYo").rateYo()
        .on("rateyo.set", function (e, data) {

            document.getElementById("rating").value = data.rating;
            document.getElementById("ratingForm").submit();
        });


});