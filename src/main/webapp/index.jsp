<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
String previousScreen = null;

Cookie[] cookies = request.getCookies();
if(cookies !=null){
    for(Cookie cookie : cookies){
        if(cookie.getName().equals("previousScreen")){
        	previousScreen = cookie.getValue();
        }
        //System.out.println("cookies: " + cookie.getName());
    }
}

System.out.println("previousScreen = " + previousScreen);

//redirect to proper screen
if(previousScreen == null){
	response.sendRedirect("login.jsp");
}else if(previousScreen.equals("login")){
	response.sendRedirect("clock_in.jsp");
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
