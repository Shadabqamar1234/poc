package com.local.common.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.github.pjfanning.xlsx.StreamingReader;

public final class ExcelUtils {

	public static List<List<Object>> parseSheet(String filePath) throws IOException {

		try {
			if (filePath.endsWith(".xlsx")) {
				return parseXlsx(filePath);
			} else if (filePath.endsWith(".xls")) {
				return parseXls(filePath);
			} else if (filePath.endsWith(".csv")) {
				return parseCsv(filePath);
			} else {
				System.err.println("Unsupported file type");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static List<List<Object>> parseXlsx(String filePath) throws IOException {
		System.out.println("Inside parseXlsx ");
		System.out.println("###########Excel parsing started ");
		InputStream is = new FileInputStream(new File(filePath));

		Workbook workbook = StreamingReader
				.builder()
				.rowCacheSize(100)
				.bufferSize(1024)
				.open(is);

		List<List<Object>> rows = new ArrayList<>();

		Sheet sheet = workbook.getSheetAt(0);

			for (Row row : sheet) {
				System.out.println( "Parsing at row num :"+row.getRowNum());
				List<Object> rowData = new ArrayList<>();
				for (Cell cell : row) {
					rowData.add(getCellValue(cell));
				}
				rows.add(rowData);
			}
		

		workbook.close();
		System.out.println("###########Excel parsing completed ");

		return rows;

	}

	private static List<List<Object>> parseCsv(String filePath) throws FileNotFoundException, IOException {
		System.out.println("Inside parseCsv ");
		System.out.println("###########Excel parsing started ");

		List<List<Object>> cellData = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split(",");
				List<Object> rowData = new ArrayList<>();
				for (String field : fields) {
					rowData.add(field);
				}
				cellData.add(rowData);
			}
		}
		System.out.println("###########Excel parsing completed ");

		return cellData;
	}

	private static List<List<Object>> parseXls(String filePath) throws IOException {
		System.out.println("Inside parseXls ");

		System.out.println("###########Excel parsing started ");

		List<List<Object>> rows = new ArrayList<>();

		try (InputStream inputStream = new FileInputStream(filePath);
				Workbook workbook = new HSSFWorkbook(inputStream);) {
			Sheet sheet = workbook.getSheetAt(0);

			for (Row row : sheet) {

				List<Object> rowData = new ArrayList<>();

				for (Cell cell : row) {
					rowData.add(cell.toString());
				}
				rows.add(rowData);
			}

			workbook.close();

		}
		System.out.println("###########Excel parsing completed ");

		return rows;

	}

	private static Object getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case NUMERIC:
			return cell.getNumericCellValue();
		case STRING:
			return cell.getStringCellValue();
		case BOOLEAN:
			return cell.getBooleanCellValue();
		case BLANK:
			return null;
		default:
			return null;
		}
	}

}
