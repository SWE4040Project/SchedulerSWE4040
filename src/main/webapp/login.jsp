<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Clock in Screen</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" type="text/css" href="./css/clock_css.css">
	<link rel="stylesheet" type="text/css" href="./css/main.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
	<script src="./js/global_web.js"></script>
	<script src="./js/login.js"></script>
</head>
<body>
<div id="main_container">
    <div id="title_header"></div>
    <div id="content_container">
        <div class="login_area">
            <form>
                <div class="login_field" id="userName">
                    <label class="label">Username: </label>
                    <input type="text" placeholder="Employee ID" name="loginID" required>
                </div>
                <div class="login_field" id="password">
                    <label class="label">Password: </label>
                    <input type="password" placeholder="Password" name="pwd" required>
                </div>
                <div class="login_field"  id="login_button"><button type="submit">Sign In</button></div>
            </form>
        </div>
    </div>
    <div id="footer"></div>
</div>
</body>
</html>
