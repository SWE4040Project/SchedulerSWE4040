$(document).ready(function() {

    if(sessionStorage.getItem(SCHEDULER_APP.authorization) == null){
        window.location.replace(SCHEDULER_APP.base_url + '/login.jsp');
    }

    document.getElementById("button_breakin").disabled = true;
    document.getElementById("button_clockout").disabled = true;
    getCurrentShift();

	$("#button_breakin").click(function(){
    	breakin();
    });  
	$("#button_clockout").click(function(){
    	clockout();
    });

    $('[data-toggle="tooltip"]').tooltip();
});

function getCurrentShift(){
    var url = SCHEDULER_APP.base_url + '/rest/shifts/current';
    $.ajax({
        url: url,
        type: 'GET',
        headers: {
            'Authorization':sessionStorage.getItem(SCHEDULER_APP.authorization),
            'xsrfToken': sessionStorage.getItem(SCHEDULER_APP.xsrfTokenName)
        },
        contentType: 'application/json',
        dataType: 'json',
        async: true,
        success: function(data) {
            console.log("scheduled_day: " + data.scheduled_day);

			/*{
			 "scheduled_day": "Tuesday, Mar 07",
			 "scheduled_end": "10:14 PM",
			 "scheduled_start": "3:12 PM",
			 "actual_start": "--",
			 "actual_end": "--",
			 "current_time": "3:25 PM",
			 "progress": 80
			 }*/

            //setup screen
            document.getElementById("start_time").innerHTML = "Clocked in at " + data.actual_start;
            document.getElementById("end_time").innerHTML = "Clocked out at " + data.actual_end;
            document.getElementById("scheduled_shift").innerHTML = data.scheduled_start +" - "+ data.scheduled_end;
            document.getElementById("scheduled_date").innerHTML = data.scheduled_day;
            document.getElementById("current_time").innerHTML = data.current_time;
            $('.progress-bar').css('width', data.progress+'%');

            $('#shift_id').show();
            document.getElementById("button_breakin").disabled = false;
            document.getElementById("button_clockout").disabled = false;
        },
        error: function(err) {
            $('#noShiftMessage').show();
        }
    });
}

function breakin(){
	var url = SCHEDULER_APP.url_clockin + '/breakin';

	$.ajax({
	    url: url,
	    type: 'POST',
        headers: {
            'Authorization':sessionStorage.getItem(SCHEDULER_APP.authorization),
            'xsrfToken': sessionStorage.getItem(SCHEDULER_APP.xsrfTokenName)
        },
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

function clockout(){
	var url = SCHEDULER_APP.url_clockin + '/clockout';

	$.ajax({
	    url: url,
	    type: 'POST',
        headers: {
            'Authorization':sessionStorage.getItem(SCHEDULER_APP.authorization),
            'xsrfToken': sessionStorage.getItem(SCHEDULER_APP.xsrfTokenName)
        },
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
