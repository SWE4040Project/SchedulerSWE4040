$(document).ready(function() {
	console.log("Loading admin page.");
	load_data('employees');
});

var editColumns = [];

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
	modalContent += '';
	console.log("modalContent " + modalContent + "\n");
	modalBody.innerHTML = modalContent;
	
	console.log("edit_button"+no);
	$('#editModal').modal({
		show: 'true'
	});
}

function load_data(tableName){
	console.log("tableName " + tableName);
	if(tableName == null){
		tableName = 'employees';
	}
	var tableTitle = document.getElementById("tableTitle");
	tableTitle.innerHTML = tableName.toUpperCase() + " table";
	
	var url = "https://localhost:8443/Scheduler/rest/database?table="+tableName;
	$.ajax({
	    url: url,
	    type: 'GET',
	    contentType: 'application/json',
	    async: true,
	    success: function(data) {
	        console.log("data.columnCount: " + data.columnCount);
	        console.log("data.columns: " + data.columns);
	        console.log("data.rowCount: " + data.rowCount);
	        console.log("data.rows: " + data.rows);
	        
	        //TODO if no data...
	        
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
	            	+ '<a href="javascript:void(0)" class="btn btn-sm btn-raised btn-danger">Delete</a></td>';
	            row.innerHTML = buildRow;
	        }
	    },
		error: function(err) {
	        alert("Error: " + err.responseText);
	    }
	});
}
