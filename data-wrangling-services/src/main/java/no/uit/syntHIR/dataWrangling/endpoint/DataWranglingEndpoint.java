package no.uit.syntHIR.dataWrangling.endpoint;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import no.uit.syntHIR.dataWrangling.FHIRResource.response.ResourceResList;
import no.uit.syntHIR.dataWrangling.engine.DataWranglingEngine;
import no.uit.syntHIR.dataWrangling.util.BasicConstants;

@RestController
@RequestMapping("/api/v1/data-wrangling")
public class DataWranglingEndpoint {

	final static Logger LOGGER = LoggerFactory.getLogger(DataWranglingEndpoint.class);
	
	@Autowired
	private DataWranglingEngine dataWranglineEngine;
	
	@RequestMapping(value = "/convert/npr/csv-to-fhir", method = RequestMethod.POST)
	public ResponseEntity<List<ResourceResList>> convertCSVDataToFHIRResourceFormatJson(HttpServletRequest request, @RequestParam("file") MultipartFile csvFile) {
		
		LOGGER.info("Request Parameter for URL {}", request.getRequestURL(), csvFile);
		
		//Upload the CSV file to be converted to the Azure Blob Storage
		String csvFileName = csvFile.getOriginalFilename();
		InputStream csvFileInputStream = null;
		long csvFileSize = csvFile.getSize();
		BufferedReader fileBufferedReader = null;
		try {
			csvFileInputStream = csvFile.getInputStream();
			String fileUploadedUrl  = dataWranglineEngine.uploadFileToAzureStorage(csvFileInputStream, csvFileName, csvFileSize, BasicConstants.AZURE_STORAGE_BLOB_CONTAINER_HEALTH_DATA_FILES_NAME);
			URL fileURL = new URL(fileUploadedUrl);
			fileBufferedReader = new BufferedReader(new InputStreamReader(fileURL.openStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<ResourceResList> fhirResourcesList= dataWranglineEngine.convertCSVToFHIRResourcesJson(fileBufferedReader);
		
		return ResponseEntity.ok()
		        .contentType(MediaType.APPLICATION_JSON)
		        .body(fhirResourcesList);
		
	}
	
	@RequestMapping(value = "/convert/npr/fhir-to-csv", method = RequestMethod.POST)
	public ResponseEntity<Resource> downloadCSVFileFromFHIRResourceJson(HttpServletRequest request, @RequestBody List<ResourceResList> fhirResourcesList) {
		
		LOGGER.info("Request Parameter for URL {}", request.getRequestURL(), fhirResourcesList);
		
		ByteArrayOutputStream csvFileOutputStream = dataWranglineEngine.convertFHIRResourcesJsonObjToCSV(fhirResourcesList);
		
		ByteArrayResource resource = new ByteArrayResource(csvFileOutputStream.toByteArray());
		/*
		 * InputStreamResource file = null; file = new
		 * InputStreamResource(csvFileOutputStream.);
		 */
		
		return ResponseEntity.ok()
		        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=csv_file")
		        .contentType(MediaType.parseMediaType("application/octet-stream"))
		       // .contentType(MediaType.parseMediaType("application/csv"))
		        .body(resource);
	}

}
