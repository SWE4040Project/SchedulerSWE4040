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
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.AuthenticateDbHandler;
import org.*;
import org.ClockDbHandler;
import org.WorkedNoteParameters;
import org.DatabaseConnectionPool;
import org.Employee;
import org.JsonVar;
import org.LoginParameters;
import org.WebTokens;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.wink.common.model.multipart.InMultiPart;
import org.apache.wink.common.internal.utils.MediaTypeUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import oracle.jdbc.OraclePreparedStatement;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.io.IOException;

@Path("/")
public class ClockinResource {

	private static final String PATH_CLOCKIN = "clockin/clockin";
	private static final String PATH_CLOCKOUT = "clockin/clockout";
	private static final String PATH_BREAKIN = "clockin/breakin";
	private static final String PATH_BREAKOUT = "clockin/breakout";
	private static final String PATH_ADDSHIFTNOTE = "clockin/addshiftnote";
	private static final String PATH_TEST_AUTH = "clockin/testauth";
	private static final String PATH_JSON = "json";
	private static final String LOGIN = "login";
	private static final String PATH_CONNECTIONS = "connections/database";
	private static final String PATH_DATABASE = "database";
	private static final String PATH_DATABASE_EDIT = "database/edit";
	private static final String PATH_DATABASE_DELETE = "database/delete";
	private static final String PATH_DATABASE_ADD = "database/add";
	private static final String CSV_PATH = "csv_upload";
	private static final String PATH_RECENT_SHIFT = "shifts/recent";
	private static final String CALENDAR_STREAM = "calendar/load";
	private static final String CALENDAR_SHIFT_APPROVE = "calendar/approve";
	private static final String EMPLOYEE_PROFILE = "employee/profile";
	private static final String PATH_CREATE_EMPLOYEE = "create/employee";
	private static final String PATH_EMPLOYEE_STATE = "employee/state";
	private static final String PATH_SEND_PUSH_NOTIFICATION = "pushnote";
	private static final String PATH_EMPLOYEE_PUSHNOTE = "employee/pushnote/edit";

	Gson gson = new Gson();


	@Path(PATH_CLOCKIN)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response clockin(@HeaderParam(JsonVar.AUTHORIZATION) String jsonWebToken, @HeaderParam(JsonVar.XSRF_TOKEN) String xsrfToken, String obj) {

		AuthenticateDbHandler auth = new AuthenticateDbHandler();
		WebTokens webTokens = new WebTokens(jsonWebToken.replace(JsonVar.BEARER, ""), xsrfToken);

		Status status = Response.Status.OK;

		Employee emp = auth.employeeFromJWT(webTokens);
		if (emp == null) {
			status = Response.Status.BAD_REQUEST;
		}

		String result = "{\"Status\":\"Employee " + emp.getId() + " is clocked in.\"}";

		Shift shift = Shift.getEligibleClockinShift(emp.getId(), emp.getCompany_id());

		ClockDbHandler clk = new ClockDbHandler();
		String error = clk.clockInWithScheduledShift(emp.getId(), shift.getId(), emp.getCompany_id());
		if (error.length() > 0) {
			status = Response.Status.INTERNAL_SERVER_ERROR;
			result = "{\"Status\":\"" + error + "\"}";
		}

		return Response.status(status).entity(result).build();
	}

	@Path(PATH_CLOCKOUT)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response clockout(@HeaderParam(JsonVar.AUTHORIZATION) String jsonWebToken, @HeaderParam(JsonVar.XSRF_TOKEN) String xsrfToken, String obj) {

		AuthenticateDbHandler auth = new AuthenticateDbHandler();
		WebTokens webTokens = new WebTokens(jsonWebToken.replace(JsonVar.BEARER, ""), xsrfToken);

		Status status = Response.Status.OK;

		Employee emp = auth.employeeFromJWT(webTokens);
		if (emp == null) {
			status = Response.Status.BAD_REQUEST;
		}

		String result = "{\"Status\":\"Employee " + emp.getId() + " is clocked out.\"}";

		Shift shift = Shift.getCurrentShift(emp.getId(), emp.getCompany_id());

		ClockDbHandler clk = new ClockDbHandler();
		String error = clk.clockOutWithScheduledShift(emp.getId(), shift.getId(), emp.getCompany_id());
		if (error.length() > 0) {
			status = Response.Status.INTERNAL_SERVER_ERROR;
			result = "{\"Status\":\"" + error + "\"}";
		}

		return Response.status(status).entity(result).build();
	}

	@Path(PATH_BREAKIN)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response breakin(@HeaderParam(JsonVar.AUTHORIZATION) String jsonWebToken, @HeaderParam(JsonVar.XSRF_TOKEN) String xsrfToken, String obj) {

		AuthenticateDbHandler auth = new AuthenticateDbHandler();
		WebTokens webTokens = new WebTokens(jsonWebToken.replace(JsonVar.BEARER, ""), xsrfToken);

		Status status = Response.Status.OK;

		Employee emp = auth.employeeFromJWT(webTokens);
		if (emp == null) {
			status = Response.Status.BAD_REQUEST;
		}

		String result = "{\"Status\":\"Employee " + emp.getId() + " is on break.\"}";

		Shift shift = Shift.getCurrentShift(emp.getId(), emp.getCompany_id());

		ClockDbHandler clk = new ClockDbHandler();
		String error = clk.breakInWithScheduledShift(emp.getId(), shift.getId());
		if (error == null || error.length() > 0) {
			status = Response.Status.INTERNAL_SERVER_ERROR;
			result = "{\"Status\":\"" + error + "\"}";
		}

		return Response.status(status).entity(result).build();
	}

	@Path(PATH_BREAKOUT)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response breakout(@HeaderParam(JsonVar.AUTHORIZATION) String jsonWebToken, @HeaderParam(JsonVar.XSRF_TOKEN) String xsrfToken, String obj) {

		AuthenticateDbHandler auth = new AuthenticateDbHandler();
		WebTokens webTokens = new WebTokens(jsonWebToken.replace(JsonVar.BEARER, ""), xsrfToken);

		Status status = Response.Status.OK;

		Employee emp = auth.employeeFromJWT(webTokens);
		if (emp == null) {
			status = Response.Status.BAD_REQUEST;
		}

		String result = "{\"Status\":\"Employee " + emp.getId() + " is off break.\"}";

		Shift shift = Shift.getCurrentShift(emp.getId(), emp.getCompany_id());

		ClockDbHandler clk = new ClockDbHandler();
		String error = clk.breakOutWithScheduledShift(emp.getId(), shift.getId());
		if (error.length() > 0) {
			status = Response.Status.INTERNAL_SERVER_ERROR;
			result = "{\"Status\":\"" + error + "\"}";
		}

		return Response.status(status).entity(result).build();
	}

	@Path(PATH_ADDSHIFTNOTE)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addnote(@HeaderParam(JsonVar.AUTHORIZATION) String jsonWebToken, @HeaderParam(JsonVar.XSRF_TOKEN) String xsrfToken, String obj) {

		AuthenticateDbHandler auth = new AuthenticateDbHandler();
		WebTokens webTokens = new WebTokens(jsonWebToken.replace(JsonVar.BEARER, ""), xsrfToken);

		Status status = Response.Status.OK;

		WorkedNoteParameters params = gson.fromJson(obj, WorkedNoteParameters.class);
		Employee emp = auth.employeeFromJWT(webTokens);
		if (emp == null) {
			status = Response.Status.BAD_REQUEST;
		}

		String result = "{\"Status\":\"Employee " + emp.getId() + " has modified their shift notes.\"}";

		Shift shift = Shift.getCurrentShift(emp.getId(), emp.getCompany_id());

		ClockDbHandler clk = new ClockDbHandler();
		String error = clk.addNoteWithScheduledShift(emp.getId(), shift.getId(), params.getWorkedNote());
		if (error.length() > 0) {
			status = Response.Status.INTERNAL_SERVER_ERROR;
			result = "{\"Status\":\"" + error + "\"}";
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

		if (params.getPassword() == null || params.getPassword().length() <= 0) {
			status = Response.Status.FORBIDDEN;
		}
		AuthenticateDbHandler auth = new AuthenticateDbHandler();
		Employee emp = auth.login(params.getUsername(), params.getPassword());
		if (emp == null) {
			status = Response.Status.FORBIDDEN;
			return Response.status(status).header("Content-Type", "application/json").build();
		}

		//create jwt
		WebTokens webTokens = auth.createJWT(emp);

		if (webTokens == null) {
			return Response.status(Status.NOT_ACCEPTABLE).entity("{\"Login\":\"Invalid\"}").header("Content-Type", "application/json").build();
		}

		System.out.println("Authorization\":\"" + webTokens.getJsonWebToken());

		String response = "{"
				+ "\"" + JsonVar.AUTHORIZATION + "\":\"" + webTokens.getJsonWebToken() + "\","
				+ "\"" + JsonVar.XSRF_TOKEN + "\":\"" + webTokens.getXsrfToken() + "\""
				+ "}";

		return Response.status(status).entity(response).header("Content-Type", "application/json")
				.header("SET-COOKIE", JsonVar.AUTHORIZATION + "=" + webTokens.getJsonWebToken()
						+ "; Path=/; HttpOnly")
				.header("SET-COOKIE", JsonVar.XSRF_TOKEN + "=" + webTokens.getXsrfToken()
						+ "; Path=/;").build();
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
	public Response getDatabaseConnectionCount() {
		return Response.status(Response.Status.OK)
				.entity(
						"{\"Active Database Connections\": \""
								+ DatabaseConnectionPool.getInstance().getActiveConnectionCount()
								+ "\"}"
				)
				.header("Content-Type", "application/json").build();
	}

	private enum DATABASE_TABLES {
		employees,
		breaks,
		companies,
		location,
		positions,
		scheduled_shifts,
		system,
		employee_locations,
		employee_positions
	}

	@Path(PATH_DATABASE)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getData(@QueryParam("table") String table) {

		Status status = Response.Status.OK;
		JsonObject dataTable = new JsonObject();
		Connection con = null;
		String result = null;
		try {

			//connect to database via connection pool
			DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
			con = dbpool.getConnection();

			//create the statement object
			Statement stmt = con.createStatement();

			//prevent sql injection
			String sqlStatement = null;
			try {
				System.out.println("table: " + table);
				DATABASE_TABLES.valueOf(table.toLowerCase());
				sqlStatement = "select * from " + table;
			} catch (IllegalArgumentException e) {
				throw new Exception("Table does not exist in the database.");
			}

			//step execute query
			ResultSet rs = stmt.executeQuery(sqlStatement);

			ResultSetMetaData rsmd = rs.getMetaData();
			int rsmdLength = rsmd.getColumnCount();
			dataTable.addProperty("columnCount", rsmdLength);
			JsonArray jaColumns = new JsonArray();
			for (int i = 0; i < rsmdLength; i++) {
				jaColumns.add(rsmd.getColumnLabel(i + 1));
			}
			dataTable.add("columns", jaColumns);
			int rowCount = 0;

			JsonArray array = new JsonArray();
			while (rs.next()) {
				rowCount++;
				JsonArray ja = new JsonArray();
				for (int i = 0; i < rsmdLength; i++) {
					ja.add(rs.getString(i + 1));
				}
				array.add(ja);
			}
			dataTable.add("rows", array);
			dataTable.addProperty("rowCount", rowCount);
			System.out.println("dataTable.toString() " + dataTable.toString());

			result = dataTable.toString();

		} catch (Exception e) {
			System.out.println("Catching exception: " + e.getMessage());
			status = Response.Status.INTERNAL_SERVER_ERROR;
			result = e.getMessage();
		} finally {
			//step5 close the connection object
			try {
				con.close();
			} catch (Exception e) {
				System.out.println("Finally: " + e.getMessage());
			}
		}
		return Response.status(status).entity(result).header("Content-Type", "application/json").build();
	}

	@Path(PATH_DATABASE_EDIT)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editData(@QueryParam("table") String table, String obj) {

		Status status = Response.Status.NO_CONTENT;
		Connection con = null;
		String result = null;
		OraclePreparedStatement stmt = null;
		try {

			//prevent sql injection
			String sqlStatement = null;
			try {
				System.out.println("table: " + table);
				DATABASE_TABLES.valueOf(table.toLowerCase());
				System.out.println("Table exists. Mapping to table now.");
			} catch (IllegalArgumentException e) {
				throw new Exception("Table does not exist in the database.");
			}

			JSONObject jsonObject = (JSONObject) new JSONParser().parse(obj);
			System.out.println("jsonObject.toJSONString() " + jsonObject.toJSONString());

			//parse column names
			JSONArray colNames = (JSONArray) jsonObject.get("columnNames");
			Iterator<String> iter = colNames.iterator();
			sqlStatement = "UPDATE " + table + " set ";
			while (iter.hasNext()) {
				sqlStatement += (String) iter.next() + " = ?";
				if (iter.hasNext()) {
					sqlStatement += ", ";
				}
			}

			//connect to database via connection pool
			DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
			con = dbpool.getConnection();

			JSONArray colData = (JSONArray) jsonObject.get("columnData");
			sqlStatement += " where ID = '" + colData.get(0) + "'";

			stmt = (OraclePreparedStatement) con.prepareStatement(sqlStatement);
			System.out.println("sqlStatement " + sqlStatement);

			//parse update data
			Iterator<String> iterData = colData.iterator();
			int countPlace = 1;
			while (iterData.hasNext()) {
				String d = iterData.next();
				try {
					System.out.println("Trying: " + d);
					int di = Integer.parseInt(d);
					stmt.setInt(countPlace, di);
				} catch (NumberFormatException nfe) {
					System.out.println("Not an integer...");
					stmt.setString(countPlace, d);
				}
				countPlace++;
			}
			int i = stmt.executeUpdate();

			if (i <= 0) {
				status = Response.Status.BAD_REQUEST;
			} else {
				status = Response.Status.OK;
			}
		} catch (ParseException pe) {
			System.out.println("Catching parse exception: " + pe.getMessage());
			status = Response.Status.INTERNAL_SERVER_ERROR;
			result = pe.getMessage();
		} catch (Exception e) {
			System.out.println("Catching exception: " + e.getMessage());
			status = Response.Status.INTERNAL_SERVER_ERROR;
			result = e.getMessage();
		} finally {
			//step5 close the connection object
			try {
				con.close();
			} catch (Exception e) {
				System.out.println("Finally: " + e.getMessage());
			}
		}
		if (result == null) {
			result = "{\"Data\":\"Ok\"}";
		}
		return Response.status(status).entity(result).header("Content-Type", "application/json").build();
	}

	@Path(PATH_DATABASE_DELETE)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteData(@QueryParam("table") String table, String obj) {

		Status status = Response.Status.NO_CONTENT;
		Connection con = null;
		String result = null;
		OraclePreparedStatement stmt = null;
		try {

			//prevent sql injection
			String sqlStatement = null;
			try {
				System.out.println("table: " + table);
				DATABASE_TABLES.valueOf(table.toLowerCase());
				System.out.println("Table exists. Mapping to table now.");
			} catch (IllegalArgumentException e) {
				throw new Exception("Table does not exist in the database.");
			}

			JSONObject jsonObject = (JSONObject) new JSONParser().parse(obj);
			System.out.println("jsonObject.toJSONString() " + jsonObject.toJSONString());


			//connect to database via connection pool
			DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
			con = dbpool.getConnection();

			sqlStatement = "delete from " + table + " where " + jsonObject.get("rowId") + " = ?";

			stmt = (OraclePreparedStatement) con.prepareStatement(sqlStatement);
			System.out.println("sqlStatement " + sqlStatement);

			try {
				int di = Integer.parseInt((String) jsonObject.get("id"));
				stmt.setInt(1, di);
			} catch (NumberFormatException nfe) {
				System.out.println("Not an integer...");
			}
			int i = stmt.executeUpdate();

			if (i <= 0) {
				status = Response.Status.BAD_REQUEST;
			} else {
				status = Response.Status.OK;
			}
		} catch (ParseException pe) {
			System.out.println("Catching parse exception: " + pe.getMessage());
			status = Response.Status.INTERNAL_SERVER_ERROR;
			result = pe.getMessage();
		} catch (Exception e) {
			System.out.println("Catching exception: " + e.getMessage());
			status = Response.Status.INTERNAL_SERVER_ERROR;
			result = e.getMessage();
		} finally {
			//step5 close the connection object
			try {
				con.close();
			} catch (Exception e) {
				System.out.println("Finally: " + e.getMessage());
			}
		}
		if (result == null) {
			result = "{\"Data\":\"Ok\"}";
		}
		return Response.status(status).entity(result).header("Content-Type", "application/json").build();
	}

	@Path(PATH_DATABASE_ADD)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addData(@QueryParam("table") String table, String obj) {

		Status status = Response.Status.NO_CONTENT;
		Connection con = null;
		String result = null;
		OraclePreparedStatement stmt = null;
		try {

			//prevent sql injection
			String sqlStatement = null;
			try {
				System.out.println("table: " + table);
				DATABASE_TABLES.valueOf(table.toLowerCase());
				System.out.println("Table exists. Mapping to table now.");
			} catch (IllegalArgumentException e) {
				throw new Exception("Table does not exist in the database.");
			}

			JSONObject jsonObject = (JSONObject) new JSONParser().parse(obj);
			System.out.println("jsonObject.toJSONString() " + jsonObject.toJSONString());

			//parse column names
			JSONArray colNames = (JSONArray) jsonObject.get("columnNames");
			Iterator<String> iter = colNames.iterator();

			/*
			 	INSERT INTO worked_shifts(start_time,scheduled_shift_ID,employee_ID, location_ID)
					VALUES ('20-NOV-16 15:54:30','1','1','1');
			 */
			sqlStatement = "INSERT INTO " + table + "(";
			String paramSpace = "";
			while (iter.hasNext()) {
				sqlStatement += (String) iter.next();
				paramSpace += "?";
				if (iter.hasNext()) {
					sqlStatement += ", ";
					paramSpace += ", ";
				}
			}
			sqlStatement += ") VALUES (" + paramSpace + ")";

			//connect to database via connection pool
			DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
			con = dbpool.getConnection();

			JSONArray colData = (JSONArray) jsonObject.get("columnData");

			stmt = (OraclePreparedStatement) con.prepareStatement(sqlStatement);
			System.out.println("sqlStatement " + sqlStatement);

			//parse update data
			Iterator<String> iterData = colData.iterator();
			int countPlace = 1;
			while (iterData.hasNext()) {
				String d = iterData.next();
				try {
					System.out.println("Trying: " + d);
					int di = Integer.parseInt(d);
					stmt.setInt(countPlace, di);
				} catch (NumberFormatException nfe) {
					System.out.println("Not an integer...");
					stmt.setString(countPlace, d);
				}
				countPlace++;
			}
			int i = stmt.executeUpdate();

			if (i <= 0) {
				status = Response.Status.BAD_REQUEST;
			} else {
				status = Response.Status.OK;
			}
		} catch (ParseException pe) {
			System.out.println("Catching parse exception: " + pe.getMessage());
			status = Response.Status.INTERNAL_SERVER_ERROR;
			result = pe.getMessage();
		} catch (Exception e) {
			System.out.println("Catching exception: " + e.getMessage());
			status = Response.Status.INTERNAL_SERVER_ERROR;
			result = e.getMessage();
		} finally {
			//step5 close the connection object
			try {
				con.close();
			} catch (Exception e) {
				System.out.println("Finally: " + e.getMessage());
			}
		}
		if (result == null) {
			result = "{\"Data\":\"Ok\"}";
		}
		return Response.status(status).entity(result).header("Content-Type", "application/json").build();
	}

	@Path(CSV_PATH)
	@POST
	@Consumes(MediaTypeUtils.MULTIPART_FORM_DATA)
	public Response csv(@CookieParam(JsonVar.AUTHORIZATION) String jsonWebToken, @CookieParam(JsonVar.XSRF_TOKEN) String xsrfToken,
						InMultiPart csv_file, @FormParam("csv_type") String csv_type) throws IOException {

		Status status = Response.Status.OK;


		if (csv_type.equals("employees")) {
			CSVHandler.importEmployees(csv_file, 1);
		} else if (csv_type.equals("shifts")) {
			CSVHandler.importShifts(csv_file, 1);
		}


		return Response.status(status).build();
	}

	@Path(CALENDAR_STREAM)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response calendarStream(@HeaderParam(JsonVar.AUTHORIZATION) String jsonWebToken, @HeaderParam(JsonVar.XSRF_TOKEN) String xsrfToken,
								   @QueryParam("start") String start, @QueryParam("end") String end, @QueryParam("employee") String emp_id, String obj) {

		Status status = Response.Status.OK;

		WebTokens webTokens = new WebTokens(jsonWebToken.replace(JsonVar.BEARER, ""), xsrfToken);
		AuthenticateDbHandler auth = new AuthenticateDbHandler();
		Employee logged_in_employee = auth.employeeFromJWT(webTokens);

		int id;
		if (emp_id == null) {
			id = -1;
		} else {
			try {
				id = Integer.parseInt(emp_id);
			} catch (Exception e) {
				System.out.println("/calendar/load : Requested Employee Id not an integer");
				id = -1;
			}
		}

		//id = -1 return calendar events for the whole company, otherwise only the specified employee's shifts are returned
		CalendarEvent[] events = CalendarEvent.getEventsForRange(start, end, logged_in_employee, id, 0);

		String jsonEvents = gson.toJson(events);
		return Response.status(status).entity(jsonEvents).build();
	}

	@Path(CALENDAR_STREAM)
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response calendarStream(@HeaderParam(JsonVar.AUTHORIZATION) String jsonWebToken, @HeaderParam(JsonVar.XSRF_TOKEN) String xsrfToken,
								   @FormParam("start") String start, @FormParam("end") String end, @QueryParam("employee") String emp_id,
								   @FormParam("viewType") String type, String obj){

		Status status = Response.Status.OK;

		WebTokens webTokens = new WebTokens(jsonWebToken.replace(JsonVar.BEARER, ""), xsrfToken);
		AuthenticateDbHandler auth = new AuthenticateDbHandler();
		Employee logged_in_employee = auth.employeeFromJWT(webTokens);

		int viewType =  Integer.valueOf(type);

		int id;
		if(emp_id == null){
			id = -1;
		}else{
			try {
				id = Integer.parseInt(emp_id);
			}catch (Exception e){
				System.out.println("/calendar/load : Requested Employee Id not an integer");
				id = -1;
			}
		}

		//id = -1 return calendar events for the whole company, otherwise only the specified employee's shifts are returned
		CalendarEvent[] events = CalendarEvent.getEventsForRange(start, end, logged_in_employee, id, viewType);

		String jsonEvents = gson.toJson(events);
		return Response.status(status).entity(jsonEvents).build();
	}

//	@Path(CALENDAR_SHIFT_APPROVE)
//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response calendarShiftApprove(@CookieParam("Authorization") String jsonWebToken, @CookieParam("xsrfToken") String xsrfToken,
//										 org.apache.sling.commons.json.JSONObject params, String obj){
//
//		Status status;
//
//		JSONObject
//
//		String start = params.getString("id");
//
//		WebTokens tokens = new WebTokens(jsonWebToken, xsrfToken);
//		AuthenticateDbHandler auth = new AuthenticateDbHandler();
////		Employee logged_in_employee = auth.employeeFromJWT(tokens);
//		Employee logged_in_employee = Employee.getEmployeeById(8);
//
//		Timestamp startTime = null;
//		Timestamp endTime = null;
//
//		if(start != null){
//			startTime = Timestamp.valueOf(start);
//		}
//		if(end != null){
//			endTime = Timestamp.valueOf(end);
//		}
//
//		boolean success = Shift.approveShift(logged_in_employee, Integer.parseInt(shift), startTime, endTime);
//
//		if(success){
//			status = Response.Status.OK;
//		}else{
//			status = Response.Status.BAD_REQUEST;
//		}
//
//		return Response.status(status).build();
//	}

	@Path(EMPLOYEE_PROFILE)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response employeeProfile(@HeaderParam(JsonVar.AUTHORIZATION) String jsonWebToken, @HeaderParam(JsonVar.XSRF_TOKEN) String xsrfToken,
									@QueryParam("employee") String employee, String obj) {

		Status status = Response.Status.OK;

		WebTokens tokens = new WebTokens(jsonWebToken.replace(JsonVar.BEARER, ""), xsrfToken);
		AuthenticateDbHandler auth = new AuthenticateDbHandler();
		Employee logged_in_employee = auth.employeeFromJWT(tokens);

		int id;
		if (employee == null) {
			id = logged_in_employee.getId();
		} else {
			try {
				id = Integer.parseInt(employee);
			} catch (Exception e) {
				System.out.println("/employee/profile : Requested Employee Id not an integer");
				id = -1;
			}
		}

		EmployeeProfile profile = EmployeeProfile.getProfile(logged_in_employee, id);

		String jsonProfile = gson.toJson(profile);

		return Response.status(status).entity(jsonProfile).header("Content-Type", "application/json").build();
	}

	@Path(PATH_EMPLOYEE_PUSHNOTE)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response setEmployeePushNotificationToken(@HeaderParam(JsonVar.AUTHORIZATION) String jsonWebToken, @HeaderParam(JsonVar.XSRF_TOKEN) String xsrfToken,
									String obj){

		AuthenticateDbHandler auth = new AuthenticateDbHandler();
		WebTokens webTokens = new WebTokens(jsonWebToken.replace(JsonVar.BEARER, ""), xsrfToken);

		Status status = Response.Status.OK;

		Employee employee = gson.fromJson(obj, Employee.class);
		employee.setId(-1); //clear employeeId if one is passed.
		//parse employeeId from jsonWebToken
		int empId = auth.getInt(JsonVar.EMPLOYEE_ID, webTokens);
		if(empId < 0){
			Response.status(Status.BAD_REQUEST).build();
		}

		String error = Employee.setEmployeePushNotificationToken(empId, employee.getPush_notification_token());

		String result = "{\"Status\":\"Employee "+ empId +" has added or modified their push notification token.\"}";

		if( error.length() > 0 ){
			status = Response.Status.INTERNAL_SERVER_ERROR;
			result = "{\"Status\":\""+ error +"\"}";
		}

		return Response.status(status).entity(result).build();
	}

	@Path(PATH_CREATE_EMPLOYEE)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEmployee(@HeaderParam(JsonVar.AUTHORIZATION) String jsonWebToken,
								   @HeaderParam(JsonVar.XSRF_TOKEN) String xsrfToken,
								   String obj) {

		Employee emp = gson.fromJson(obj, Employee.class);

		// Note: this is a static create for Symposium
		// 1. check validity of employee username
		// 2. create static employee

		if( !Employee.validateUniqueEmployeeUsername(emp.getName())){
			return Response.status(Status.NOT_ACCEPTABLE).entity(
					"{\"Error\": \" Username already exists. Please choose a new one. \"}"
			).build();
		}
		emp.setName(emp.getName());
		emp.setCompany_employee_id(emp.getName());
		emp.setCompany_id(3);

		if(emp.getPassword() != null) {
			try{
				Employee.createEmployee(emp);
				Shift.initializeDemonstrationScheduleForNewEmployee(emp);
			}catch (Exception e) {
				e.printStackTrace();
				return Response.status(Status.BAD_REQUEST).entity(
						"{\"Error\": \""+e.getMessage()+"\"}"
				).build();
			}
		}

		return Response.status(Response.Status.OK).entity(
				"{\"Success\": \"Ok\"}"
		).header("Content-Type", "application/json").build();
	}

	@Path(PATH_EMPLOYEE_STATE)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmployeeState(@HeaderParam(JsonVar.AUTHORIZATION) String jsonWebToken, @HeaderParam(JsonVar.XSRF_TOKEN) String xsrfToken){

		WebTokens webTokens = new WebTokens(jsonWebToken.replace(JsonVar.BEARER, ""), xsrfToken);
		AuthenticateDbHandler auth = new AuthenticateDbHandler();
		Employee emp = auth.employeeFromJWT(webTokens);

		if(emp == null){
			return Response.status(Status.NOT_FOUND).entity(
					"{\"state\": \"No\"}"
			).header("Content-Type", "application/json").build();
		}

		return Response.status(Status.OK).entity(
				"{\"state\": \""+ emp.getEmployeeClockState() +"\"}"
		).header("Content-Type", "application/json").build();
	}

	@Path("shifts/current")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response currentShifts(@HeaderParam(JsonVar.AUTHORIZATION) String jsonWebToken, @HeaderParam(JsonVar.XSRF_TOKEN) String xsrfToken){

		WebTokens webTokens = new WebTokens(jsonWebToken.replace(JsonVar.BEARER, ""), xsrfToken);
		AuthenticateDbHandler auth = new AuthenticateDbHandler();
		Employee emp = auth.employeeFromJWT(webTokens);

		System.out.println("Employee id: " + emp.getId() + " : company id: " + emp.getCompany_id());

		Shift shift = null;
		if(emp.getEmployeeClockState() == 0){
			shift = Shift.getEligibleClockinShift(emp.getId(),emp.getCompany_id());
		}else{
			shift = Shift.getCurrentShift(emp.getId(),emp.getCompany_id());
		}
		if(shift == null){
			return Response.status(Status.NOT_FOUND).entity(
					"{\"Success\": \"No\"}"
			).header("Content-Type", "application/json").build();
		}

		double progress_percentage = getProgressPercentage(shift);

		JSONObject json = getJsonObject(shift, progress_percentage);

		return Response.status(Response.Status.OK).entity(json.toString()).header("Content-Type", "application/json").build();
	}

	private JSONObject getJsonObject(Shift shift, double progress_percentage) {
		JSONObject json = new JSONObject();
		json.put("actual_start","--");
		json.put("actual_end","--");
		json.put("scheduled_start","--");
		json.put("scheduled_end","--");
		json.put("progress", progress_percentage);

		if (shift.getReal_start_time() != null){
			json.put("actual_start",(new SimpleDateFormat("h:mm a EEEE, MMM dd")).format(shift.getReal_start_time().getTime()));
			System.out.println("Shift real start: " + shift.getReal_start_time());
		}
		if (shift.getReal_end_time() != null) {
			json.put("actual_end", (new SimpleDateFormat("h:mm a EEEE, MMM dd")).format(shift.getReal_end_time().getTime()));
			System.out.println("Shift real end: " + shift.getReal_end_time());
		}
		if (shift.getScheduled_start_time() != null) {
			json.put("scheduled_start", (new SimpleDateFormat("h:mm a")).format(shift.getScheduled_start_time().getTime()));
			json.put("scheduled_day",(new SimpleDateFormat("EEEE, MMM dd")).format(shift.getScheduled_start_time().getTime()));
			System.out.println("Shift scheduled start: " + shift.getScheduled_start_time());
		}
		if (shift.getScheduled_end_time() != null) {
			json.put("scheduled_end", (new SimpleDateFormat("h:mm a")).format(shift.getScheduled_end_time().getTime()));
			System.out.println("Shift scheduled end: " + shift.getScheduled_end_time());
		}
		if (shift.getWorked_notes() != null) {
			json.put("shift_notes", shift.getWorked_notes());
			System.out.println("Shift shift_notes: " + shift.getWorked_notes());
		}else{
			json.put("shift_notes", "No notes");
		}
		json.put("current_time", (new SimpleDateFormat("h:mm a")).format(new java.util.Date()));
		return json;
	}

	private double getProgressPercentage(Shift shift) {
		long milliseconds1 = shift.getScheduled_start_time().getTime();
		long milliseconds2 = shift.getScheduled_end_time().getTime();
		long diff = Math.abs(milliseconds2 - milliseconds1);
		long currentmilliseconds = Calendar.getInstance().getTimeInMillis();
		long from_start = Math.abs(currentmilliseconds - milliseconds1);
		return (from_start/(double)diff) * 100;
	}

	@Path(PATH_SEND_PUSH_NOTIFICATION)
	@GET
	public Response sendPushNotification(@QueryParam("token") String jsonWebToken, @QueryParam("body") String body) {

		try {
			sendAndroidNotification(jsonWebToken, body, "Tracker");
		} catch (IOException ioe) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ioe.getMessage()).header("Content-Type", "application/json").build();
		}

		return Response.status(Response.Status.OK).entity(
				"{\"success\": \"ok\"}"
		).header("Content-Type", "application/json").build();
	}

	private void sendAndroidNotification(String deviceToken, String body, String title) throws IOException {

		String ANDROID_NOTIFICATION_URL = "https://fcm.googleapis.com/fcm/send";
		String ANDROID_NOTIFICATION_KEY = "AAAAwKa8oDw:APA91bFYMGsO_XTkVrGqjLiG5PrfgnnE6e9VWhtaWCp60Xbu-GP0ZJmiBQ-MUCam61Ho-FmyBRwZg71n9jTlyhnvwk_Zah29EADYTbELTH9IRxVtYSCz4yspeGBspIETGUoPl4eTXobI";

		OkHttpClient client = new OkHttpClient();
		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		JSONObject obj = new JSONObject();
		JSONObject msgObject = new JSONObject();
		msgObject.put("body", body);
		msgObject.put("title", title);

		obj.put("to", deviceToken);
		obj.put("notification", msgObject);

		RequestBody requestBody = RequestBody.create(mediaType, obj.toString());
		Request request = new Request.Builder().url(ANDROID_NOTIFICATION_URL).post(requestBody)
				.addHeader("content-type", "application/json")
				.addHeader("authorization", "key="+ANDROID_NOTIFICATION_KEY).build();

		okhttp3.Response response = client.newCall(request).execute();

	}

}
