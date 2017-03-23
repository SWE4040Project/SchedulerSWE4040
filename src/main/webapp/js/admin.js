$(document).ready(function() {
	console.log("Loading admin page.");

	calendarInit();
	
	$('#addButton').on( "click", function() {
		add_row();
	});
	
	load_data('employees');
});
function getViewType(){
	return viewType
}
var viewType = 0; //full schedule
function calendarInit(){
	$('#calendar').fullCalendar({
		eventSources: [{
				url : "rest/calendar/load",
				type: "POST",
				headers: {
					'Authorization':sessionStorage.getItem(SCHEDULER_APP.authorization),
					'xsrfToken': sessionStorage.getItem(SCHEDULER_APP.xsrfTokenName)
				},
				data: function () {
					return {
						viewType: getViewType()
					}
				}
			}
		],
		nextDayThreshold: "06:00:00",
		customButtons: {
			fullSchedule: {
				text: "Full Schedule",
				click: function () {
					viewType = 0;
					this.parentElement.childNodes[0].classList.remove("fc-state-active");
					this.parentElement.childNodes[1].classList.remove("fc-state-active");
					this.parentElement.childNodes[2].classList.remove("fc-state-active");
					this.classList.add("fc-state-active");
					$('#calendar').fullCalendar( 'refetchEvents' );
				}
			},
			mySchedule: {
				text: "My Schedule",
				click: function () {
					viewType = 2;
					this.parentElement.childNodes[0].classList.remove("fc-state-active");
					this.parentElement.childNodes[1].classList.remove("fc-state-active");
					this.parentElement.childNodes[2].classList.remove("fc-state-active");
					this.classList.add("fc-state-active");
					$('#calendar').fullCalendar( 'refetchEvents' );
				}
			},
			currentlyWorking: {
				text: "Working Now",
				click: function () {
					viewType = 1;
					this.parentElement.childNodes[0].classList.remove("fc-state-active");
					this.parentElement.childNodes[1].classList.remove("fc-state-active");
					this.parentElement.childNodes[2].classList.remove("fc-state-active");
					this.classList.add("fc-state-active");
					$('#calendar').fullCalendar( 'refetchEvents' );
				}
			}
		},
		header: {
			left: 'month,agendaWeek,agendaDay today prev,next',
			center: 'title',
			right: 'fullSchedule,mySchedule,currentlyWorking'
		},
		allDayDefault: false,
		slotEventOverlap: false,
		allDaySlot: false,
		eventLimit: 4,

		eventRender: function (event, element) {
			element.attr('href', 'javascript:void(0);');
			element.click(function() {
				$("#shiftId").html(event.id);
				$("#date").html(moment(event.start).format('MMM Do'));
				$("#inTime").html(moment(event.real_start).format('MMM Do h:mm A'));
				$("#outTime").html(moment(event.real_end).format('MMM Do h:mm A'));
				$("#notes").html(event.note);
				$("#eventContent").dialog({ modal: true, title: event.title, width:350});
				$("#approve").click(function(){
					$.ajax({
						url: "rest/calendar/approve",
						type: 'POST',
						headers: {
							'Authorization':sessionStorage.getItem(SCHEDULER_APP.authorization),
							'xsrfToken': sessionStorage.getItem(SCHEDULER_APP.xsrfTokenName)
						},
						data: {
							shift_id : document.getElementById("shiftId").textContent,
							
						},
						contentType: 'application/json',
						dataType: 'json',
						async: true,
						success: function() {
							$("#eventContent").css("display", "none");
						},
						error: function() {
							alert("Error: something went wrong");
						}
					});
				})
			});
		}
	})
	document.getElementsByClassName("fc-fullSchedule-button")[0].classList.add("fc-state-active");
}

function togglePages(page){
	if(page=="calendar"){
		document.getElementById("db_manager").style.display='none';
		document.getElementById("calendar").style.display='block';
		document.getElementById("csv").style.display='none';
	}else if(page=="csv"){
		document.getElementById("db_manager").style.display='none';
		document.getElementById("calendar").style.display='none';
		document.getElementById("csv").style.display='block';
	}else if(page=="db_manager"){
		document.getElementById("db_manager").style.display='block';
		document.getElementById("calendar").style.display='none';
		document.getElementById("csv").style.display='none';
	}
}
// function form_submit() {
// 	$('#fileupload').fileupload({
// 		dataType: 'json',
// 		add: function (e, data) {
// 			data.context = $('<button/>').text('Upload')
// 				.appendTo(document.body)
// 				.click(function () {
// 					data.context = $('<p/>').text('Uploading...').replaceAll($(this));
// 					data.submit();
// 				});
// 		},
// 		done: function (e, data) {
// 			data.context.text('Upload finished.');
// 		}
// 	});
// };

// $("form#csv_upload_form").submit(function () {
// 	$('#uploading').css("visibility", "visible");
//
// 	$.ajax({
// 		url: 'rest/csv_upload',
// 		type: 'POST',
// 		data: $("form#csv_upload_form").serialize(),
// 		success: function(data) {
// 			$('#uploading').textContent("Upload complete");
// 		},
// 		error: function(err) {
// 			alert("Error: " + err.responseText);
// 		}
// 	});
//
// 	return false;
// });

var editColumns = [];
var currentTableName = null;

function edit_row(no)
{
	var selectedTable = document.getElementById("adminTable");
	var selectedRow = selectedTable.rows[no].cells;
	
	var modalBody = document.getElementById("modal-body-id");
	var modalContent = '';
	for(var i = 0; i < editColumns.length; i++){
		modalContent += '<div class="form-group" style="margin-top: 0; overflow: hidden;"><label class="col-md-4 control-label">'
			+ editColumns[i]
			+'</label><div class="col-md-8"><input type="text" class="form-control"'
			+ 'id="'
			+ editColumns[i]+no +'" placeholder="Name" value="'
			+ selectedRow[i].innerHTML +'"></div></div>';
	}
	console.log("modalContent " + modalContent + "\n");
	modalBody.innerHTML = modalContent;
	
	console.log("edit_button"+no);
	$('#editModal').modal('show');
	
	$('#saveChangesButton').on( "click", function() {
		$('#saveChangesButton').prop('disabled', true);
		
		var url = SCHEDULER_APP.base_url+"/rest/database/edit?table="+currentTableName;
		
		//get current input data
		var values = [];
		$('input').each(function() {
		    values.push($(this).val());
		});
		
		//get params from page
		var params = {
			columnNames: editColumns,
			columnData: values
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
		        $('#saveChangesButton').prop('disabled', false);
		        $('#editModal').modal('hide');
		        $('#successAlert').modal('show').fadeIn( 500 );
		        setTimeout(function () {
                    window.location.replace(SCHEDULER_APP.base_url + '/admin.jsp');
                }, 1800);
		    },
			error: function(err) {
		        alert("Error: " + err.responseText);
		        $('#saveChangesButton').prop('disabled', false);
		    }
		});
	});
}

function add_row()
{
	var modalBody = document.getElementById("add-modal-body");
	var modalContent = '';
	for(var i = 0; i < editColumns.length; i++){
		modalContent += '<div class="form-group" style="margin-top: 0; overflow: hidden;"><label class="col-md-4 control-label">'
			+ editColumns[i]
			+'</label><div class="col-md-8"><input type="text" class="form-control"'
			+ 'id="'
			+ editColumns[i]+i +'" placeholder="'
			+ editColumns[i].toLowerCase() + '"></div></div>';
	}
	console.log("modalContent " + modalContent + "\n");
	modalBody.innerHTML = modalContent;
	
	$('#addModal').modal('show');
	
	$('#addChangesButton').on( "click", function() {
		$('#addChangesButton').prop('disabled', true);
		
		var url = SCHEDULER_APP.base_url+"/rest/database/add?table="+currentTableName;
		
		//get current input data
		var values = [];
		$('input').each(function() {
		    values.push($(this).val());
		});
		
		//get params from page
		var params = {
			columnNames: editColumns,
			columnData: values
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
		        $('#addChangesButton').prop('disabled', false);
		        $('#addModal').modal('hide');
		        $('#successAlert').modal('show').fadeIn( 500 );
		        setTimeout(function () {
                    window.location.replace(SCHEDULER_APP.base_url + '/admin.jsp');
                }, 1800);
		    },
			error: function(err) {
		        alert("Error: " + err.responseText);
		        $('#addChangesButton').prop('disabled', false);
		    }
		});
	});
}

function delete_row(no)
{
	var selectedTable = document.getElementById("adminTable");
	var selectedRow = selectedTable.rows[no].cells;
	var rowId = selectedRow[0].innerHTML;
	
	console.log("delete_button"+no);
	$('#deleteModal').modal('show');
	
	$('#deleteButton').on( "click", function() {
		$('#deleteButton').prop('disabled', true);
		
		var url = SCHEDULER_APP.base_url+"/rest/database/delete?table="+currentTableName;
		
		//get params from page
		var params = {
			rowId: editColumns[0],
			id: rowId
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
		        $('#deleteButton').prop('disabled', false);
		        $('#deleteModal').modal('hide');
		        $('#successAlert').modal('show').fadeIn( 500 );
		        setTimeout(function () {
                    window.location.replace(SCHEDULER_APP.base_url + '/admin.jsp');
                }, 1800);
		    },
			error: function(err) {
		        alert("Error: " + err.responseText);
		        $('#deleteButton').prop('disabled', false);
		    }
		});
	});
}

function load_data(tableName){
	console.log("tableName " + tableName);
	currentTableName = tableName;
	if(tableName == null){
		tableName = 'employees';
	}
	var tableTitle = document.getElementById("dropdownLabel");
	tableTitle.innerHTML = "<h3>" + tableName.toUpperCase() + " table <b class='caret'></b></h3>";
	
	var url = SCHEDULER_APP.base_url+"/rest/database?table="+tableName;
	$.ajax({
	    url: url,
	    type: 'GET',
        headers: {
            'Authorization':sessionStorage.getItem(SCHEDULER_APP.authorization),
            'xsrfToken': sessionStorage.getItem(SCHEDULER_APP.xsrfTokenName)
        },
	    contentType: 'application/json',
	    async: true,
	    success: function(data) {
	        // console.log("data.columnCount: " + data.columnCount);
	        // console.log("data.columns: " + data.columns);
	        // console.log("data.rowCount: " + data.rowCount);
	        // console.log("data.rows: " + data.rows);
	        
	        //set columns
	        editColumns = [];
            var table = document.getElementById("adminTable");
            columns = '<tr>';
            for(i = 0; i < data.columnCount; i++){
            	columns += '<th>'+ data.columns[i] +'</th>';
            	editColumns.push(data.columns[i]);
            }
            columns += '</tr>';
            table.tHead.innerHTML = columns;
            
          //if no data
	        if(data.rowCount == 0){
	        	table.insertRow(1).innerHTML = "<td><i>No data to be loaded.</i></td>";
	        	return;
	        }
            
            //add rows
            for(i = 0; i < data.rowCount; i++){
	        	var row = table.insertRow(i+1);	
	        	var tableRow = '';
	        	if(i%2 == 0){
	        		tableRow = 'class="info"';
	        	}
	        	buildRow = '';
	            for(j = 0; j < data.columnCount; j++){
	            	buildRow += '<td '+tableRow+'>'
	            		+ data.rows[i][j] +'</td>';
	            }
	            buildRow += '<td '+ tableRow 
	            	+ '><a href="javascript:void(0)" class="btn btn-sm btn-raised btn-primary"'
	            	+ 'onclick="edit_row('+ (i+1) +')"'
	            	+ 'id="edit_button'+ (i+1) +'">Edit</a>'
	            	+ '<a href="javascript:void(0)" class="btn btn-sm btn-raised btn-danger"'
	            	+ 'onclick="delete_row('+ (i+1) +')"'
	            	+ 'id="delete_button'+ (i+1) +'">Delete</a></td>';
	            row.innerHTML = buildRow;
	        }
	    },
		error: function(err) {
	        alert("Error: " + err.responseText);
	    }
	});
}
