package org.apache.wink.rest;

/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *  
 *   http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  
 *******************************************************************************/


import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.AuthenticateDbHandler;
import org.ClockDbHandler;
import org.ClockinParameters;
import org.DatabaseConnectionPool;
import org.Employee;
import org.JsonVar;
import org.LoginParameters;
import org.WebTokens;
import org.Employee.Clock_State;

import com.google.gson.Gson;

import java.sql.*;

@Path("/")
public class ClockinResource {
	
	private static final String PATH_CLOCKIN 		= "clockin/clockin";
	private static final String PATH_CLOCKOUT 		= "clockin/clockout";
	private static final String PATH_BREAKIN 		= "clockin/breakin";
	private static final String PATH_BREAKOUT 		= "clockin/breakout";
	private static final String PATH_ADDSHIFTNOTE 	= "clockin/addshiftnote";
	private static final String PATH_JSON          	= "json";
	private static final String LOGIN  				= "login";
	private static final String PATH_CONNECTIONS	= "database/connections";
	private static final String PATH_DATABASE 		= "database";
	private static final String PATH_TEST_AUTH      = "clockin/testauth";
	 
	Gson gson = new Gson();

    @Path(PATH_CLOCKIN)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clockin(@CookieParam("Authorization") String jsonWebToken, @CookieParam("xsrfToken") String xsrfToken, String obj) {
    	
    	AuthenticateDbHandler auth = new AuthenticateDbHandler();
    	WebTokens webTokens = new WebTokens(jsonWebToken, xsrfToken);
//    	if( !auth.isAuth(webTokens) ){
//    		return Response.status(Response.Status.UNAUTHORIZED).entity("{\"ERROR\" : "
//        			+ "\"Employee token "+ webTokens.getJsonWebToken()
//        			+" is not authorized.\"}").build();	
//    	}
//    	System.out.println("Authorized.");
    	
    	Status status = Response.Status.OK;
    	
    	ClockinParameters params = gson.fromJson(obj, ClockinParameters.class);
    	params.setEmployeeId(-1); //clear employeeId if one is passed.
    	//parse employeeId from jsonWebToken
    	int empId = auth.getInt(JsonVar.EMPLOYEE_ID, webTokens.getJsonWebToken());
    	if(empId < 0){
    		status = Response.Status.BAD_REQUEST;
    	}
    	params.setEmployeeId(empId);

    	String result = "{\"Status\":\"Employee "+ params.getEmployeeId() +" is clocked in.\"}";
    	
		ClockDbHandler clk = new ClockDbHandler();
		String error = clk.clockInWithScheduledShift(params.getEmployeeId(), params.getShiftId(), params.getLocationID());
    	if( error.length() > 0 ){
    		status = Response.Status.INTERNAL_SERVER_ERROR;
    		result = "{\"Status\":\""+ error +"\"}";
    	}
    	
		return Response.status(status).entity(result).build();
    }
    
    @Path(PATH_CLOCKOUT)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clockout(@CookieParam("Authorization") String jsonWebToken, @CookieParam("xsrfToken") String xsrfToken, String obj) {
    	
    	AuthenticateDbHandler auth = new AuthenticateDbHandler();
    	WebTokens webTokens = new WebTokens(jsonWebToken, xsrfToken);
//    	if( !auth.isAuth(webTokens) ){
//    		return Response.status(Response.Status.UNAUTHORIZED).entity("{\"ERROR\" : "
//        			+ "\"Employee token "+ webTokens.getJsonWebToken()
//        			+" is not authorized.\"}").build();	
//    	}
//    	System.out.println("Authorized.");
    	
    	Status status = Response.Status.OK;
    	
    	ClockinParameters params = gson.fromJson(obj, ClockinParameters.class);
    	params.setEmployeeId(-1); //clear employeeId if one is passed.
    	//parse employeeId from jsonWebToken
    	int empId = auth.getInt(JsonVar.EMPLOYEE_ID, webTokens.getJsonWebToken());
    	if(empId < 0){
    		status = Response.Status.BAD_REQUEST;
    	}
    	params.setEmployeeId(empId);

    	String result = "{\"Status\":\"Employee "+ params.getEmployeeId() +" is clocked out.\"}";
    	
		ClockDbHandler clk = new ClockDbHandler();
		String error = clk.clockOutWithScheduledShift(params.getEmployeeId(), params.getShiftId(), params.getLocationID());
    	if( error.length() > 0 ){
    		status = Response.Status.INTERNAL_SERVER_ERROR;
    		result = "{\"Status\":\""+ error +"\"}";
    	}
    	
		return Response.status(status).entity(result).build();
    }
    
    @Path(PATH_BREAKIN)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response breakin(@CookieParam("Authorization") String jsonWebToken, @CookieParam("xsrfToken") String xsrfToken, String obj) {
    	
    	AuthenticateDbHandler auth = new AuthenticateDbHandler();
    	WebTokens webTokens = new WebTokens(jsonWebToken, xsrfToken);
//    	if( !auth.isAuth(webTokens) ){
//    		return Response.status(Response.Status.UNAUTHORIZED).entity("{\"ERROR\" : "
//        			+ "\"Employee token "+ webTokens.getJsonWebToken()
//        			+" is not authorized.\"}").build();	
//    	}
//    	System.out.println("Authorized.");
    	
    	Status status = Response.Status.OK;
    	
    	ClockinParameters params = gson.fromJson(obj, ClockinParameters.class);
    	params.setEmployeeId(-1); //clear employeeId if one is passed.
    	//parse employeeId from jsonWebToken
    	int empId = auth.getInt(JsonVar.EMPLOYEE_ID, webTokens.getJsonWebToken());
    	if(empId < 0){
    		status = Response.Status.BAD_REQUEST;
    	}
    	params.setEmployeeId(empId);
    	
    	String result = "{\"Status\":\"Employee "+ params.getEmployeeId() +" is on break.\"}";
    	
		ClockDbHandler clk = new ClockDbHandler();
		String error = clk.breakInWithScheduledShift(params.getEmployeeId(), params.getShiftId(), params.getLocationID());
    	if( error.length() > 0 ){
    		status = Response.Status.INTERNAL_SERVER_ERROR;
    		result = "{\"Status\":\""+ error +"\"}";
    	}
    	
		return Response.status(status).entity(result).build();
    }
    
    @Path(PATH_BREAKOUT)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response breakout(@CookieParam("Authorization") String jsonWebToken, @CookieParam("xsrfToken") String xsrfToken, String obj) {
    	
    	AuthenticateDbHandler auth = new AuthenticateDbHandler();
    	WebTokens webTokens = new WebTokens(jsonWebToken, xsrfToken);
//    	if( !auth.isAuth(webTokens) ){
//    		return Response.status(Response.Status.UNAUTHORIZED).entity("{\"ERROR\" : "
//        			+ "\"Employee token "+ webTokens.getJsonWebToken()
//        			+" is not authorized.\"}").build();	
//    	}
//    	System.out.println("Authorized.");
    	
    	Status status = Response.Status.OK;
    	
    	ClockinParameters params = gson.fromJson(obj, ClockinParameters.class);
    	params.setEmployeeId(-1); //clear employeeId if one is passed.
    	//parse employeeId from jsonWebToken
    	int empId = auth.getInt(JsonVar.EMPLOYEE_ID, webTokens.getJsonWebToken());
    	if(empId < 0){
    		status = Response.Status.BAD_REQUEST;
    	}
    	params.setEmployeeId(empId);
    	
    	String result = "{\"Status\":\"Employee "+ params.getEmployeeId() +" is off break.\"}";
    	
		ClockDbHandler clk = new ClockDbHandler();
		String error = clk.breakOutWithScheduledShift(params.getEmployeeId(), params.getShiftId(), params.getLocationID());
    	if( error.length() > 0 ){
    		status = Response.Status.INTERNAL_SERVER_ERROR;
    		result = "{\"Status\":\""+ error +"\"}";
    	}
    	
		return Response.status(status).entity(result).build();
    }
    
    @Path(PATH_ADDSHIFTNOTE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addnote(@CookieParam("Authorization") String jsonWebToken, @CookieParam("xsrfToken") String xsrfToken, String obj) {
    	
    	AuthenticateDbHandler auth = new AuthenticateDbHandler();
    	WebTokens webTokens = new WebTokens(jsonWebToken, xsrfToken);
//    	if( !auth.isAuth(webTokens) ){
//    		return Response.status(Response.Status.UNAUTHORIZED).entity("{\"ERROR\" : "
//        			+ "\"Employee token "+ webTokens.getJsonWebToken()
//        			+" is not authorized.\"}").build();	
//    	}
    	System.out.println("Authorized.");
    	
    	Status status = Response.Status.OK;
    	
    	ClockinParameters params = gson.fromJson(obj, ClockinParameters.class);
    	params.setEmployeeId(-1); //clear employeeId if one is passed.
    	//parse employeeId from jsonWebToken
    	int empId = auth.getInt(JsonVar.EMPLOYEE_ID, webTokens.getJsonWebToken());
    	if(empId < 0){
    		status = Response.Status.BAD_REQUEST;
    	}
    	params.setEmployeeId(empId);

    	String result = "{\"Status\":\"Employee "+ params.getEmployeeId() +" has added or modified their shift notes.\"}";
    	
		ClockDbHandler clk = new ClockDbHandler();
		String error = clk.addNoteWithScheduledShift(params.getEmployeeId(), params.getShiftId(), params.getWorkedNote());
    	if( error.length() > 0 ){
    		status = Response.Status.INTERNAL_SERVER_ERROR;
    		result = "{\"Status\":\""+ error +"\"}";
    	}
    	
		return Response.status(status).entity(result).build();
    }
    
    @Path(LOGIN)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String obj) {
    	
    	Status status = Response.Status.OK;
    	
    	LoginParameters params = gson.fromJson(obj, LoginParameters.class);
    	
    	if(params.getPassword() == null || params.getPassword().length() <= 0){
    		status = Response.Status.FORBIDDEN;
    	}
    	AuthenticateDbHandler auth = new AuthenticateDbHandler();
    	Employee emp = auth.login(params.getUsername(), params.getPassword());
    	if(emp == null){
    		status = Response.Status.FORBIDDEN;
    	}
    	
    	//create jwt   
    	WebTokens webTokens = auth.createJWT(emp);
    	
    	if(webTokens == null){
    		return Response.status(status).entity("{\"Login\":\"Invalid\"}").header("Content-Type", "application/json").build();	
    	}
    	
    	return Response.status(status).entity("{\"Login\":\"Ok\"}").header("Content-Type", "application/json")
				.header("SET-COOKIE", "Authorization=" + webTokens.getJsonWebToken() 
                      + ";Path=/Scheduler; Secure; HttpOnly")
				.header("SET-COOKIE", "xsrfToken=" + webTokens.getXsrfToken() 
                + ";Path=/Scheduler; ").build();
    }
    
    
    /*
     * Test REST calls
     * 1. Json is the most basic call - to ensure the system is up and running
     * 2. 
     */
    
    @Path(PATH_TEST_AUTH)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response testAuth() {
		return Response.status(Response.Status.OK).entity("{\"Authorized\" : \"true\"}").build();	
    }
       	
    @Path(PATH_JSON)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson() {
    	
    	// JSON to Java object, read it from a Json String.
    	String jsonInString = "{'name' : 'Brent','other' : 'Other String','randomValue' : 12345}";
    	SimplePOJO json = gson.fromJson(jsonInString, SimplePOJO.class);
    	
    	json.setOther("Success");

    	// JSON to JsonElement, convert to String later.
    	String result = gson.toJson(json);
    	    	
    	return Response.status(Response.Status.OK).entity(result).build();	
    }
    
    @Path(PATH_CONNECTIONS)
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDatabaseConnectionCount(){
    	return Response.status(Response.Status.OK)
    			.entity(
    				"{\"Active Database Connections\": \"" 
    				+ DatabaseConnectionPool.getInstance().getActiveConnectionCount()
    				+ "\"}"
    			)
    			.header("Content-Type", "application/json").build();
    }
    
    @Path(PATH_DATABASE)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHello() {
    	
    	Status status = Response.Status.OK;
    	Gson gson = new Gson();
    	ArrayPOJO arr = new ArrayPOJO();
    	    	
    	Connection con = null;
    	try{  
    		
    		//connect to database via connection pool
    		DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
        	con = dbpool.getConnection();
        	
    		//create the statement object  
    		Statement stmt = con.createStatement();  
    		  
    		//step4 execute query  
    		ResultSet rs=stmt.executeQuery("select * from users");  
    		while(rs.next())  {
    			arr.add(rs.getString(2));
    			arr.add(rs.getString(3));
    		}
		}catch(Exception e){ 
			System.out.println("Catching exception: " + e.getMessage());
			status = Response.Status.INTERNAL_SERVER_ERROR;
		}finally{
			//step5 close the connection object  
    		try {con.close();} catch (Exception e){
    			System.out.println("Finally: " + e.getMessage());
    		}
		}
    	
    	String result = gson.toJson(arr);
    		  
        return Response.status(status).entity(result).header("Content-Type", "application/json").build();
    }
}
