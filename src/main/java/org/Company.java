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

    public Company[] getCompaniesByEmployee(int employee_ID){
        Connection con = null;
        try{
            DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
            con = dbpool.getConnection();
        }catch(Exception e){}

        OraclePreparedStatement stmt = null;
        try {
            stmt = (OraclePreparedStatement) con.prepareStatement(
                    "select * FROM companies");
            ResultSet i = stmt.executeQuery();


        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{stmt.close();}catch(Exception ignore){}
        }
        return null;
    }

    private void saveNewCompany(String name){
        Connection con = null;
        try{
            con = null;
            try{
                DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
                con = dbpool.getConnection();
            }catch(Exception e){};

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

    public static Company getCompanyById(int id){
        OraclePreparedStatement stmt = null;
        Connection con = null;
        Company comp = null;
        try{
            DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
            con = dbpool.getConnection();
            stmt = (OraclePreparedStatement) con.prepareStatement(
                    "select * FROM companies WHERE ID = ?");
            stmt.setInt(1, id);
            ResultSet i = stmt.executeQuery();

            if(i.next()){
                int iter = 1;

                comp = new Company(
                        Integer.parseInt(i.getString(iter++)), 	//id
                        i.getString(iter++)//name
                );

                System.out.println("db call: company ID:" + id);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{stmt.close();}catch(Exception ignore){}
            return comp;
        }
    }
}