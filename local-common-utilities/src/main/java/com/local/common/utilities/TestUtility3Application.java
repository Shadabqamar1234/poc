package com.local.common.utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestUtility3Application {

	public static void main(String[] args) throws FileNotFoundException, IOException, OpenXML4JException {
		SpringApplication.run(TestUtility3Application.class, args);

		long startTime = System.currentTimeMillis(); // Record start time

		//String filePath = "fastexcel-demo.xlsx";// set filepath of excel file
		String projectId = "";
		int chunkSize = 10 * 1024 *1024;
		String bucketName = "";
		String objectName = "";
		String credentialsPath = "";
		String filePath = "largeexcel2.xlsx";
		try {

			List<List<Object>> rows =  ExcelUtils.parseSheet(filePath);

			for (List<Object> row : rows) {
				System.out.println(row);
			}

		} catch (IOException e) {
			System.err.println("Error reading Excel file: " + e.getMessage());
		}
	
	


		long elapsedTime = System.currentTimeMillis() - startTime;

		System.out.println("Time taken to download bytes " + elapsedTime + "ms");

		

	}
}