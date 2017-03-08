$(document).ready(function() {
	$("#button_addshiftnotes").click(function(){
    	addshiftnote();
    });  
});

function addshiftnote(){
	
	//sending string
	var shiftNote = "";
	//get from page
	shiftNote = $("textarea#shift_notes").val();
	console.log("Shift note: " + shiftNote);
	
	var url = SCHEDULER_APP.url_clockin + '/addshiftnote';
	//get params from page
	var params = {
		workedNote: shiftNote
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
            document.getElementById("shift_notes").style.border = "thick solid #00aa9a";
            setTimeout(function(){
                document.getElementById("shift_notes").style.border = "";
			}, 2000)
	    },
		error: function(err) {
            document.getElementById("shift_notes").style.border = "thick solid #f44336";
	    }
	});
}
