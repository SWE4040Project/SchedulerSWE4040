$(document).ready(function() {
	$("#button_clockin").click(function(){
    	clockin();
    });  
	
	$('[data-toggle="tooltip"]').tooltip(); 
});

function clockin(){
	var url = SCHEDULER_APP.url_clockin + '/clockin';
	//get params from page
	var params = {
			employeeId: 6,
			shiftId: 1,
			locationId: 1
		}

	$.ajax({
	    url: url,
	    type: 'POST',
        headers: {
	    	'Authorization':sessionStorage.getItem(SCHEDULER_APP.authorization),
			'xsrfToken': sessionStorage.getItem(SCHEDULER_APP.xsrfTokenName)
	    },
	    data: JSON.stringify(params),
	    contentType: 'application/json',
	    dataType: 'json',
	    async: true,
	    success: function(data) {
	        console.log("Data Status (Clocked in = 1): " + data.Status);
	        
	        //clocked in page - replaces current page in back stack
	        window.location.replace(SCHEDULER_APP.base_url + '/index.jsp');
	    },
		error: function(err) {
			$('#errorMessage').show();
	    }
	});
}
