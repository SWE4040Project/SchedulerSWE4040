$(document).ready(function() {
	console.log("Loading js page.");
    $("#button_clockin").click(function(){
    	var url = 'http://localhost:8080/Scheduler/rest/clockin/clockin';
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
    	        alert("Data Status (Clocked in = 1): " + data.Status);
    	    },
    		error: function(err) {
    	        alert("Error: " + err.responseText);
    	    }
    	});
    }); 
});
