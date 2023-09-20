package no.uit.syntHIR.syntheticFHIRData.endpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import no.uit.syntHIR.syntheticFHIRData.engine.SyntheticFHIRDataEngine;
import no.uit.syntHIR.syntheticFHIRData.util.APIConstants;
import no.uit.syntHIR.syntheticFHIRData.util.BasicConstants;
import no.uit.syntHIR.syntheticFHIRData.util.BasicUtil;

@RestController
@RequestMapping("/api/v1/synthetic")
public class SyntheticFHIRDataEndpoint {

	final static Logger LOGGER = LoggerFactory.getLogger(SyntheticFHIRDataEndpoint.class);
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private SyntheticFHIRDataEngine syntheticFHIRDataEngine;

	@RequestMapping(value = "/generate-data", method = RequestMethod.POST)
	public ResponseEntity<InputStreamResource> generateAndDownloadSyntheticData(HttpServletRequest request, @RequestParam("file") MultipartFile csvFile,
			@RequestParam("numberOfSynRecords") String numberOfSyntheticRecords) {
		
		LOGGER.info("Request Parameter for URL {}", request.getRequestURL(), csvFile, numberOfSyntheticRecords);
		
		//First save the CSV file in a local server directory
		String csvFileLocation = null;
		try {
			csvFileLocation = BasicUtil.storeFile(csvFile.getInputStream(), csvFile.getOriginalFilename(), BasicConstants.LOCAL_INPUT_REAL_DATA_DIRECTORY_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		//Upload the file on the Gretel cloud server directory and generate synthetic data 
		String syntheticOutputDataDirectoryPathOnServer  = environment.getProperty(BasicConstants.APPLICATION_CONFIG_KEY_NAME_GRETEL_SERVER_OUTPUT_SYNTHETIC_DATA_DIRECTORY_PATH) + APIConstants.REQUEST_SEPARATOR + String.valueOf(numberOfSyntheticRecords);
		
		String outputDataFileDirectoryPathOnLocal = syntheticFHIRDataEngine.generateSyntheticDataUsingGretel(BasicConstants.GRETEL_MODEL_CONFIG_FILE_PATH, csvFileLocation, 
				BasicConstants.GRETEL_PROJECT_NAME, numberOfSyntheticRecords, syntheticOutputDataDirectoryPathOnServer);
		
		File zipFile = new File(outputDataFileDirectoryPathOnLocal);
		InputStreamResource zipFileStreamResource = null;
		
		try {
			zipFileStreamResource = new InputStreamResource(new FileInputStream(zipFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok()
		        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFile.getName())
		        .contentType(MediaType.parseMediaType("application/zip"))
		        .body(zipFileStreamResource);		
	}
	
	
}
