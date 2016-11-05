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

import java.net.URI;
import java.util.Date;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.wink.common.annotations.Workspace;
import org.apache.wink.common.http.HttpStatus;
import org.apache.wink.common.model.synd.SyndContent;
import org.apache.wink.common.model.synd.SyndEntry;
import org.apache.wink.common.model.synd.SyndFeed;
import org.apache.wink.common.model.synd.SyndText;
import org.apache.wink.server.utils.LinkBuilders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.*;

@Workspace(workspaceTitle = "Demo Bookmarks Service", collectionTitle = "My Bookmarks")
@Path("bookmarks")
public class ClockinResource {

    private static final String BOOKMARK          = "bookmark";
    private static final String SUB_RESOURCE_PATH = "{" + BOOKMARK + "}";

    	
    @Path("/json")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson() {
    	
    	Gson gson = new Gson();
    	
    	// JSON to Java object, read it from a Json String.
    	String jsonInString = "{'name' : 'Brent','other' : 'Other String','randomValue' : 12345}";
    	SimplePOJO json = gson.fromJson(jsonInString, SimplePOJO.class);
    	
    	json.setOther("Success");

    	// JSON to JsonElement, convert to String later.
    	String result = gson.toJson(json);
    	    	
    	return Response.status(Response.Status.OK).entity(result).build();	
    }
    
    @Path("/authenticate")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response isAuth(@QueryParam("username") String username, @QueryParam("password") String password) {
    	
    	Status status = Response.Status.OK;
    	
    	Gson gson = new Gson();
    	
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
    
    @Path("/database")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHello() {
    	
    	Status status = Response.Status.OK;
    	Gson gson = new Gson();
    	ArrayPOJO arr = new ArrayPOJO();
    	    	
    	Connection con = null;
    	try{  
    		
    		//step1 load the driver class  
    		Class.forName("oracle.jdbc.driver.OracleDriver");  
    		  
    		//step2 create  the connection object  
    		con = DriverManager.getConnection(  
    		"jdbc:oracle:thin:@localhost:1521:xe","system","TonThefloor22T");  
    		  
    		//step3 create the statement object  
    		Statement stmt = con.createStatement();  
    		  
    		//step4 execute query  
    		ResultSet rs=stmt.executeQuery("select * from users");  
    		while(rs.next())  {
    			arr.add(rs.getString(2));
    			arr.add(rs.getString(3));
    		}
		}catch(Exception e){ 
			status = Response.Status.INTERNAL_SERVER_ERROR;
		}finally{
			//step5 close the connection object  
    		try {con.close();} catch (Exception e){}
		}
    	
    	String result = gson.toJson(arr);
    		  
        return Response.status(status).entity(result).header("Content-Type", "application/json").build();
    }
}
