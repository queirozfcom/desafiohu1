$(document).ready(function(){

    $('[data-toggle="tooltip"]').tooltip();

    enableTypeahead();

    enableListeners();

    setupSearchButtonFunctionality();

});

function updateButtonState(){

    var cityHotelEmpty = $("#search-input").val().length === 0;
    var dateFromEmpty = $("#start-date").val().length === 0;
    var dateToEmpty = $("#end-date").val().length === 0;

    var dateOmmited = $("#date-ommited").is(':checked');

    var btn = $("#search");

    if(!cityHotelEmpty && ( !(dateFromEmpty || dateToEmpty) || dateOmmited )){
        btn.prop("disabled",false);
    }else{
        btn.prop("disabled",true);
    }

}

function updateDateInputState(){
    var dateOmmited = $("#date-ommited").is(':checked');

    var dateFrom = $("#start-date");
    var dateTo = $("#end-date");

    if(dateOmmited){
        dateFrom.prop("disabled",true);
        dateTo.prop("disabled",true);
    }else{
        dateFrom.prop("disabled",false);
        dateTo.prop("disabled",false);
    }
}

function enableTypeahead(){
    var hotels = new Bloodhound({
      datumTokenizer: Bloodhound.tokenizers.whitespace,
      queryTokenizer: Bloodhound.tokenizers.whitespace,
      remote: {
        url: '/hotels/list?searchString=%QUERY',
        wildcard: '%QUERY',
        transform: function(response){
            response.map(function(elem){
                elem.composite = elem.name + " - " + elem.city;
                return elem;
            });
            return response;
        }
      }
    });

    $('#search-input').typeahead({
      hint: true,
      highlight: true,
      minLength:1
    },
    {
      name: 'hotels',
      display: 'composite',
      highlight: true,
      source: hotels,
      limit: 50,
      templates: {
        pending: "<p style='padding-left:10px;'>Carregando...</p>",
        notFound: "<p style='padding-left:10px;'>Nenhum hotel ou cidade corresponde à sua pesquisa.</p>"
      }
    });
}

function enableListeners(){

    // save the id of the selected hotel so we can probe the server later
    $("#search-input").on('typeahead:selected',function(e,datum){
        $(this).attr('hotel_id',datum.id);
        updateButtonState()
    });

    // we must 'listen' to changes here to verify whether we must enable
    // or disable 'buscar' button
    $("input[type=checkbox]").change(function(){
        updateButtonState();
        updateDateInputState();
    });
    $("input[type=text]").change(function(){
        updateButtonState();
    });

    // datepickers
    $("#start-date, #end-date").datepicker({
        format: 'dd/mm/yyyy',
        language: 'pt-BR',
        startDate: "01/05/2015",
        endDate: "31/05/2015",
        orientation: 'top',
        todayHighlight: true
    });
}

function setupSearchButtonFunctionality(){
    // search button
    $("#search").click(function(){

        $("#results-grid").hide();
        $("#results-title").hide();

        var selectedHotelId = $("#search-input").attr('hotel_id');

        var searchContents = $("#search-input").val();

        var dateFrom = encodeURIComponent($("#start-date").val());
        var dateTo = encodeURIComponent($("#end-date").val());

        var omitDates = $("#date-ommited").is(':checked');

        var requestUrl;

        if(omitDates){
            requestUrl = "/availabilities/list?hotelId="+selectedHotelId;
        }else{
            requestUrl = "/availabilities/list?hotelId="+selectedHotelId+"&startDate="+dateFrom+"&endDate="+dateTo;
        }



        $.get(requestUrl, function(data){
            var arr = JSON.parse(data);
            populateGrid(arr,searchContents);
        });

    });
}

function populateGrid(arrayOfObjects,title){

    // remove all previous results, if any

    $("#results-grid").empty();

    $("#results-title").html(title);

    var table = $("<table></table>").addClass("table table-condensed table-hover");

    var header = $("<thead>").append(
        $("<tr>")
        .append($("<th style='text-align:center;'>").text("Data"))
        .append($("<th style='text-align:center;'>").text("Disponível?"))
        .append($("<th style='text-align:center;'>").text("Opções"))
     )

    table.append(header);

    arrayOfObjects.forEach(function(obj){

        var row;

        if(obj.avail === true){
            row = $("<tr>")
                .append($("<td style='text-align:center;'>").text(obj.date))
                .append($("<td style='text-align:center;'>").text("Sim").css("background-color","#FF7519"))
                .append($("<td style='text-align:center;'>").html( $("<button>").addClass("btn btn-xs btn-default").html("Reservar")))
        }else{
            row = $("<tr>")
                .append($("<td style='text-align:center;'>").text(obj.date))
                .append($("<td style='text-align:center;'>").text("Não"))
        }
        table.append(row);
    });

    $("#results-grid").append(table);
    $("#results-grid").show();
    $("#results-title").show();

}