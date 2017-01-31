package org;

import oracle.jdbc.OraclePreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * Created by Josh on 2017-01-14.
 */
public class Shift {

    //This method is intended for inserting new shifts from CSV
    public static boolean importFromCSV(int emp_id, int location_id, Timestamp scheduled_start, Timestamp scheduled_end, Timestamp real_start,
                                        Timestamp real_end, Timestamp approved_start, Timestamp approved_end, int available, String shift_notes){

    	OraclePreparedStatement stmt = null;
        Connection con = null;
        boolean success = false;
        try{
            DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
            con = dbpool.getConnection();
            stmt = (OraclePreparedStatement) con.prepareStatement(
                    "INSERT  INTO SCHEDULED_SHIFTS (employees_ID, location_ID, scheduled_start_time, scheduled_end_time, real_start_time," +
                            "real_end_time, approved_start_time, approved_end_time, available, worked_notes) VALUES (?,?,?,?,?,?,?,?,?,?)");

            stmt.setInt(1, emp_id);
            stmt.setInt(2, location_id);
            stmt.setTimestamp(3,scheduled_start);
            stmt.setTimestamp(4,scheduled_end);
            stmt.setTimestamp(5,real_start);
            stmt.setTimestamp(6,real_end);
            stmt.setTimestamp(7,approved_start);
            stmt.setTimestamp(8,approved_end);
            stmt.setInt(9,available);
            stmt.setString(10,shift_notes);

            int i = stmt.executeUpdate();

            success = true;

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{stmt.close();
            }catch(Exception ignore){}
        }
        return success;
    }
}
