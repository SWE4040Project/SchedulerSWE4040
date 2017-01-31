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
				<div class="panel panel-info">
		  		<div class="panel panel-danger notifications">
				  	<div class="panel-heading centered">
					  	<div>
		   	 				<i class="material-icons notifications">error</i> Not authorized. Please login from the login page.
		   	 			</div>
				   	</div>
				</div>
				<form action="/my/link/location" method="get">
				    <input type="submit" value="To login page" 
				         class="btn btn-primary" name="Submit" id="frm1_submit" />
				</form>
	          </div>
			</div>
		</div>
	</div>
</div>

<%@ include file="./common/footer.jsp" %>

</body>
</html>
