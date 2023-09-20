 package no.uit.syntHIR.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import no.uit.syntHIR.engine.SyntHIREngine;

@RestController
@RequestMapping("/api/v1/synthir")
public class SyntHIREndpoint {

	final static Logger LOGGER = LoggerFactory.getLogger(SyntHIREndpoint.class);
	
	@Autowired
	private SyntHIREngine synthirEngine;
	
	@RequestMapping(value = "/convert-and-upload", method = RequestMethod.POST)
	public ResponseEntity<String> convertCSVtoFHIRResourcesAndUploadToFHIRServer(HttpServletRequest request, @RequestParam("file") MultipartFile csvFile, @RequestParam("fhirServerUrl") String fhirServerBaseUrl) {
		
		LOGGER.info("Request Parameter for URL {}", request.getRequestURL(), csvFile, fhirServerBaseUrl);
		
		synthirEngine.convertCSVtoFHIRResourcesAndUploadToFHIRServer(csvFile, fhirServerBaseUrl);
		
		return ResponseEntity.ok()
		        .contentType(MediaType.APPLICATION_JSON)
		        .body("Success");
	}
	
	@RequestMapping(value = "/download-and-convert", method = RequestMethod.POST)
	public ResponseEntity<byte[]> downloadFHIRResourcesFromFHIRServerAndConvertToCSV(HttpServletRequest request, @RequestParam("fhirServerUrl") String fhirServerBaseUrl) {
		
		LOGGER.info("Request Parameter for URL {}", request.getRequestURL(), fhirServerBaseUrl);
		
		byte[] csvFileByteArray = synthirEngine.downloadFHIRResourcesFromFHIRServerAndConvertToCSV(fhirServerBaseUrl);
		
		
		return ResponseEntity.ok() 
			  .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" + "file.csv")
			  .body(csvFileByteArray);
		 
	}
	
	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	public ResponseEntity<String> generateSyntheticRecordsAndUploadToSyntHIRFHIRServer(HttpServletRequest request, @RequestParam("file") MultipartFile csvFile, @RequestParam("numberOfSynRecords") String numberOfSyntheticRecords,
			@RequestParam("fhirServerUrl") String fhirServerBaseUrl) {
		
		LOGGER.info("Request Parameter for URL {}", request.getRequestURL(), csvFile, numberOfSyntheticRecords, fhirServerBaseUrl);
		
		synthirEngine.generateSyntheticRecordsAndConvertToFHIRResourcesAndUploadToSyntHIRServer(csvFile, numberOfSyntheticRecords, fhirServerBaseUrl);
	
		return ResponseEntity.ok()
		        .contentType(MediaType.APPLICATION_JSON)
		        .body("Success");
	}
	
}
