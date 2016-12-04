$(document).ready(function() {
	console.log("Loading js page.");
    $("#button_clockin").click(function(){
    	var url = 'http://localhost:8081/Scheduler/rest/clockin/clockin'
		if(document.getElementById('firstShift').checked){
			var selectedShiftId = document.getElementById('firstShift').value;
		}else if(document.getElementById('secondShift').checked){
			var selectedShiftId = document.getElementById('firstShift').value;
		}else if(document.getElementById('thirdShift').checked){
			var selectedShiftId = document.getElementById('firstShift').value;
		}else{
			alert("Radio button error!");
		}
		console.log("Value:" + selectedShiftId);
    	var params = {
    			employeeId: 1,
    			shiftId: selectedShiftId,
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
