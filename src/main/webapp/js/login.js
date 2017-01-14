$(document).ready(function() {
	
	//clear cookies if exist
	if(Cookies.get(SCHEDULER_APP.xsrfTokenCookieName)){
		//IMPORTANT! when deleting a cookie, you must pass the exact same path 
		// and domain attributes that was used to set the cookie
		Cookies.remove(SCHEDULER_APP.xsrfTokenCookieName, { path: '/Scheduler' });
		console.log("Cookie: removing " + SCHEDULER_APP.xsrfTokenCookieName);
	}
	if(Cookies.get(SCHEDULER_APP.previousScreenCookieName)){
		//IMPORTANT! when deleting a cookie, you must pass the exact same path 
		// and domain attributes that was used to set the cookie
		Cookies.remove(SCHEDULER_APP.previousScreenCookieName);
		console.log("Cookie: removing " + SCHEDULER_APP.previousScreenCookieName);
	}
	
	var test = Cookies.get(SCHEDULER_APP.xsrfTokenCookieName)
	console.log("Cookie: xsrfTokenCookieName exists? " + test);
	var test2 = Cookies.get(SCHEDULER_APP.previousScreenCookieName)
	console.log("Cookie: previousScreenCookieName exists? " + test2);
	
    // process the form
    $('form').submit(function(event) {
    	 // stop the form from submitting the normal way and refreshing the page
        event.preventDefault();
        
    	login();
    });

});

function login(){
	
	var url = SCHEDULER_APP.base_url + '/rest/login';
	
	// get the form data
    // there are many ways to get this data using jQuery (you can use the class or id also)
    var params = {
		'username'    : $('input[name=username]').val(),
        'password'    : $('input[name=password]').val(),
    };
    
    console.log("params[0]: " + params[0]);

    // process the form
    var req = $.ajax({
        type        : 'POST', // define the type of HTTP verb we want to use (POST for our form)
        url         : url, // the url where we want to POST
        data        : JSON.stringify(params), // our data object
        contentType : 'application/json',
	    dataType    : 'json',
        encode      : true,
        async		: true
    })
    
    var success = function(data) {
        console.log("Data Status (Clocked in = 0): " + data.Status);

        //set previous screen
        Cookies.set(SCHEDULER_APP.previousScreenCookieName,"login");
        
    	//clocked in page - replaces current page in back stack
        window.location.replace(SCHEDULER_APP.base_url + '/index.jsp');
    	//window.location.replace(SCHEDULER_APP.base_url + '/clock_in.jsp');
    };
	var err = function(err) {
        alert("Error: " + err.responseText);
    };
    
    req.then( success, err );
}
