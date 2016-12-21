$(document).ready(function() {
  var menuVisible = true;
  $('#menu-toggle').click(function() {
    if (menuVisible) {
      $('#sideMenu').css({'display':'none'});
      $('#menu-toggle').css({'background-color':'#333377'});
      menuVisible = false;
      return;
    }
    $('#sideMenu').css({'display':'block'});
    $('#menu-toggle').css({'background-color':'#3f51b5'});
    menuVisible = true;
  });
});