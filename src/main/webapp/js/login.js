$(document).ready(function() {
	$("#login_button").click(function(){
    	login();
    });  
});

function login(){
	//clocked in page - replaces current page in back stack
	window.location.replace(SCHEDULER_APP.base_url + '/clock_in.jsp');
}
