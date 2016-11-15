package org;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import java.sql.*;

/**
 * Created by Josh on 2016-11-05.
 */
public class Company {
    private int id;
    private String name;

    //Constructor used for creating objects from db tables
    public Company(int compId, String compName){
        id = compId;
        name = compName;
    }

    //constructor for new companies
    public Company(String compName){
        saveNewCompany(compName);
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }


    private void saveNewCompany(String name){
        Connection con = null;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection("jdbc:oracle:thin:@"+DBVar.DEV_URL+":"+DBVar.DEV_PORT+":"+DBVar.DEV_SID,DBVar.DEV_USERNAME,DBVar.DEV_PASSWORD);

            OraclePreparedStatement stmt = (OraclePreparedStatement)con.prepareStatement("INSERT INTO companies(name) VALUES (?) RETURNING ID INTO ?");
            stmt.setString(1,name);
            stmt.registerReturnParameter(2, OracleTypes.NUMBER);
            int i = stmt.executeUpdate();
            if(i>0) {
                ResultSet rs = stmt.getReturnResultSet();
                rs.next();
                this.name = name;
                id = rs.getInt(1);
            }
        }catch(Exception e){
        }finally{
            //close the connection object
            try {
                con.close();
            } catch (Exception e){
            }
        }
    }
}