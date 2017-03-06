package org;

import oracle.jdbc.OraclePreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.ArrayList;

/**
 * Created by Josh on 2017-01-14.
 */
public class Shift {
    private int id;
    private int employee_id;
    private int location_id;
    private int company_id;
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

    private Shift(int id, int employee_id, int location_id, int company_id, Timestamp scheduled_start_time, Timestamp scheduled_end_time, Timestamp real_start_time, Timestamp real_end_time, Timestamp approved_start_time, Timestamp approved_end_time, boolean available, String worked_notes) {
        this.id = id;
        this.employee_id = employee_id;
        this.location_id = location_id;
        this.company_id = location_id;
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

    public int getCompany_id() {
        return company_id;
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

    public static boolean approveShift(Employee emp, int shift_id, Timestamp start, Timestamp end){
        boolean success = false;

        OraclePreparedStatement stmt = null;
        Connection con = null;
        try{
            DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
            con = dbpool.getConnection();
            String query = "UPDATE SCHEDULED_SHIFTS SET approved = 1" ;

            if(start == null){
                query = query.concat(", approved_start_time = scheduled_start_time");
            }else{
                query = query.concat(", approved_start_time = ?");
            }

            if(end == null){
                query = query.concat(", approved_end_time = scheduled_end_time");
            }else{
                query = query.concat(", approved_end_time = ?");
            }

            query = query.concat(" WHERE ID = ? AND company_id = ?");

            stmt = (OraclePreparedStatement) con.prepareStatement(query);

            int iter = 1;

            if(start != null){
                stmt.setTimestamp(iter++, start);
            }
            if(end != null){
                stmt.setTimestamp(iter++, end);
            }
            stmt.setInt(iter++,shift_id);
            stmt.setInt(iter++,emp.getCompany_id());

            stmt.execute();

        }catch(Exception e){
            e.printStackTrace();
            success = false;
        }finally{
            try{stmt.close();
            }catch(Exception ignore){}
        }

        return success;
    }

    public static Shift getRecentShiftById(int id,int company_id){
        OraclePreparedStatement stmt = null;
        Connection con = null;
        Shift recentShift = null;

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        cal.add(Calendar.HOUR, -12);
        String yesterdayTimeStampString = dateFormat.format(cal.getTime());
        Timestamp yesterdayTimeStamp = timestampFromDateString(yesterdayTimeStampString);
        System.out.println("Timestamp: "+yesterdayTimeStamp);

        try{
            DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
            con = dbpool.getConnection();
            stmt = (OraclePreparedStatement) con.prepareStatement(
                    "select * from SCHEDULED_SHIFTS where COMPANY_ID = ? and SCHEDULED_START_TIME >= ? and EMPLOYEES_ID = ?");
            stmt.setInt(1,company_id);
            stmt.setTimestamp(2, yesterdayTimeStamp);
            stmt.setInt(3, id);
            ResultSet i = stmt.executeQuery();

            if(i.next()){
                recentShift = new Shift(
                        i.getInt("ID"), //ID
                        i.getInt("employees_ID"), //employee ID
                        i.getInt("location_ID"), //location_ID
                        i.getInt("company_id"), //location_ID
                        i.getTimestamp("scheduled_start_time"), //scheduled_start
                        i.getTimestamp("scheduled_end_time"), //scheduled_end
                        i.getTimestamp("real_start_time"), //real_start
                        i.getTimestamp("real_end_time"), //real_end
                        i.getTimestamp("approved_start_time"), //approved_start
                        i.getTimestamp("approved_end_time"), //approved_end
                        i.getBoolean("available"),   //available
                        i.getString("worked_notes") //worked_notes
                );
                System.out.println("shift db call: employee ID:" + id);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{stmt.close();}catch(Exception ignore){}
            return recentShift;
        }
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
                        i.getInt("ID"), //ID
                        i.getInt("employees_ID"), //employee ID
                        i.getInt("location_ID"), //location_ID
                        i.getInt("company_id"), //location_ID
                        i.getTimestamp("scheduled_start_time"), //scheduled_start
                        i.getTimestamp("scheduled_end_time"), //scheduled_end
                        i.getTimestamp("real_start_time"), //real_start
                        i.getTimestamp("real_end_time"), //real_end
                        i.getTimestamp("approved_start_time"), //approved_start
                        i.getTimestamp("approved_end_time"), //approved_end
                        i.getBoolean("available"),   //available
                        i.getString("worked_notes") //worked_notes
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
    public static boolean importFromCSV(int emp_id, int location_id, int company_id, Timestamp scheduled_start, Timestamp scheduled_end, Timestamp real_start,
                                        Timestamp real_end, Timestamp approved_start, Timestamp approved_end, int available, String shift_notes){

    	OraclePreparedStatement stmt = null;
        Connection con = null;
        boolean success = false;
        try{
            DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
            con = dbpool.getConnection();
            stmt = (OraclePreparedStatement) con.prepareStatement(
                    "INSERT  INTO SCHEDULED_SHIFTS (employees_ID, location_ID, company_ID, scheduled_start_time, scheduled_end_time, real_start_time," +
                            "real_end_time, approved_start_time, approved_end_time, available, worked_notes) VALUES (?,?,?,?,?,?,?,?,?,?,?)");

            stmt.setInt(1, emp_id);
            stmt.setInt(2, location_id);
            stmt.setInt(3, company_id);
            stmt.setTimestamp(4,scheduled_start);
            stmt.setTimestamp(5,scheduled_end);
            stmt.setTimestamp(6,real_start);
            stmt.setTimestamp(7,real_end);
            stmt.setTimestamp(8,approved_start);
            stmt.setTimestamp(9,approved_end);
            stmt.setInt(10,available);
            stmt.setString(11,shift_notes);

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

    private static Timestamp timestampFromDateString(String date){
        String[] dateVals = date.split("-");
        LocalDateTime dateTime = LocalDateTime.of(
            Integer.valueOf(dateVals[0]),
            Integer.valueOf(dateVals[1]),
            Integer.valueOf(dateVals[2]),
            Integer.valueOf(dateVals[3]),
            Integer.valueOf(dateVals[4]),
            Integer.valueOf(dateVals[5]));
        return Timestamp.valueOf(dateTime);
    }

    public static void initializeDemonstrationScheduleForNewEmployee(Employee emp) throws Exception {

        OraclePreparedStatement stmt = null;
        Connection con = null;
        boolean success = false;
        try{
            DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
            con = dbpool.getConnection();

            stmt = (OraclePreparedStatement) con.prepareStatement(
                    "SELECT * FROM EMPLOYEES where COMPANIES_EMPLOYEE_ID = ?");
            stmt.setString(1,emp.getCompany_employee_id());
            ResultSet rs = stmt.executeQuery();
            int emp_id = -1;
            if(rs.next()){
                emp_id = rs.getInt("id");
            }

            final int LOCATION = 2;
            final int COMPANY = 3;

            stmt = (OraclePreparedStatement) con.prepareStatement(
                    "INSERT ALL " +
                            "INTO SCHEDULED_SHIFTS (employees_ID, location_ID, company_ID, scheduled_start_time, scheduled_end_time) " +
                            "VALUES (?,?,?,?,?) " +
                            "INTO SCHEDULED_SHIFTS (employees_ID, location_ID, company_ID, scheduled_start_time, scheduled_end_time) " +
                            "VALUES (?,?,?,?,?) " +
                            "INTO SCHEDULED_SHIFTS (employees_ID, location_ID, company_ID, scheduled_start_time, scheduled_end_time) " +
                            "VALUES (?,?,?,?,?) " +
                            "INTO SCHEDULED_SHIFTS (employees_ID, location_ID, company_ID, scheduled_start_time, scheduled_end_time) " +
                            "VALUES (?,?,?,?,?) " +
                            "INTO SCHEDULED_SHIFTS (employees_ID, location_ID, company_ID, scheduled_start_time, scheduled_end_time) " +
                            "VALUES (?,?,?,?,?) " +
                            "INTO SCHEDULED_SHIFTS (employees_ID, location_ID, company_ID, scheduled_start_time, scheduled_end_time) " +
                            "VALUES (?,?,?,?,?) " +
                            "INTO SCHEDULED_SHIFTS (employees_ID, location_ID, company_ID, scheduled_start_time, scheduled_end_time) " +
                            "VALUES (?,?,?,?,?) " +
                            "INTO SCHEDULED_SHIFTS (employees_ID, location_ID, company_ID, scheduled_start_time, scheduled_end_time) " +
                            "VALUES (?,?,?,?,?) " +
                            "INTO SCHEDULED_SHIFTS (employees_ID, location_ID, company_ID, scheduled_start_time, scheduled_end_time) " +
                            "VALUES (?,?,?,?,?) " +
                            "SELECT 1 FROM DUAL "); //junk query statement as necessary for INSERT ALL syntax

            //current shift
            stmt.setInt(1, emp_id);
            stmt.setInt(2, LOCATION);
            stmt.setInt(3, COMPANY);
            stmt.setTimestamp(4, getTimestampAlteredByDayAmount(0));
            stmt.setTimestamp(5, getTimestampAlteredByDayAmountAndSetHour(0,18));

            //one day past
            stmt.setInt(6, emp_id);
            stmt.setInt(7, LOCATION);
            stmt.setInt(8, COMPANY);
            stmt.setTimestamp(9, getTimestampAlteredByDayAmountAndSetHour(-1,9));
            stmt.setTimestamp(10, getTimestampAlteredByDayAmountAndSetHour(-1,17));

            //two day past
            stmt.setInt(11, emp_id);
            stmt.setInt(12, LOCATION);
            stmt.setInt(13, COMPANY);
            stmt.setTimestamp(14, getTimestampAlteredByDayAmountAndSetHour(-2,7));
            stmt.setTimestamp(15, getTimestampAlteredByDayAmountAndSetHour(-2,15));

            //three day past
            stmt.setInt(16, emp_id);
            stmt.setInt(17, LOCATION);
            stmt.setInt(18, COMPANY);
            stmt.setTimestamp(19, getTimestampAlteredByDayAmountAndSetHour(-3,9));
            stmt.setTimestamp(20, getTimestampAlteredByDayAmountAndSetHour(-3,17));

            //four day past
            stmt.setInt(21, emp_id);
            stmt.setInt(22, LOCATION);
            stmt.setInt(23, COMPANY);
            stmt.setTimestamp(24, getTimestampAlteredByDayAmountAndSetHour(-4,12));
            stmt.setTimestamp(25, getTimestampAlteredByDayAmountAndSetHour(-4,18));

            //one day future
            stmt.setInt(26, emp_id);
            stmt.setInt(27, LOCATION);
            stmt.setInt(28, COMPANY);
            stmt.setTimestamp(29, getTimestampAlteredByDayAmountAndSetHour(1,8));
            stmt.setTimestamp(30, getTimestampAlteredByDayAmountAndSetHour(1,16));

            //two day future
            stmt.setInt(31, emp_id);
            stmt.setInt(32, LOCATION);
            stmt.setInt(33, COMPANY);
            stmt.setTimestamp(34, getTimestampAlteredByDayAmountAndSetHour(2,8));
            stmt.setTimestamp(35, getTimestampAlteredByDayAmountAndSetHour(2,16));

            //three day future
            stmt.setInt(36, emp_id);
            stmt.setInt(37, LOCATION);
            stmt.setInt(38, COMPANY);
            stmt.setTimestamp(39, getTimestampAlteredByDayAmountAndSetHour(3,8));
            stmt.setTimestamp(40, getTimestampAlteredByDayAmountAndSetHour(3,16));

            //five day future
            stmt.setInt(41, emp_id);
            stmt.setInt(42, LOCATION);
            stmt.setInt(43, COMPANY);
            stmt.setTimestamp(44, getTimestampAlteredByDayAmountAndSetHour(5,12));
            stmt.setTimestamp(45, getTimestampAlteredByDayAmountAndSetHour(5,20));

            int i = stmt.executeUpdate();

            success = true;

        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally{
            try{stmt.close();}catch(Exception ignore){}
            try{con.close();}catch(Exception ignore){}
        }
    }

    private static Timestamp getTimestampAlteredByDayAmount(int days){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String currentTimeStampString = dateFormat.format(cal.getTime());
        return timestampFromDateString(currentTimeStampString);
    }

    private static Timestamp getTimestampAlteredByDayAmountAndSetHour(int days, int hour){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, days);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String currentTimeStampString = dateFormat.format(cal.getTime());
        return timestampFromDateString(currentTimeStampString);
    }
}
