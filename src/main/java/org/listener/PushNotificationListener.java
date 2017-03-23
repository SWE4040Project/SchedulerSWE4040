package org.listener; /**
 * Created by Brent on 2017-03-01.
 */

import com.google.android.gcm.server.*;
import oracle.jdbc.internal.OraclePreparedStatement;
import org.DatabaseConnectionPool;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ALL")
@WebListener()
public class PushNotificationListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    // Public constructor is required by servlet spec
    public PushNotificationListener() {}

    public void contextInitialized(ServletContextEvent event) {
        System.out.println("org.listener.PushNotificationListener initialized.");
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleWithFixedDelay(new SendPushNotifications(), 0, 30, TimeUnit.SECONDS);
    }

    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("org.listener.PushNotificationListener shut down.");
        scheduler.shutdownNow();
    }

    public class SendPushNotifications implements Runnable {

        public void run() {
            //query Push Notification table
            //execute push notification on each push notification
            OraclePreparedStatement stmt = null;
            Connection con = null;
            try{
                // Send push notifications here
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
                String dateString = sdf.format(cal.getTime());
                System.out.println( "sendPushNotification START - Current time: "+dateString );
                java.sql.Timestamp timestamp = timestampFromDateString(dateString);

                DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
                con = dbpool.getConnection();
                stmt = (OraclePreparedStatement) con.prepareStatement("select * from notification_table note inner join employees emp " +
                        "on note.EMP_ID = emp.ID where send_date <= ? AND sent IS NULL");

                stmt.setTimestamp(1, timestamp);
                ResultSet rs = stmt.executeQuery();

                while(rs.next()) {
                    //extract information
                    try {
                        String token = rs.getString("push_token");
                        String title = rs.getString("title");
                        String message = rs.getString("message");

                        if (token != null && token.length() > 0) {
                            sendPushNotification(token, title, message);
                        } else {
                            System.out.println("NULL token for employee "
                                    + rs.getString("emp_id")
                                    + " in row "+rs.getString("id"));
                        }
                    }finally {
                        //clear from table - only attempt once
                        try {
                            stmt = (OraclePreparedStatement) con.prepareStatement("UPDATE notification_table " +
                                    "SET sent = '1' " +
                                    "WHERE id = ? ");

                            stmt.setInt(1, rs.getInt("id"));
                            boolean i = stmt.execute();
                        }catch(SQLException e){
                            System.out.println("ERROR updating push notification table after send - " + e.getMessage());
                        }
                    }
                }

            }catch(Exception e){
                System.out.println("ERROR getFirebasePushNotificationAPIToken: " + e.getMessage());
            }finally{
                try{stmt.close();
                }catch(Exception ignore){}
                try{con.close();
                }catch(Exception ignore){}
                System.out.println("sendPushNotification END");
            }
        }

        public boolean sendPushNotification(String token, String title, String body){

            //get from SYSTEM table
            final String GCM_API_KEY = getFirebasePushNotificationAPIToken();
            final int retries = 3;
            final String notificationToken = token;
            try {
                Sender sender = new Sender(GCM_API_KEY);
                Notification notification = new Notification.Builder("myicon")
                        .title(title)
                        .body(body)
                        .build();
                Message msg = new Message.Builder()
                        .notification(notification)
                        .build();

                System.out.println("Sending " + token);

                try {
                    Result result = sender.send(msg, notificationToken, retries);

                    if (StringUtils.isEmpty(result.getErrorCodeName())) {
                        System.out.println("GCM Notification is sent successfully");
                        return true;
                    }

                    System.out.println("Error occurred while sending push notification :" + result.getErrorCodeName());
                } catch (InvalidRequestException e) {
                    System.out.println("Invalid Request" + e.getMessage());
                } catch (IOException e) {
                    System.out.println("IO Exception" + e.getMessage());
                }
            }catch (Exception e){
                System.out.println("Error: " + e.getMessage());
            }
            return false;
        }

        private String getFirebasePushNotificationAPIToken(){
            OraclePreparedStatement stmt = null;
            Connection con = null;
            try{
                DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
                con = dbpool.getConnection();
                stmt = (OraclePreparedStatement) con.prepareStatement("Select * from system");
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    return rs.getString("FIREBASE_NOTIFICATION_API_KEY");
                }

            }catch(Exception e){
                System.out.println("ERROR getFirebasePushNotificationAPIToken: " + e.getMessage());
            }finally{
                try{stmt.close();
                }catch(Exception ignore){}
                try{con.close();
                }catch(Exception ignore){}
            }
            return null;
        }
    }

    private Timestamp timestampFromDateString(String date){
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
}
