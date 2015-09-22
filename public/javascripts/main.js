


$(document).ready(function(){

    var hotels = new Bloodhound({
      datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
      queryTokenizer: Bloodhound.tokenizers.whitespace,
      remote: {
        url: '/hotels/list?searchString=%QUERY',
        wildcard: '%QUERY'
      }
    });

    $('#search-input').typeahead({
      hint: true,
      highlight: true,
      minLength:1
    },
    {
      name: 'hotels',
      display: 'value',
      highlight: true,
      source: hotels,
      limit: 20
    });


    // we must 'listen' to changes here to verify whether we must enable
    // or disable 'buscar' button
    $("input[type=checkbox]").change(function(){
        updateButtonState();
    });
    $("input[type=text]").keyup(function(){
        updateButtonState();
    });


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

    console.log(dateOmmited);

}