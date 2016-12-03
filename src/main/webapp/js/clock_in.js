$(document).ready(function() {
	console.log("Loading js page.");
	//set previous screen
    if(Cookies.get(SCHEDULER_APP.previousScreenCookieName) != "login"){
    	//redirect to index
    	window.location.replace(SCHEDULER_APP.base_url + '/index.jsp');
    }
    
	$("#button_clockin").click(function(){
    	clockin();
    });  
});

function clockin(){
	var url = SCHEDULER_APP.url_clockin + '/clockin';
	//get params from page
	var params = {
			employeeId: 1,
			shiftId: 1,
			locationId: 1
		}
	
	$.ajax({
	    url: url,
	    type: 'POST',
	    data: JSON.stringify(params),
	    contentType: 'application/json',
	    dataType: 'json',
	    async: true,
	    success: function(data) {
	        console.log("Data Status (Clocked in = 1): " + data.Status);
	        
	        //set previous screen
	        Cookies.set(SCHEDULER_APP.previousScreenCookieName,"clock_in");
	        
	        //clocked in page - replaces current page in back stack
	        window.location.replace(SCHEDULER_APP.base_url + '/index.jsp');
	        //window.location.replace(SCHEDULER_APP.base_url + '/in_shift.jsp');
	    },
		error: function(err) {
	        alert("Error: " + err.responseText);
	    }
	});
}
