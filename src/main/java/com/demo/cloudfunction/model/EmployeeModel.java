package com.demo.cloudfunction.model;


public class EmployeeModel {
	public EmployeeModel() {};
	
	public EmployeeModel(String name, int id, String address, String email) {
		super();
		this.name = name;
		this.id = id;
		this.address = address;
		this.email = email;
	}

	String name;
	int id;
	String address;
	String email;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String exportCSV() {
		return name + "," + id + "," + address + "," + email;
	}
	
	@Override
	public String toString() {
		return "EmployeeModel [name=" + name + ", id=" + id + ", address=" + address + ", email=" + email + "]";
	}
}
