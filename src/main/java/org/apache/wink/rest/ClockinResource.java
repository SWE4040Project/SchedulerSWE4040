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

package org.apache.wink.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.ClockDbHandler;
import org.ClockinParameters;
import org.DatabaseConnectionPool;
import org.apache.wink.common.annotations.Workspace;

import com.google.gson.Gson;

import java.sql.*;

@Workspace(workspaceTitle = "Employee Clockin", collectionTitle = "Clockin")
@Path("clockin")
public class ClockinResource {
	
	private static final String PATH_CLOCKIN 		= "clockin";
	private static final String PATH_JSON          	= "json";
	private static final String PATH_AUTHENTICATE  	= "authenticate";
	private static final String PATH_CONNECTIONS	= "database/connections";
	private static final String PATH_DATABASE 		= "database";
	 
	Gson gson = new Gson();

    @Path(PATH_CLOCKIN)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clockin(String obj) {
    	
    	Status status = Response.Status.OK;
    	
    	ClockinParameters params = gson.fromJson(obj, ClockinParameters.class);

    	String result = "{\"Status\":\""+ params.getEmployeeId() +"\"}";
    	
		ClockDbHandler clk = new ClockDbHandler();
		String error = clk.clockInWithScheduledShift(params.getEmployeeId(), params.getShiftId(), params.getLocationID());
    	if( error.length() > 0 ){
    		status = Response.Status.INTERNAL_SERVER_ERROR;
    		result = "{\"Status\":\""+ error +"\"}";
    	}
    	
		return Response.status(status).entity(result).build();
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
    
    @Path(PATH_AUTHENTICATE)
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response isAuth(@QueryParam("username") String username, @QueryParam("password") String password) {
    	
    	Status status = Response.Status.OK;
    	
    	// JSON to Java object, read it from a Json String.
    	if(password == null || password.length() <= 0){
    		status = Response.Status.FORBIDDEN;
    	}
    	String jsonInString = "{'name' : "+username+",'other' : 'Other String','randomValue' : 12345}";
    	SimplePOJO json = gson.fromJson(jsonInString, SimplePOJO.class);
    	
    	json.setOther("Success");

    	// JSON to JsonElement, convert to String later.
    	String result = gson.toJson(json);
    	    	
    	return Response.status(status).entity(result).header("Content-Type", "application/json").build();	
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
