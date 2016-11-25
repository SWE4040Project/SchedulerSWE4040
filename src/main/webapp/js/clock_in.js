$(document).ready(function() {
	console.log("Loading js page.");
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
	        
	        //clocked in page - replaces current page in back stack
	        window.location.replace(SCHEDULER_APP.base_url + '/in_shift.jsp');
	    },
		error: function(err) {
	        alert("Error: " + err.responseText);
	    }
	});
}
