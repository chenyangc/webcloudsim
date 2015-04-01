package org.app.models;


public class DrawData {
	private long id;
	
	private String name;
	
	private String value;

	public DrawData(){
		
	}
	
	public DrawData(String name,String value){
		this.name=name;		
		this.value=value;
	}
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	public void setId(long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
	
	
}
