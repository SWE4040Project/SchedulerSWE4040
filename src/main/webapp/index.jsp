<!-- Version 2 of clockin - modulizing the clockin screen. -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="./common/header.jsp" %>
	<link rel="stylesheet" type="text/css" href="css/main.css">
	
	<%@ include file="./common/footer.jsp" %>
</head>
<body>

<div class="container">
	<div class="row vertical-offset-100">
		<div class="col-md-4 col-md-offset-4">
			<div style="float:right;"><button style="margin: 0px;" onclick="globalweb_employee_logout();" class="btn btn-warning">Logout</button></div>
			<div class="panel panel-default">
				<div class="panel panel-primary">
					<%@ page import="org.Employee" %>
					<%@ page import= "org.Shift" %>
					<%@ page import="java.text.SimpleDateFormat" %>
					<%@ page import="java.sql.Timestamp" %>
					<%@ page import="org.ClockDbHandler" %>
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

					<%!
						public void jspClockIn(Shift shift){
							ClockDbHandler clockDb = new ClockDbHandler();
							clockDb.clockInWithScheduledShift(shift.getEmployee_id(), shift.getId(), shift.getLocation_id());
						}
					%>

					<%


					System.out.println("index.jsp called.");
					
					//load screen content
					if(emp == null){
						response.sendRedirect("login.jsp");
					}else if(emp.getEmployeeClockState() == 0){
						%><jsp:include page="partial_clock_in.jsp"/><%
					}else if(emp.getEmployeeClockState() == 1){
						%><jsp:include page="partial_in_shift.jsp"/><%
					}else if(emp.getEmployeeClockState() == 2){
						%><jsp:include page="partial_on_break.jsp"/><%
					}else{
						//default
						response.sendRedirect("login.jsp");
					}
					%>
		      </div>
		  </div>
		</div>
	</div> 
</div>

</body>
</html>