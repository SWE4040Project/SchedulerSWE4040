package org;
import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.TIMESTAMP;
import org.DBVar;

import java.sql.*;

/**
 * Created by Josh on 2016-11-06.
 */
public class ClockDbHandler {
    Connection con;
    public ClockDbHandler(){
        con = null;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection("jdbc:oracle:thin:@"+DBVar.DEV_URL+":"+ DBVar.DEV_PORT+":"+DBVar.DEV_SID,DBVar.DEV_USERNAME,DBVar.DEV_PASSWORD);

        }catch(Exception e){        }
    }

    public boolean clockInWithScheduledShift(int employee_id, int shift_id, int location_id){
        try {
            OraclePreparedStatement stmt = (OraclePreparedStatement) con.prepareStatement(
                    "INSERT INTO worked_shifts(start_time,scheduled_shift_ID,employee_ID, location_ID) VALUES (?,?,?,?)");
            stmt.setTIMESTAMP(1, new TIMESTAMP(new Date(System.currentTimeMillis())));
            stmt.setInt(2, shift_id);
            stmt.setInt(3, employee_id);
            stmt.setInt(4, location_id);
            int i = stmt.executeUpdate();
            if (i > 0)
                return true;

        }catch(Exception e){
            return false;
        }
        return false;
    }
}
