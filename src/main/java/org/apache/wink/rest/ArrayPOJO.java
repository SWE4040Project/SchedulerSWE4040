package org.apache.wink.rest;

import java.util.ArrayList;

public class ArrayPOJO {
	ArrayList<String> array;
	
	public ArrayPOJO(){
		array = new ArrayList<String>();
	}
	
	public void add(String i){
		array.add(i);
	}
	public String get(int index){
		return array.get(index);
	}
}
