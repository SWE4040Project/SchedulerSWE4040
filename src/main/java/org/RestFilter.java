package org;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestFilter implements Filter {

  private String contextPath;

  public void init(FilterConfig fc) throws ServletException {
    contextPath = fc.getServletContext().getContextPath();
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;  

    System.out.println("RestFilter called.");
     
    //get authorization token from Header
    String jsonWebToken = null;
    String xsrfToken = null;

    //From Header
    jsonWebToken = req.getHeader(JsonVar.AUTHORIZATION);
    if(jsonWebToken != null){
        jsonWebToken = jsonWebToken.replace(JsonVar.BEARER, ""); //remove Bearer<whitespace>
    }
    xsrfToken = req.getHeader(JsonVar.XSRF_TOKEN);

      System.out.println("jsonWebToken and xsrfToken = " + jsonWebToken + " : " + xsrfToken);

    WebTokens webTokens = new WebTokens(jsonWebToken, xsrfToken);
    Employee emp = new AuthenticateDbHandler().employeeFromJWT(webTokens);
    
    if(jsonWebToken == null || xsrfToken == null){
    	//not logged in. Redirect to login
        System.out.println("jsonWebToken and/or xsrfToken null. " + jsonWebToken + " : " + xsrfToken);
    	request.getRequestDispatcher("/login.jsp").forward(request, response);
    }else{
		if(emp == null || emp.getId() < -1 ){
			System.out.println("NOT AUTHORIZED.");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
		
		//However note if you receive a GET request and try to forward to a POST resource, 
		// It will throw a 405 error.
		String reqUrl = req.getRequestURI();
		reqUrl = reqUrl.replace("/Scheduler", "");
		System.out.println("Authorized. Generated route: " + reqUrl );
		
		//Pass employee object to request
		request.setAttribute("employeeObject", emp);
		request.getRequestDispatcher(reqUrl).forward(request, response); 
    }
	
  }

  public void destroy() {
  }
}