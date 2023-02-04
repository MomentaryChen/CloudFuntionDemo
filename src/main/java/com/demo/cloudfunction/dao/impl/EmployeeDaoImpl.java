package com.demo.cloudfunction.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.demo.cloudfunction.dao.EmployeeDao;
import com.demo.cloudfunction.mapper.EmployeeRowMapper;
import com.demo.cloudfunction.model.EmployeeModel;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

	@Autowired
	NamedParameterJdbcTemplate template;

	@Override
	public List<EmployeeModel> findAll() {
		return template.query("select * from employee", new EmployeeRowMapper());
	}

}