package org;
/**
 * Created by Josh on 2016-11-06.
 */
public class Employee {
    private int id;
    private String name;
    private int company_employee_id;
    private int company_id;
    private boolean manager;
    private boolean super_admin;
    private int current_worked_shift_id;
    private Clock_State emp_clock_state;
    public enum Clock_State {
    	NOT_CLOCKED_IN, CLOCKED_IN, BREAK, SHIFT_COMPLETE    	
    };
    
    public Employee(int id, String name){
        this.id = id;
        this.name = name;
    }
    
    public Employee(int id, 
    		String name, 
    		int company_employee_id, 
    		int company_id, 
    		boolean manager,
    		boolean super_admin){
        this.id = id;
        this.name = name;
        this.company_employee_id = company_employee_id;
        this.company_id = company_id;
        this.manager = manager;
        this.super_admin = super_admin;
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

	public int getCompany_employee_id() {
		return company_employee_id;
	}

	public void setCompany_employee_id(int company_employee_id) {
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

	public Clock_State getEmployeeClockState() {
		return emp_clock_state;
	}

	public void setEmployeeClockState(Clock_State emp_clock_state) {
		this.emp_clock_state = emp_clock_state;
	}

	public int getCurrent_worked_shift_id() {
		return current_worked_shift_id;
	}

	public void setCurrent_worked_shift_id(int current_worked_shift_id) {
		this.current_worked_shift_id = current_worked_shift_id;
	}
}