package org;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

public class AdminAuthenticationFilter implements Filter {

  private String contextPath;

  public void init(FilterConfig fc) throws ServletException {
    contextPath = fc.getServletContext().getContextPath();
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;  

    System.out.println("AdminAuthenticationFilter called.");

      //get authorization token from Cookies
      Cookie[] cookies = req.getCookies();
      String jsonWebToken = null;
      String xsrfToken = null;

      //from Cookies
      if (cookies != null) {
          for (int i = 0; i < cookies.length; i++) {
              if (cookies[i].getName().equals("Authorization")) {
                  jsonWebToken = cookies[i].getValue();
              }
              if (cookies[i].getName().equals("xsrfToken")) {
                  xsrfToken = cookies[i].getValue();
              }
          }
      }

    WebTokens webTokens = new WebTokens(jsonWebToken, xsrfToken);
    Employee emp = new AuthenticateDbHandler().employeeFromJWT(webTokens);
    
    if(jsonWebToken == null || xsrfToken == null){
    	//not logged in. Redirect to login
    	request.getRequestDispatcher("/login.jsp").forward(request, response);
    }else{
		if( emp == null || !emp.isSuper_admin()){
			System.out.println("Need SUPER ADMIN privileges to access.");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
		//However note if you receive a GET request and try to forward to a POST resource, 
		// It will throw a 405 error.
		String reqUrl = req.getRequestURI();
		reqUrl = reqUrl.replace("/Scheduler", "");
		System.out.println("Authorized. Generated route: " + reqUrl );
		request.getRequestDispatcher(reqUrl).forward(request, response); 
    }
	
  }

  public void destroy() {
  }
}