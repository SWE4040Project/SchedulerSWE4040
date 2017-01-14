$(document).ready(function() {
	//set previous screen
    if( !(Cookies.get(SCHEDULER_APP.previousScreenCookieName) == "clock_in" || Cookies.get(SCHEDULER_APP.previousScreenCookieName) == "on_break") ){
    	//redirect to index
    	window.location.replace(SCHEDULER_APP.base_url + '/index.jsp');
    }
    
	$("#button_breakin").click(function(){
    	breakin();
    });  
	$("#button_clockout").click(function(){
    	clockout();
    });  
});

function breakin(){
	var url = SCHEDULER_APP.url_clockin + '/breakin';
	//get params from page
	var params = {
			employeeId: 1,
			shiftId: 1,
			locationId: 1
		}
	//deactivate button
	//$('#button_breakin').prop('onclick',null).off('click');
	
	$.ajax({
	    url: url,
	    type: 'POST',
	    data: JSON.stringify(params),
	    contentType: 'application/json',
	    dataType: 'json',
	    async: true,
	    success: function(data) {
	        console.log("Data: " + data.Status);
	        
	        //set previous screen
	        Cookies.set(SCHEDULER_APP.previousScreenCookieName,"in_shift");
	        
	        //clocked in page - replaces current page in back stack
	        window.location.replace(SCHEDULER_APP.base_url + '/index.jsp');
	    },
		error: function(err) {
	        alert("Error: " + err.responseText);
	    }
	});
}

function clockout(){
	var url = SCHEDULER_APP.url_clockin + '/clockout';
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
	        console.log("Data: " + data.Status);
	        
	        //clocked in page - replaces current page in back stack
	        window.location.replace(SCHEDULER_APP.base_url + '/login.jsp');
	    },
		error: function(err) {
	        alert("Error: " + err.responseText);
	    }
	});
}
