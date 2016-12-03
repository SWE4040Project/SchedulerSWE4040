$(document).ready(function() {
	//set previous screen
    if(Cookies.get(SCHEDULER_APP.previousScreenCookieName) != "in_shift"){
    	//redirect to index
    	window.location.replace(SCHEDULER_APP.base_url + '/index.jsp');
    }
    
	$("#button_breakout").click(function(){
    	breakout();
    });  
});

function breakout(){
	var url = SCHEDULER_APP.url_clockin + '/breakout';
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
	        
	      //set previous screen
	        Cookies.set(SCHEDULER_APP.previousScreenCookieName,"on_break");
	        
	        //clocked in page - replaces current page in back stack
	        window.location.replace(SCHEDULER_APP.base_url + '/index.jsp');
	    },
		error: function(err) {
	        alert("Error: " + err.responseText);
	    }
	});
}
