var SCHEDULER_APP = {};
SCHEDULER_APP.website = "https://swe4040.herokuapp.com"; //https://localhost:8443
SCHEDULER_APP.base_url = SCHEDULER_APP.website;
SCHEDULER_APP.url_clockin = SCHEDULER_APP.base_url + "/rest/clockin";
SCHEDULER_APP.xsrfTokenCookieName = "xsrfToken";
SCHEDULER_APP.previousScreenCookieName = "previousScreen";
SCHEDULER_APP.currentScreenCookieName = "currentScreen";

function globalweb_logout() {
	//deletes the two cookies required for authentication
	//clear cookies if exist
	if(Cookies.get(SCHEDULER_APP.xsrfTokenCookieName)){
		//IMPORTANT! when deleting a cookie, you must pass the exact same path 
		// and domain attributes that was used to set the cookie
		Cookies.remove(SCHEDULER_APP.xsrfTokenCookieName, { path: '/Scheduler' });
		Cookies.remove(SCHEDULER_APP.xsrfTokenCookieName);
		console.log("Cookie: removing " + SCHEDULER_APP.xsrfTokenCookieName);
	}
	if(Cookies.get(SCHEDULER_APP.previousScreenCookieName)){
		//IMPORTANT! when deleting a cookie, you must pass the exact same path 
		// and domain attributes that was used to set the cookie
		Cookies.remove(SCHEDULER_APP.previousScreenCookieName, { path: '/Scheduler' });
		Cookies.remove(SCHEDULER_APP.previousScreenCookieName);
		console.log("Cookie: removing " + SCHEDULER_APP.previousScreenCookieName);
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
