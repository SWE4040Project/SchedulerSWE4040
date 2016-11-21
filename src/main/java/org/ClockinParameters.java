package org;

public class ClockinParameters {
	
	private int employeeId;
	private int shiftId;
	private int locationId;
	private String workedNote;
	
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public int getShiftId() {
		return shiftId;
	}
	public int getLocationID() {
		return locationId;
	}
	public void setShiftId(int shiftId) {
		this.shiftId = shiftId;
	}
	public String getWorkedNote() {
		return workedNote;
	}
	public void setWorkedNote(String workedNote) {
		this.workedNote = workedNote;
	}
}
