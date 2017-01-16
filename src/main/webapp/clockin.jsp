<!-- Version 2 of clockin - modulizing the clockin screen. -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="./common/header.jsp" %>
	<link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>

<div class="container">
  <div class="row vertical-offset-100">
	<div class="col-md-4 col-md-offset-4">
		<div class="panel panel-default">
			<div class="panel panel-primary">
				  <div class="panel-heading">
				    <h3 class="panel-title">Clocked In Status</h3>
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
						
						      	<p id="start_time" class="list-group-item-text">Clocked in: 1:58 pm Monday, Jan 26th</p>
						      	<p id="end_time" class="list-group-item-text">Clocked out: --</p>
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
						      	<h4 class="list-group-item-heading">Notes</h4>
						
						      	<p class="list-group-item-text">Enter Shift Notes Here.</p>
						    </div>
					  	</div>
				  	</div>

				    <div class="form-group">
				      <div class="centered col-md-12">
				        <button class="btn btn-primary">Okay</button>
				      </div>
				    </div>
	              </div>
              </div>
          </div>
      </div>
  </div>
</div>

<%@ include file="./common/footer.jsp" %>
<script src="./js/clock_in.js"></script>

</body>
</html>
