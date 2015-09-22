


$(document).ready(function(){

    var hotels = new Bloodhound({
      datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
      queryTokenizer: Bloodhound.tokenizers.whitespace,
      remote: {
        url: '/hotels/list?searchString=%QUERY',
        wildcard: '%QUERY'
      }
    });

    $('#search').typeahead({
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

//    $('#search').focus(function(){
//        $this.typeahead('open');
//    });
//
//    $('#search').blur(function(){
//        $(this).typeahead('close');
//    });

});

if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}