package org;

import oracle.jdbc.OraclePreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Josh on 2017-02-25.
 */
public class EmployeeProfile {

    String id;
    String name;
    ArrayList<String> positions;
    String company;
    ArrayList<String> managers;

    public EmployeeProfile(){
        positions = new ArrayList<String>();
        managers = new ArrayList<String>();
    }

    public void setId(int id) {
        this.id = Integer.toString(id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPosition(String position) {
        positions.add(position);
    }

    public void setComany(String company) {
        this.company = company;
    }

    public void addManager(String manager) {
        managers.add(manager);
    }

    public static EmployeeProfile getProfile(Employee emp, int id){
        EmployeeProfile prof = new EmployeeProfile();
        if(!emp.atSameCompany(id)){
            return null;
        }
        prof.createProfile(id);
        return prof;
    }

    private void createProfile(int id){
        OraclePreparedStatement stmt = null;
        Connection con = null;
        try{
            DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
            con = dbpool.getConnection();
            stmt = (OraclePreparedStatement) con.prepareStatement(
                    "select emp.id, emp.name, comp.name FROM employees emp JOIN COMPANIES comp " +
                            "ON emp.companies_id = comp.id " +
                            "WHERE emp.ID = ?");
            stmt.setInt(1, id);
            ResultSet i = stmt.executeQuery();

            if(i.next()){
                this.setId(i.getInt(1));
                this.setName(i.getString(2));
                this.setComany(i.getString(3));
            }

            stmt = (OraclePreparedStatement) con.prepareStatement(
                    "select pos.name from Employees emp\n" +
                    "LEFT JOIN employee_positions emp_pos\n" +
                    "ON emp.id = emp_pos.employees_id\n" +
                    "LEFT JOIN positions pos\n" +
                    "ON pos.id = emp_pos.positions_id\n" +
                    "WHERE emp.id = ?");

            stmt.setInt(1, id);
            i = stmt.executeQuery();

            while(i.next()){
                this.addPosition(i.getString(1));
            }

            stmt = (OraclePreparedStatement) con.prepareStatement(
                    "select emp.name from Employees emp\n" +
                            " LEFT JOIN employee_locations emp_loc \n" +
                            " ON emp.id = emp_loc.employee_id\n" +
                            " WHERE manager = 1 AND emp_loc.location_ID IN(\n" +
                            "  Select emp_locs.location_id from employees emps\n" +
                            "  LEFT JOIN employee_locations emp_locs\n" +
                            "  ON emp_locs.employee_id = emps.id\n" +
                            "  Where emps.id = ?)");

            stmt.setInt(1, id);
            i = stmt.executeQuery();

            while(i.next()){
                this.addManager(i.getString(1));
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{stmt.close();}catch(Exception ignore){}
            return;
        }
    }
}
