package com.demo.cloudfunction.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.comparators.FixedOrderComparator;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class CSVUtils {
	 private static CSVUtils instance;
	 
	private CSVUtils() {};
	
	public static CSVUtils getInstance() {
		if(instance == null){
            synchronized(CSVUtils.class){
                if(instance == null){
                    instance = new CSVUtils();
                }    
            }
        } 
        return instance;
	}
	
	public <E> StatefulBeanToCsv<E> exportCSVFromList(Writer writer, String[] headerOrders, Class<E> clazz) {
		
		HeaderColumnNameMappingStrategy<E> strategy = new HeaderColumnNameMappingStrategy<>();
		strategy.setType(clazz);
		strategy.setColumnOrderOnWrite(new FixedOrderComparator<>("ID", "NAME", "EMAIL"));
		
		StatefulBeanToCsv<E> csvWriter = 
				new StatefulBeanToCsvBuilder<E>(writer)
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
				.withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withMappingStrategy(strategy)
				.withOrderedResults(true).build();
		
		return csvWriter;
	}
	
	public <E> StringWriter  writeRowsToCsvStrings(List<E> rows, FixedOrderComparator<String> fixedOrderComparator, Class<E> clazz)
	        throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

	    StringWriter writer = new StringWriter();
	    HeaderColumnNameMappingStrategy<E> strategy = new HeaderColumnNameMappingStrategy<>();
	    strategy.setType(clazz);
	    strategy.setColumnOrderOnWrite(fixedOrderComparator);

	    StatefulBeanToCsvBuilder<E> builder = new StatefulBeanToCsvBuilder(writer);
	    StatefulBeanToCsv beanWriter = builder
	              .withMappingStrategy(strategy)
	              .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
	              .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
	              .build();

	    beanWriter.write(rows);
	    
	    return writer;
	}

}
