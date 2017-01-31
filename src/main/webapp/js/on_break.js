$(document).ready(function() {
	$("#button_breakout").click(function(){
    	breakout();
    });  
});

function breakout(){
	var url = SCHEDULER_APP.url_clockin + '/breakout';
	//get params from page
	var params = {
			employeeId: 6,
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
	        window.location.replace(SCHEDULER_APP.base_url + '/index.jsp');
	    },
		error: function(err) {
	        alert("Error: " + err.responseText);
	    }
	});
}
