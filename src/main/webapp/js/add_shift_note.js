$(document).ready(function() {
	$("#button_addshiftnotes").click(function(){
    	addshiftnote();
    });  
});

function addshiftnote(){
	
	//sending string
	var shiftNote = "";
	//get from page
	shiftNote = $("textarea#shift-notes").val();
	console.log("Shift note: " + shiftNote);
	
	var url = SCHEDULER_APP.url_clockin + '/addshiftnote';
	//get params from page
	var params = {
			employeeId: 6,
			shiftId: 1,
			locationId: 1,
			workedNote: shiftNote
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
	    },
		error: function(err) {
	        alert("Error: " + err.responseText);
	    }
	});
}
