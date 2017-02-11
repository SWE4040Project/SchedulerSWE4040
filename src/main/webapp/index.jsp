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
					<%
					Employee emp = (Employee) request.getAttribute("employeeObject");

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