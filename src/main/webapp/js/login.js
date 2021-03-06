$(document).ready(function() {
	
	globalweb_logout();
	console.log("Logged out. Provide username and password to login.");
	
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

        // Save data to sessionStorage
        sessionStorage.setItem(SCHEDULER_APP.authorization, "Bearer " + data.Authorization);
        sessionStorage.setItem(SCHEDULER_APP.xsrfTokenName, data.xsrfToken);

    	//clocked in page - replaces current page in back stack
    	window.location.replace(SCHEDULER_APP.base_url + '/index.jsp');
    };
	var err = function(err) {
		$('#errorMessage').show();
    };
    
    req.then( success, err );
}
