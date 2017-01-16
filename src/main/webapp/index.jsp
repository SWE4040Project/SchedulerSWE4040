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
					<%
					/*
						1. Get current employee state
						2. Load screen based on state
					*/
					String previousScreen = null;
					
					Cookie[] cookies = request.getCookies();
					if(cookies !=null){
					    for(Cookie cookie : cookies){
					        if(cookie.getName().equals("previousScreen")){
					        	previousScreen = cookie.getValue();
					        }
					    }
					}
					
					System.out.println("previousScreen = " + previousScreen);
					
					//load screen content
					if(previousScreen == null){
						response.sendRedirect("login.jsp");
					}else if(previousScreen.equals("login")){ 
						%><jsp:include page="clock_in.jsp"/><%
					}else if(previousScreen.equals("clock_in")){
						response.sendRedirect("in_shift.jsp");
					}else if(previousScreen.equals("in_shift")){
						//will be null on clockout
						response.sendRedirect("on_break.jsp");
					}else if(previousScreen.equals("on_break")){
						response.sendRedirect("in_shift.jsp");
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

<%@ include file="./common/footer.jsp" %>
<script src="./js/clock_in.js"></script>

</body>
</html>