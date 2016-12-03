<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="header_includes.jsp" %>
	<link rel="stylesheet" type="text/css" href="./css/main.css">
	<script src="./js/login.js"></script>
</head>
<body>
<div id="main_container">
    <div id="title_header"></div>
    <div id="content_container">
        <div class="login_area">
            <form method='POST'>
                <div class="login_field" id="userName">
                    <label class="label">Username: </label>
                    <input type="text" placeholder="Employee Name" name="username" required>
                </div>
                <div class="login_field" id="password">
                    <label class="label">Password: </label>
                    <input type="password" placeholder="Password" name="password" required>
                </div>
                <div class="login_field"  id="login_button"><button type="submit">Sign In</button></div>
            </form>
        </div>
    </div>
    <div id="footer"></div>
</div>
</body>
</html>
