package org;
import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.TIMESTAMP;
import org.DBVar;
import org.DatabaseConnectionPool;

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

    public String clockInWithScheduledShift(int employee_id, int shift_id, int location_id){
        try {
            OraclePreparedStatement stmt = (OraclePreparedStatement) con.prepareStatement(
                    "execute PROCEDURE_CLOCKIN(?,?,?,?,?)");
            		//"INSERT INTO worked_shifts(start_time,scheduled_shift_ID,employee_ID, location_ID) VALUES (?,?,?,?)");
            stmt.setTIMESTAMP(1, new TIMESTAMP(new Date(System.currentTimeMillis())));
            stmt.setInt(2, shift_id);
            stmt.setInt(3, employee_id);
            stmt.setInt(4, location_id);
            int i = stmt.executeUpdate();
            if (i > 0)
                return "";

        }catch(Exception e){
            return e.getMessage();
        }
        return "clockInWithScheduledShift: No rows updated.";
    }
}
