package org;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.RAW;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.Employee.Clock_State;

import java.security.SecureRandom;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Josh on 2016-11-06.
 */
public class Employee {
    private int id;
    private String name;
    private String company_employee_id;
    private int company_id;
    private boolean manager;
    private boolean super_admin;
    private int current_worked_shift_id;
    private int emp_clock_state;
    public enum Clock_State {
    	NOT_CLOCKED_IN, CLOCKED_IN, BREAK, SHIFT_COMPLETE    	
    };
    
    public Employee(int id, String name){
        this.id = id;
        this.name = name;
    }
    
    public Employee(int id, 
    		String name, 
    		String company_employee_id,
    		int company_id, 
    		boolean manager,
    		boolean super_admin,
			int emp_clock_state){
        this.id = id;
        this.name = name;
        this.company_employee_id = company_employee_id;
        this.company_id = company_id;
        this.manager = manager;
        this.super_admin = super_admin;
		this.emp_clock_state = emp_clock_state;
    }
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany_employee_id() {
		return company_employee_id;
	}

	public void setCompany_employee_id(String company_employee_id) {
		this.company_employee_id = company_employee_id;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public boolean isManager() {
		return manager;
	}

	public void setManager(boolean manager) {
		this.manager = manager;
	}

	public boolean isSuper_admin() {
		return super_admin;
	}

	public void setSuper_admin(boolean super_admin) {
		this.super_admin = super_admin;
	}

	public int getEmployeeClockState() {
		return emp_clock_state;
	}

	public void setEmployeeClockState(int emp_clock_state) {
		this.emp_clock_state = emp_clock_state;
	}

	public int getCurrent_worked_shift_id() {
		return current_worked_shift_id;
	}

	public void setCurrent_worked_shift_id(int current_worked_shift_id) {
		this.current_worked_shift_id = current_worked_shift_id;
	}

	public boolean atSameCompany(int id){
		OraclePreparedStatement stmt = null;
		Connection con = null;
		boolean worksWith = false;
		try{
			DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
			con = dbpool.getConnection();
			stmt = (OraclePreparedStatement) con.prepareStatement(
					"select id FROM employees WHERE ID = ? and COMPANIES_ID = ?");
			stmt.setInt(1, id);
			stmt.setInt(2, getCompany_id());
			ResultSet i = stmt.executeQuery();

			if(i.next()){
				worksWith = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{stmt.close();}catch(Exception ignore){}
			return worksWith;
		}
	}

	public static Employee getEmployeeById(int id){
		OraclePreparedStatement stmt = null;
		Connection con = null;
		Employee emp = null;
		try{
			DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
			con = dbpool.getConnection();
			stmt = (OraclePreparedStatement) con.prepareStatement(
					"select * FROM employees WHERE ID = ?");
			stmt.setInt(1, id);
			ResultSet i = stmt.executeQuery();

			if(i.next()){
				int iter = 1;

				emp = new Employee(
						Integer.parseInt(i.getString(iter++)), 	//id
						i.getString(iter++),					//name
						i.getString(iter++),					//company_employee_id
						Integer.parseInt(i.getString(iter++)),	//company ID
						(Integer.parseInt(i.getString(iter++)) == 1 ) ? true : false,	//manager
						(Integer.parseInt(i.getString(iter++)) == 1 ) ? true : false,		//super admin
						Integer.parseInt(i.getString(iter++))
				);

				System.out.println("db call: employee ID:" + id);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{stmt.close();}catch(Exception ignore){}
			return emp;
		}
	}

	public void setNewPassword(String password){
		byte salt[] = newSalt();
		char char_password[] = password.toCharArray();
		byte[] hash = hashFunction(salt, char_password);

		setPassword(salt, hash);
	}

	public boolean validPassword(byte[] hashed_password, byte[] in_salt, String plain_password){

		byte salt[] = in_salt;
		char char_password[] = plain_password.toCharArray();
		byte[] hash = hashFunction(salt, char_password);

//		byte[] db_pass = hashed_password.getBytes();

		if(Arrays.equals(hash, hashed_password)){
			return true;
		}else{
			return  false;
		}
	}

	//This method is intended for inserting new employees from CSV
	//we probably shouldn't import passwords, employees should have to create one on their first login or similar
	public static boolean importFromCSV(String fullName, String emp_comp_id, int companay_id, boolean manager, int state, String web_password){

		OraclePreparedStatement stmt = null;
		Connection con = null;
		boolean success = false;
		try{
			DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
			con = dbpool.getConnection();
			stmt = (OraclePreparedStatement) con.prepareStatement(
					"INSERT  INTO EMPLOYEES (name, companies_employee_id, companies_id, manager, super_admin, state) VALUES (?,?,?,?,?,?) RETURNING ID INTO ?");

			if(state<0 || state>2){state=0;}

			stmt.setString(1, fullName);
			stmt.setString(2, emp_comp_id);
			stmt.setInt(3,companay_id);
			stmt.setBoolean(4, manager);
			stmt.setBoolean(5, false);
			stmt.setInt(6,state);
			stmt.registerReturnParameter(7, OracleTypes.NUMBER);

			int i = stmt.executeUpdate();
			if(i>0){
				ResultSet rs = stmt.getReturnResultSet();
				rs.next();
				Employee emp = Employee.getEmployeeById(rs.getInt(1));
				emp.setNewPassword(web_password);
			}

			success = true;

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{stmt.close();
			}catch(Exception ignore){}
			return success;
		}

	}

	private void setPassword(byte[] salt, byte[] pass){
		OraclePreparedStatement stmt = null;
		Connection con = null;
		try{
			DatabaseConnectionPool dbpool = DatabaseConnectionPool.getInstance();
			con = dbpool.getConnection();
			stmt = (OraclePreparedStatement) con.prepareStatement("UPDATE EMPLOYEES SET web_password = ?,salt = ? WHERE ID = ?");

			stmt.setRAW(1, new RAW(pass));
			stmt.setRAW(2, new RAW(salt));
			stmt.setInt(3,this.id);

			stmt.execute();


		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{stmt.close();
			}catch(Exception ignore){}
		}
	}

	private byte[] newSalt(){
		Random r = new SecureRandom();
		byte[] salt = new byte[32];
		r.nextBytes(salt);
		return salt;
	}

	private byte[] hashFunction(byte salt[], char[] char_password){
		byte[] hash;
		try{
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec keyspec = new PBEKeySpec(char_password,salt,50,512);
			SecretKey secret_key = skf.generateSecret(keyspec);
			hash = secret_key.getEncoded();
			return hash;

		}catch(Exception e){
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			return null;
		}
	}
}
