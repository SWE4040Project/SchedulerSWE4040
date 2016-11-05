package org.apache.wink.rest;

public class SimplePOJO {
	
	private String name;
	private String other;
	private int randomValue;
	
	public SimplePOJO(String name, String other, int randomValue) {
		this.name = name;
		this.other = other;
		this.randomValue = randomValue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public int getRandomValue() {
		return randomValue;
	}
	public void setRandomValue(int randomValue) {
		this.randomValue = randomValue;
	}	
}
