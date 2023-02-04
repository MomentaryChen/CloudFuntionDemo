package com.demo.cloudfunction.dao;

import java.util.List;

import com.demo.cloudfunction.model.EmployeeModel;

public interface EmployeeDao {
	List<EmployeeModel> findAll();
}
