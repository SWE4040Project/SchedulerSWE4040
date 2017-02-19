var SCHEDULER_APP = {};
SCHEDULER_APP.website = location.protocol + "//" + location.host; //https://swe4040.herokuapp.com, http://localhost:8080
SCHEDULER_APP.base_url = SCHEDULER_APP.website;
SCHEDULER_APP.url_clockin = SCHEDULER_APP.base_url + "/rest/clockin";
SCHEDULER_APP.authorization = "Authorization";
SCHEDULER_APP.xsrfTokenName = "xsrfToken";

function globalweb_logout() {
	//deletes the two cookies required for authentication
	//clear cookies if exist
	sessionStorage.removeItem(SCHEDULER_APP.authorization);
    sessionStorage.removeItem(SCHEDULER_APP.xsrfTokenName);

	if(Cookies.get(SCHEDULER_APP.xsrfTokenName)){
		//IMPORTANT! when deleting a cookie, you must pass the exact same path
		// and domain attributes that was used to set the cookie
		Cookies.remove(SCHEDULER_APP.xsrfTokenName);
		console.log("Cookie: removing " + SCHEDULER_APP.xsrfTokenName);
	}
}

function globalweb_employee_logout(){
	globalweb_logout();
	window.location.replace(SCHEDULER_APP.base_url + '/login.jsp');
}

function globalweb_displayErrorModal(title, message){
	$('#modal_title_id').text(title);
	$('#modal_message_id').text(message);
	$('#errorModal').modal('show');
}
