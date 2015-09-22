$(document).ready(function(){

    $('#search').typeahead({
        minLength: 3.
        highlight:true,
    },
    {
        name: 'hotels-data',
        source
    }
    );

    $('#search').focus(function(){
        $(this).typeahead('open');
    });

    $('#search').blur(function(){
        $(this).typeahead('close');
    });

});

if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}