package org;

import oracle.jdbc.OraclePreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Josh on 2017-02-11.
 */
public class CalendarEvent {
    private int id;
    private int employee_id;
    private String title;
    private String allDay;
    private String start;
    private String end;
    private String color;
    private String note;
    private String editable;
    private String real_start;
    private String real_end;

    public CalendarEvent(int id, int employee_id, String title, boolean allDay, Timestamp start, Timestamp end, boolean available,
                         Timestamp approved_start, Timestamp real_start, Timestamp real_end, String notes){
        this.id = id;
        this.employee_id = employee_id;
        this.title = (title == null)? "Available" : title;
        this.title = this.title.concat("  "+hours(start, end));
        this.allDay = allDay? "true" : "false";
        this.start = getISO8601StringForDate(start);
        this.end = getISO8601StringForDate(end);
        this.real_start = getISO8601StringForDate(real_start);
        this.real_end = getISO8601StringForDate(real_end);
        if(available){
            this.color = "grey";
        }else{
            if(approved_start == null){
                this.color = "orange";
            }else{
                this.color = "green";
            }
        } this.note = notes;
        this.editable = "true";
    }

    public static CalendarEvent[] getEventsForRange(String start, String end, Employee emp){
        Timestamp startDate = timestampFromDateString(start);
        Timestamp endDate = timestampFromDateString(end);
        CalendarEvent[] events = CalendarEvent.getShiftsForDateRange(startDate, endDate, emp.getCompany_id(), -1);
        return  events;
    }

    private String getISO8601StringForDate(Date date) {
        if(date == null){
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Halifax"));
        return dateFormat.format(date);
    }

    private String hours(Timestamp start, Timestamp end){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String ret = start.toLocalDateTime().format(formatter) + " - " + end.toLocalDateTime().format(formatter);
        return ret;
    }

    private static Timestamp timestampFromDateString(String date){
        String[] dateVals = date.split("-");
        LocalDateTime dateTime = LocalDateTime.of(Integer.valueOf(dateVals[0]),Integer.valueOf(dateVals[1]),Integer.valueOf(dateVals[2]),0, 0, 0);
        return Timestamp.valueOf(dateTime);
    }

    private static CalendarEvent[] getShiftsForDateRange(Timestamp start, Timestamp end, int company_id, int employee_id){
        OraclePreparedStatement stmt = null;
        Connection con = null;
        ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();
        try{
            DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
            con = dbpool.getConnection();
            CalendarEvent event = null;
            String query = "select shift.*, emp.NAME FROM scheduled_shifts shift " +
                    "LEFT JOIN employees emp ON shift.employees_ID = emp.ID " +
                    "WHERE shift.COMPANY_ID = ? AND shift.SCHEDULED_START_TIME >= ? AND shift.SCHEDULED_START_TIME <= ?";
            if(employee_id != -1){
                query = query.concat(" AND EMPLOYEES_ID = ?");
            }
            stmt = (OraclePreparedStatement) con.prepareStatement(query);
            stmt.setInt(1, company_id);
            stmt.setTimestamp(2, start);
            stmt.setTimestamp(3, end);
            if(employee_id != -1){
                stmt.setInt(4,employee_id);
            }
            ResultSet i = stmt.executeQuery();

            while(i.next()){

                event = new CalendarEvent(
                        i.getInt("ID"), //ID
                        i.getInt("employees_id"),
                        i.getString("NAME"),
                        true,
                        i.getTimestamp("scheduled_start_time"), //scheduled_start
                        i.getTimestamp("scheduled_end_time"), //scheduled_end
                        i.getBoolean("available")? true : false,   //available
                        i.getTimestamp("approved_start_time"), //approved_start
                        i.getTimestamp("real_start_time"),
                        i.getTimestamp("real_end_time"),
                        i.getString("worked_notes") //worded_notes
                );
                events.add(event);

            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{stmt.close();}catch(Exception ignore){}
            CalendarEvent[] eventsArray = new CalendarEvent[events.size()];
            eventsArray = events.toArray(eventsArray);
            return eventsArray;
        }
    }
}
