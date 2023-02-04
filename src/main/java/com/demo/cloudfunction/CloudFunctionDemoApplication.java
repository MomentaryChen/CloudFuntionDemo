package com.demo.cloudfunction;


import java.io.IOException;
import java.io.StringWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.collections4.comparators.FixedOrderComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.demo.cloudfunction.config.GCSConfig;
import com.demo.cloudfunction.dao.EmployeeDao;
import com.demo.cloudfunction.model.EmployeeModel;
import com.demo.cloudfunction.service.CloudStorageService;
import com.demo.cloudfunction.utils.CSVUtils;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

@SpringBootApplication
public class CloudFunctionDemoApplication {
	
	@Autowired
	EmployeeDao employeeDao;
	
	@Autowired
	CloudStorageService gcsService;
	
	@Autowired
	GCSConfig gcsConfig;

	CSVUtils csvUtils = CSVUtils.getInstance();
	
	public static void main(String[] args) {
		SpringApplication.run(CloudFunctionDemoApplication.class, args);
	}
	
	@Bean
	public Function<String, String> function() {
		List<EmployeeModel> employees = employeeDao.findAll();
		
		Instant nowInstant = Instant.now();
		ZoneId taipeiZoneId = ZoneId.of("Asia/Taipei");
		DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
		ZonedDateTime zdt = ZonedDateTime.ofInstant(nowInstant, taipeiZoneId);
		
		String objectName = "employees/employees-" + zdt.format(DTF).toString()  + ".csv";
		
		final String msg = "上傳成功";
		try {
			FixedOrderComparator<String> fixedOrderComparator = new FixedOrderComparator<>("ID", "NAME", "ADDRESS", "EMAIL");
			StringWriter writer = csvUtils.writeRowsToCsvStrings(employees, fixedOrderComparator, EmployeeModel.class );
			gcsService.uploadObjectFromMemory(gcsConfig.bucketName, objectName, writer.toString());
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
		
		return value -> msg;
	}

}
