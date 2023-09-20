package no.uit.syntHIR.dataWrangling.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class DataWranglingUtil {
	
	/**
	 * Read CSV and convert it onto list of beans
	 * 
	 * @param <T>
	 * @param csvFileLocation
	 * @param className
	 * @param list
	 * @return List of objects of type 'T' (Generic)
	 */
	public static <T> List<T> readCsvToBeanList(String csvFileLocation, Class<T> className, List<T> list) {
	    
		HeaderColumnNameMappingStrategy<T> ms = new HeaderColumnNameMappingStrategy<T>();
	    ms.setType(className);

	    try {
			CsvToBean<T> cb = new CsvToBeanBuilder<T>(new FileReader(csvFileLocation))
	                .withType(className)
	                .withMappingStrategy(ms)
	                .build();

	         list = cb.parse();
	    }catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    return list;
	}
	
	public static <T> List<T> readCsvToBeanList(BufferedReader fileBufferedReader, Class<T> className, List<T> list) {
	    
		HeaderColumnNameMappingStrategy<T> ms = new HeaderColumnNameMappingStrategy<T>();
	    ms.setType(className);

	    CsvToBean<T> cb = new CsvToBeanBuilder<T>(fileBufferedReader)
		        .withType(className)
		        .withMappingStrategy(ms)
		        .build();

		 list = cb.parse();
		 
	    return list;
	}

	public static <T> File readBeanToCSV(String csvFileLocation, Class<T> className, List<T> list) {
	
		HeaderColumnNameMappingStrategy<T> mappingStrategy = new HeaderColumnNameMappingStrategy<T>();
		mappingStrategy.setType(className);
	    
		try {
			Writer fileWriter = Files.newBufferedWriter(Paths.get(csvFileLocation));
			
			StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(fileWriter)
					.withMappingStrategy(mappingStrategy)
                   // .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
			
			beanToCsv.write(list);
			fileWriter.flush();
			fileWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvDataTypeMismatchException e) {
			e.printStackTrace();
		} catch (CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
		
		return new File(csvFileLocation);
	}
	
	public static <T> File readBeanToCSV(File csvFile, Class<T> className, List<T> list) {
		
		HeaderColumnNameMappingStrategy<T> mappingStrategy = new HeaderColumnNameMappingStrategy<T>();
		mappingStrategy.setType(className);
	    
		try {
			FileWriter fileWriter = new FileWriter(csvFile);
			
			StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(fileWriter)
					.withMappingStrategy(mappingStrategy)
                   // .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
			
			beanToCsv.write(list);
			fileWriter.flush();
			fileWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvDataTypeMismatchException e) {
			e.printStackTrace();
		} catch (CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
		
		return csvFile;
	}
	


	/**
	 * Takes input as 1 or 2 which is the encoded value of gender
	 * in the dataset
	 * 
	 * @param genderEncodingStr
	 * @return gender encoding for FHIR format : male or female
	 */
	public static String convertGenderEncoding(String genderEncodingStr){
		String genderFHIR = "";
		if (!(BasicUtil.isStringBlank(genderEncodingStr) || BasicUtil.isStringEmpty(genderEncodingStr) || 
				BasicUtil.isStringNull(genderEncodingStr))) {
			genderEncodingStr = String.format("%.0f", Double.parseDouble(genderEncodingStr));
			int practitionerGenderInt = Integer.parseInt(genderEncodingStr);
			genderFHIR = practitionerGenderInt == 1 ? "male" : "female";
		}
		return genderFHIR;
	}
	

}
