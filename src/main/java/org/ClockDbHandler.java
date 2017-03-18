package org;
import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.TIMESTAMP;
import org.Employee.Clock_State;

import java.sql.*;

public class ClockDbHandler {

	private DatabaseConnectionPool dbpool;
    public ClockDbHandler(){
		dbpool = DatabaseConnectionPool.getInstance();
	}
    
    private Clock_State getEmployeeClockState(int employee_id, int shift_id){
    	OraclePreparedStatement stmt = null;
		Connection con = null;
		try {
			con = dbpool.getConnection();
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"select emp.state from scheduled_shifts shfts left join employees emp "
            		+ "on emp.ID = shfts.EMPLOYEES_ID "
            		+ "where emp.ID = ? and shfts.ID = ? "
            		+ "and real_start_time is not null");
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
			e.printStackTrace();
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
			try{con.close();}catch(Exception ignore){}
        }
    	return null;
    }
    
    private boolean updateEmployeeState(int employee_id, Clock_State clockedIn) {
    	OraclePreparedStatement stmt = null;
		Connection con = null;
		try {
			con = dbpool.getConnection();
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
			try{con.close();}catch(Exception ignore){}
        }
    	return false;
	}

    public String clockInWithScheduledShift(int employee_id, int shift_id, int location_id){
    	OraclePreparedStatement stmt = null;
		Connection con = null;
		try {
			con = dbpool.getConnection();
    		Clock_State state = getEmployeeClockState(employee_id, shift_id);
        	if(state != Clock_State.NOT_CLOCKED_IN){
        		return "Error with employee state => clock_state "+state+", employeeId "+employee_id+", shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state.name());
        	
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"update scheduled_shifts set real_start_time = ? where ID = ? and employees_ID = ? and location_ID = ? ");
            stmt.setTIMESTAMP(1, new TIMESTAMP(new Date(System.currentTimeMillis())));
            stmt.setInt(2, shift_id);
            stmt.setInt(3, employee_id);
            stmt.setInt(4, location_id);
            int i = stmt.executeUpdate();
            if (i <= 0){
            	return "Update of scheduled_shifts failed => employeeId "+employee_id+" and shiftId "+shift_id;
            }
        	if( !updateEmployeeState(employee_id, Clock_State.CLOCKED_IN)){
        		return "Error updating employee state => employeeId "+employee_id+" and shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state.name());
            return ""; //success
            	
        }catch(Exception e){
        	System.out.println(e.getMessage());
            return e.getMessage();
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
			try{con.close();}catch(Exception ignore){}
        }
    }
    
    public String breakInWithScheduledShift(int employee_id, int shift_id, int location_id){
    	OraclePreparedStatement stmt = null;
		Connection con = null;
		try {
			con = dbpool.getConnection();
    		Employee emp = Employee.getEmployeeById(employee_id);
			emp.setCurrent_worked_shift_id(shift_id);
    		int state = emp.getEmployeeClockState();
        	if(state != 1){
        		return "Error with employee state => clock_state "+state+", employeeId "+employee_id+", shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state);
        	
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"INSERT INTO breaks(start_time, scheduled_shift_ID)"
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
        	System.out.println("Log: Clock_State =" + state);
            return ""; //success
            	
        }catch(Exception e){
            return e.getMessage();
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
			try{con.close();}catch(Exception ignore){}
        }
    }
    
    public String breakOutWithScheduledShift(int employee_id, int shift_id, int location_id){
    	OraclePreparedStatement stmt = null;
		Connection con = null;
		try {
			con = dbpool.getConnection();
    		Employee emp = Employee.getEmployeeById(employee_id);
			emp.setCurrent_worked_shift_id(shift_id);
    		int state = emp.getEmployeeClockState();
        	if(state != 2){
        		return "Error with employee state => clock_state "+state+", employeeId "+employee_id+", shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state);
        	
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"update breaks set end_time = ? "
            		+ "where scheduled_shift_id = ?");
            stmt.setTIMESTAMP(1, new TIMESTAMP(new Date(System.currentTimeMillis())));
            stmt.setInt(2, emp.getCurrent_worked_shift_id());
            int i = stmt.executeUpdate();
            if (i <= 0){
            	return "Update of breaks failed => employeeId "+employee_id+" and shiftId "+shift_id;
            }
        	if( !updateEmployeeState(employee_id, Clock_State.CLOCKED_IN)){
        		return "Error updating employee state => clock_state "+state+",employeeId "+employee_id+" and shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state);
            return ""; //success
            	
        }catch(Exception e){
            return e.getMessage();
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
			try{con.close();}catch(Exception ignore){}
        }
    }


    public String clockOutWithScheduledShift(int employee_id, int shift_id, int location_id){
    	OraclePreparedStatement stmt = null;
		Connection con = null;
		try {
			con = dbpool.getConnection();
    		Clock_State state = getEmployeeClockState(employee_id, shift_id);
        	if(state != Clock_State.CLOCKED_IN){
        		return "Error with employee state => clock_state "+state+", employeeId "+employee_id+", shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state.name());
        	
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"update scheduled_shifts set real_end_time = ? where "
            		+ "id = ? and "
            		+ "employees_id = ? and "
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
			try{con.close();}catch(Exception ignore){}
        }
    }
    
    public String addNoteWithScheduledShift(int employee_id, int shift_id, String worked_note){
    	OraclePreparedStatement stmt = null;
		Connection con = null;
		try {
			con = dbpool.getConnection();
    		Employee emp = Employee.getEmployeeById(employee_id);
			emp.setCurrent_worked_shift_id(shift_id);
    		int state = emp.getEmployeeClockState();
        	if(state != 1){
        		return "Error with employee state => clock_state "+state+", employeeId "+employee_id+", shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State =" + state);
        	
        	//truncate if worked_note is longer than 256
        	if(worked_note.length()>256){
        		worked_note = worked_note.substring(0,255);
        	}
        	
        	System.out.println("Log: worked_note: "+ worked_note);
        	
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"update scheduled_shifts set worked_notes = ? "
            		+ "where id = ?");
            stmt.setString(1, worked_note);
            stmt.setInt(2, emp.getCurrent_worked_shift_id());
            int i = stmt.executeUpdate();
            if (i <= 0){
            	return "Update of breaks failed => employeeId "+employee_id+" and shiftId "+shift_id;
            }
        	System.out.println("Log: Clock_State =" + state);
            return ""; //success
            	
        }catch(Exception e){
            return e.getMessage();
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
			try{con.close();}catch(Exception ignore){}
        }
    }
}
