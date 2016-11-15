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
    
    public Clock_State getEmployeeClockState(int employee_id){
    	OraclePreparedStatement stmt = null;
    	try {
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"select state from employees where ID = ?");
            stmt.setInt(1, employee_id);
            ResultSet i = stmt.executeQuery();
            if (i.next()){
            	//parse result
            	int state = i.getInt(1);
            	switch(state){
            	case 0: return Clock_State.NOT_CLOCKED_IN;
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
        	if(getEmployeeClockState(employee_id) != Clock_State.NOT_CLOCKED_IN){
        		return "Duplicate clockin - duplicate employeeId "+employee_id+" and shiftId "+shift_id;
        	}
        	System.out.println("Log: Clock_State.NOT_CLOCKED_IN");
        	
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
        	System.out.println("Log: Clock_State.CLOCKED_IN");
            return ""; //success
            	
        }catch(Exception e){
            return e.getMessage();
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
        }
    }
}
