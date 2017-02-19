<div class="panel-heading">
   <h3 class="panel-title">In Shift Status</h3>
</div>
<div class="panel-body">
   <div class="centered">
   	<legend>Clocked In.</legend>
  	</div>
  	
  	<div class="list-group">
 			<div class="list-group-item">
    	<div class="row-action-primary">
      		<i id="check-icon" class="btn-primary material-icons">check</i>
	    </div>
	    <div class="row-content">
	      	<div data-toggle="tooltip" data-placement="left" title="" data-original-title="Displays the current shift you are clocked in for." class="action-secondary"><i class="material-icons">info</i></div>
	      	<h4 class="list-group-item-heading">Shift</h4>
	      	
	      	<%@ page import="org.Shift" %>
	      	<% Shift shift = (Shift) request.getAttribute("shiftObject"); %>
	
	      	<p id="start_time" class="list-group-item-text">Clocked in: 1:58 pm Monday, Jan 26th</p>
	      	<p id="end_time" class="list-group-item-text">Clocked out: -- </p>
	    </div>
  	</div>
  	<div class="list-group-separator"></div>
  	<br>
  	<div class="list-group-item">
    	<div class="col-md-9">
    		<p id="scheduled_shift" class="list-group-item-text">Shift: 2 pm - 10 pm</p>
      		<p id="scheduled_date" class="list-group-item-text"> Monday, Jan 26th</p>
    	</div>
    	<div class="col-md-3">
    		<b id="current_time">4:24 pm</b>
   		</div>
   		<div class="row-action-primary">
   			<br>
   		</div>
      	<div class="col-md-12">
	      	<div class="progress">
			  <div class="progress-bar progress-bar-primary" style="width: 30%"></div>
			</div>
		</div>
  	</div>
	<div class="list-group-separator"></div>
  	<div class="list-group-item">
    	<div class="row-action-primary">
      		<i class="material-icons">info</i>
	    </div>
	    <div class="row-content">
	      	<h4 class="list-group-item-heading">Notes <button style="float: right; margin: 0px;" id="button_addshiftnotes" class="btn btn-info">Update Notes</button></h4>
	      	<textarea id="shift-notes" class="list-group-item-text" placeholder='Enter Shift Notes Here.' ></textarea>
	    </div>
  	</div>
 	</div>

   <div class="form-group">
     <div class="centered col-md-12">
       <button id="button_breakin" class="btn btn-info">Take Break</button>
       <button id="button_clockout" class="btn btn-danger">Clock-out</button>
     </div>
   </div>
</div>

<script src="./js/in_shift.js"></script>
<script src="./js/add_shift_note.js"></script>