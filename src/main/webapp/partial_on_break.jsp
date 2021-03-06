<div class="panel-heading">
   <h3 class="panel-title">On Break Status</h3>
</div>
<div class="panel-body">
   <div class="centered">
   	<legend>Clocked In.</legend>
  	</div>

	<%@ page import="org.Employee" %>
	<%@ page import="org.Shift" %>
	<%@ page import="java.text.SimpleDateFormat" %>
	<%@ page import="java.sql.Timestamp" %>

	<%
		Employee emp = (Employee) request.getAttribute("employeeObject");

		Shift shift = Shift.getRecentShiftById(emp.getId(),emp.getCompany_id());
		String sTime = (new SimpleDateFormat("h:mm a")).format(shift.getScheduled_start_time().getTime());
		String eTime = (new SimpleDateFormat("h:mm a")).format(shift.getScheduled_end_time().getTime());
		String sDate = (new SimpleDateFormat("EEEE, MMM dd")).format(shift.getScheduled_start_time().getTime());
		String currentTime = (new SimpleDateFormat("h:mm a")).format(new java.util.Date());
		Timestamp rsTimestamp = shift.getReal_start_time();
		Timestamp reTimestamp = shift.getReal_end_time();
		String rsTime = "--";
		String reTime = "--";
		if(rsTimestamp != null){
			rsTime = (new SimpleDateFormat("h:mm a EEEE, MMM dd")).format(shift.getReal_start_time().getTime());
		}
		if(reTimestamp != null){
			reTime = (new SimpleDateFormat("h:mm a EEEE, MMM dd")).format(shift.getReal_end_time().getTime());
		}

		int progress = 20;


	%>

  	<div class="list-group">
 			<div class="list-group-item">
    	<div class="row-action-primary">
      		<i id="check-icon" class="btn-primary material-icons">check</i>
	    </div>
	    <div class="row-content">
	      	<div data-toggle="tooltip" data-placement="left" title="" data-original-title="Displays the current shift you are clocked in for." class="action-secondary"><i class="material-icons">info</i></div>
	      	<h4 class="list-group-item-heading">Shift</h4>
	
	      	<p id="start_time" class="list-group-item-text">Clocked in: <%=rsTime%></p>
	      	<p id="end_time" class="list-group-item-text">Clocked out: <%=reTime%> </p>
	    </div>
  	</div>
  	<div class="list-group-separator"></div>
  	<br>
  	<div class="list-group-item">
    	<div class="col-md-9">
    		<p id="scheduled_shift" class="list-group-item-text">Shift: <%= sTime %> - <%= eTime %></p>
      		<p id="scheduled_date" class="list-group-item-text"> <%=sDate%></p>
    	</div>
    	<div class="col-md-3">
    		<b id="current_time"><%=currentTime%></b>
   		</div>
   		<div class="row-action-primary">
   			<br>
   		</div>
      	<div class="col-md-12">
	      	<div class="progress">
			  <div class="progress-bar progress-bar-primary" style="width: <%=progress%>%"></div>
			</div>
		</div>
  	</div>
	<div class="list-group-separator"></div>
  	<div class="list-group-item">
    	<div class="row-action-primary">
      		<i class="material-icons">info</i>
	    </div>
	    <div class="row-content">
	      	<h4 class="list-group-item-heading">Notes</h4>
	
	      	<p class="list-group-item-text">Enter Shift Notes Here.</p>
	    </div>
  	</div>
 	</div>

   <div class="form-group">
     <div class="centered col-md-12">
       <button id="button_breakout" class="btn btn-info">End Break</button>
     </div>
   </div>
</div>

<script src="./js/on_break.js"></script>
<script src="./js/add_shift_note.js"></script>