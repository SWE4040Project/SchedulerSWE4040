package org;
import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.TIMESTAMP;
import org.DatabaseConnectionPool;
import org.Employee.Clock_State;

import java.sql.*;

/**
 * Created by Josh on 2016-11-06.
 */
public class ClockDbHandler {
    Connection con;
    public ClockDbHandler(){
        con = null;
        try{
        	DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();       	
        	con = dbpool.getConnection();
        }catch(Exception e){        }
    }
    
    public Clock_State getEmployeeClockState(int employee_id, int shift_id){
    	OraclePreparedStatement stmt = null;
    	try {
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"select emp.state from employees emp "
            		+ "join worked_shifts wrk on emp.ID = wrk.EMPLOYEE_ID "
            		+ "where emp.ID = ? and wrk.scheduled_shift_id = ?");
            stmt.setInt(1, employee_id);
            stmt.setInt(2, shift_id);
            ResultSet i = stmt.executeQuery();
            if( !i.next() ){
            	return Clock_State.NOT_CLOCKED_IN;
            }else {
            	//parse result
            	int state = i.getInt(1);
            	System.out.println("State: " + state);
            	switch(state){
            	//shift has been worked. Do not allow another clockin
            	case 0: return Clock_State.SHIFT_COMPLETE;
            	case 1: return Clock_State.CLOCKED_IN;
            	case 2: return Clock_State.BREAK;
            	default: return null;
            	}
            }
        }catch(Exception e){
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
        }
    	return null;
    }
    
    public Employee getEmployeeClockStateandWorkedShiftID(int employee_id, int shift_id){
    	OraclePreparedStatement stmt = null;
    	try {
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"select emp.state, wrk.ID from employees emp "
            		+ "join worked_shifts wrk on emp.ID = wrk.EMPLOYEE_ID "
            		+ "where emp.ID = ? and wrk.scheduled_shift_id = ?");
            stmt.setInt(1, employee_id);
            stmt.setInt(2, shift_id);
            ResultSet i = stmt.executeQuery();
            
            Employee emp = new Employee(employee_id, null);
            
            if( !i.next() ){
            	emp.setEmployeeClockState(Clock_State.NOT_CLOCKED_IN);
            }else {
            	//parse result
            	int state = i.getInt(1);
            	System.out.println("State: " + state);
            	switch(state){
            	//shift has been worked. Do not allow another clockin
            	case 0: emp.setEmployeeClockState(Clock_State.SHIFT_COMPLETE);
            	break;
            	case 1: emp.setEmployeeClockState(Clock_State.CLOCKED_IN);
            	break;
            	case 2: emp.setEmployeeClockState(Clock_State.BREAK);
            	break;
            	default: return null;
            	}
            	emp.setCurrent_worked_shift_id(i.getInt(2));
            	return emp;
            }
        }catch(Exception e){
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
        }
    	return null;
    }
    
    public boolean updateEmployeeState(int employee_id, Clock_State clockedIn) {
    	OraclePreparedStatement stmt = null;
    	try {
    		//parse result
        	int state = -1;
        	switch(clockedIn){
	        	case NOT_CLOCKED_IN: state = 0;
	        	break;
	        	case CLOCKED_IN: state = 1;
        		break;
	        	case BREAK: state = 2;
        		break;
	        	default: state = -1;
        	}
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"update employees set state = ? where ID = ?");
            stmt.setInt(1, state);
            stmt.setInt(2, employee_id);
            int i = stmt.executeUpdate();
            if (i > 0)
            	return true; //success
            	
        }catch(Exception e){
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
        }
    	return false;
	}

    public String clockInWithScheduledShift(int employee_id, int shift_id, int location_id){
    	OraclePreparedStatement stmt = null;
    	try {
    		Clock_State state = getEmployeeClockState(employee_id, shift_id);
        	if(state != Clock_State.NOT_CLOCKED_IN){
        		return "Error with employee state => clock_state "+state+", employeeId "+employee_id+", shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state.name());
        	
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"INSERT INTO worked_shifts(start_time,scheduled_shift_ID,employee_ID, location_ID) VALUES (?,?,?,?)");
            stmt.setTIMESTAMP(1, new TIMESTAMP(new Date(System.currentTimeMillis())));
            stmt.setInt(2, shift_id);
            stmt.setInt(3, employee_id);
            stmt.setInt(4, location_id);
            int i = stmt.executeUpdate();
            if (i <= 0){
            	return "Update of worked_shifts failed => employeeId "+employee_id+" and shiftId "+shift_id;
            }
        	if( !updateEmployeeState(employee_id, Clock_State.CLOCKED_IN)){
        		return "Error updating employee state => employeeId "+employee_id+" and shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state.name());
            return ""; //success
            	
        }catch(Exception e){
            return e.getMessage();
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
        }
    }
    
    public String breakInWithScheduledShift(int employee_id, int shift_id, int location_id){
    	OraclePreparedStatement stmt = null;
    	try {
    		Employee emp = getEmployeeClockStateandWorkedShiftID(employee_id, shift_id);
    		Clock_State state = emp.getEmployeeClockState();
        	if(state != Clock_State.CLOCKED_IN){
        		return "Error with employee state => clock_state "+state+", employeeId "+employee_id+", shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state.name());
        	
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"INSERT INTO breaks(start_time, worked_shift_ID)"
            		+ "VALUES (?,?)");
            stmt.setTIMESTAMP(1, new TIMESTAMP(new Date(System.currentTimeMillis())));
            stmt.setInt(2, emp.getCurrent_worked_shift_id());
            int i = stmt.executeUpdate();
            if (i <= 0){
            	return "Update of breaks failed => employeeId "+employee_id+" and shiftId "+shift_id;
            }
        	if( !updateEmployeeState(employee_id, Clock_State.BREAK)){
        		return "Error updating employee state => clock_state "+state+",employeeId "+employee_id+" and shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state.name());
            return ""; //success
            	
        }catch(Exception e){
            return e.getMessage();
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
        }
    }
    
    public String breakOutWithScheduledShift(int employee_id, int shift_id, int location_id){
    	OraclePreparedStatement stmt = null;
    	try {
    		Employee emp = getEmployeeClockStateandWorkedShiftID(employee_id, shift_id);
    		Clock_State state = emp.getEmployeeClockState();
        	if(state != Clock_State.BREAK){
        		return "Error with employee state => clock_state "+state+", employeeId "+employee_id+", shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state.name());
        	
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"update breaks set end_time = ? "
            		+ "where worked_shift_id = ?");
            stmt.setTIMESTAMP(1, new TIMESTAMP(new Date(System.currentTimeMillis())));
            stmt.setInt(2, emp.getCurrent_worked_shift_id());
            int i = stmt.executeUpdate();
            if (i <= 0){
            	return "Update of breaks failed => employeeId "+employee_id+" and shiftId "+shift_id;
            }
        	if( !updateEmployeeState(employee_id, Clock_State.CLOCKED_IN)){
        		return "Error updating employee state => clock_state "+state+",employeeId "+employee_id+" and shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state.name());
            return ""; //success
            	
        }catch(Exception e){
            return e.getMessage();
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
        }
    }


    public String clockOutWithScheduledShift(int employee_id, int shift_id, int location_id){
    	OraclePreparedStatement stmt = null;
    	try {
    		Clock_State state = getEmployeeClockState(employee_id, shift_id);
        	if(state != Clock_State.CLOCKED_IN){
        		return "Error with employee state => clock_state "+state+", employeeId "+employee_id+", shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state.name());
        	
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"update worked_shifts set end_time = ? where "
            		+ "scheduled_shift_id = ? and "
            		+ "employee_id = ? and "
            		+ "location_id = ?");
            stmt.setTIMESTAMP(1, new TIMESTAMP(new Date(System.currentTimeMillis())));
            stmt.setInt(2, shift_id);
            stmt.setInt(3, employee_id);
            stmt.setInt(4, location_id);
            int i = stmt.executeUpdate();
            if (i <= 0){
            	return "Update of worked_shifts failed => employeeId "+employee_id+" and shiftId "+shift_id;
            }
        	if( !updateEmployeeState(employee_id, Clock_State.NOT_CLOCKED_IN)){
        		return "Error updating employee state => clock_state "+state+",employeeId "+employee_id+" and shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state.name());
            return ""; //success
            	
        }catch(Exception e){
            return e.getMessage();
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
        }
    }
}
