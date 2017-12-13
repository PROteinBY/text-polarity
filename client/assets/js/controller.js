var URL = "http://localhost:8080/is?text=";

$(document).ready(function(){
    $("#analyse").click(function() {
        var text = $("#input").val();
        var itemClass = "text-item ";

        $.ajax({
            type:"GET",
            url: URL + text,
            contentType: "application/json",
            success: function (data) {
                if (data === true)
                    itemClass += "green";
                else
                    itemClass += "red";

                $("<div class='" + itemClass + "'>"+ text + "</div>").prependTo( "#text-list" );
            }
        });
    });
});

