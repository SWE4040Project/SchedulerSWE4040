package org;

import oracle.jdbc.OraclePreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by Josh on 2017-01-14.
 */
public class Shift {
    private int id;
    private int employee_id;
    private int location_id;
    private Timestamp scheduled_start_time;
    private Timestamp scheduled_end_time;
    private Timestamp real_start_time;
    private Timestamp real_end_time;
    private Timestamp approved_start_time;
    private Timestamp approved_end_time;
    private boolean available;
    private Time length;
    private String worked_notes;

    public Shift(){

    }

    public Shift(int location_id, Timestamp scheduled_start_time, Timestamp scheduled_end_time, Timestamp real_start_time, Timestamp real_end_time, Timestamp approved_start_time, Timestamp approved_end_time, boolean available, String worked_notes, int employee_id, Time length) {
        this.location_id = location_id;
        this.scheduled_start_time = scheduled_start_time;
        this.scheduled_end_time = scheduled_end_time;
        this.real_start_time = real_start_time;
        this.real_end_time = real_end_time;
        this.approved_start_time = approved_start_time;
        this.approved_end_time = approved_end_time;
        this.available = available;
        this.worked_notes = worked_notes;
        this.employee_id = employee_id;
        this.length = length;
    }

    private Shift(int id, int employee_id, int location_id, Timestamp scheduled_start_time, Timestamp scheduled_end_time, Timestamp real_start_time, Timestamp real_end_time, Timestamp approved_start_time, Timestamp approved_end_time, boolean available, String worked_notes) {
        this.id = id;
        this.employee_id = employee_id;
        this.location_id = location_id;
        this.scheduled_start_time = scheduled_start_time;
        this.scheduled_end_time = scheduled_end_time;
        this.real_start_time = real_start_time;
        this.real_end_time = real_end_time;
        this.approved_start_time = approved_start_time;
        this.approved_end_time = approved_end_time;
        this.available = available;
        this.length = length;
        this.worked_notes = worked_notes;
    }

    public int getId() {
        return id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public int getLocation_id() {
        return location_id;
    }

    public Timestamp getScheduled_start_time() {
        return scheduled_start_time;
    }

    public Timestamp getScheduled_end_time() {
        return scheduled_end_time;
    }

    public Timestamp getReal_start_time() {
        return real_start_time;
    }

    public Timestamp getReal_end_time() {
        return real_end_time;
    }

    public Timestamp getApproved_start_time() {
        return approved_start_time;
    }

    public Timestamp getApproved_end_time() {
        return approved_end_time;
    }

    public boolean isAvailable() {
        return available;
    }

    public Time getLength() {
        return length;
    }

    public String getWorked_notes() {
        return worked_notes;
    }

    public static Shift getShiftById(int id){
        OraclePreparedStatement stmt = null;
        Connection con = null;
        Shift shift = null;
        try{
            DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
            con = dbpool.getConnection();
            stmt = (OraclePreparedStatement) con.prepareStatement(
                    "select * FROM scheduled_shifts WHERE ID = ?");
            stmt.setInt(1, id);
            ResultSet i = stmt.executeQuery();

            if(i.next()){
                int iter = 1;

                shift = new Shift(
                        Integer.parseInt(i.getString(iter++)), //ID
                        Integer.parseInt(i.getString(iter++)), //employee ID
                        Integer.parseInt(i.getString(iter++)), //location_ID
                        i.getTimestamp(iter++), //scheduled_start
                        i.getTimestamp(iter++), //scheduled_end
                        i.getTimestamp(iter++), //real_start
                        i.getTimestamp(iter++), //real_end
                        i.getTimestamp(iter++), //approved_start
                        i.getTimestamp(iter++), //approved_end
                        i.getBoolean(iter++),   //available
                        i.getString(iter++) //worked_notes
                );

                System.out.println("db call: employee ID:" + id);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{stmt.close();}catch(Exception ignore){}
            try{con.close();}catch(Exception ignore){}
        }
        return shift;
    }


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
            try{stmt.close();}catch(Exception ignore){}
            try{con.close();}catch(Exception ignore){}
        }
        return success;
    }
}
