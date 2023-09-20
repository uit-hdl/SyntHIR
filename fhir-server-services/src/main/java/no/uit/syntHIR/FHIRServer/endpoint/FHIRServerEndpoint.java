package no.uit.syntHIR.FHIRServer.endpoint;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import no.uit.syntHIR.FHIRServer.FHIRResource.ResourceResList;
import no.uit.syntHIR.FHIRServer.engine.FHIRServerEngine;
import no.uit.syntHIR.FHIRServer.util.APIConstants;

@RestController
@RequestMapping("/api/v1/fhir-server")
public class FHIRServerEndpoint {

	final static Logger LOGGER = LoggerFactory.getLogger(FHIRServerEndpoint.class);
	
	@Autowired
	private FHIRServerEngine fhirServerEngine;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<Object> uploadFHIRResourcesJsonToFHIRServer(HttpServletRequest request, @RequestParam("fhirServerUrl") String fhirServerBaseUrl, 
			@RequestBody List<ResourceResList> fhirResourceRecords) {
		
		LOGGER.info("Request Parameter for URL {}", request.getRequestURL(), fhirResourceRecords, fhirServerBaseUrl);
		fhirServerEngine.uploadFHIRResourcesObjToFHIRServer(fhirResourceRecords, fhirServerBaseUrl);
		return ResponseEntity.ok()
		        .contentType(MediaType.APPLICATION_JSON)
		        .body("Success");		
	}
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ResponseEntity<List<ResourceResList>> downloadFHIRResourcesJsonFromFHIRServer(HttpServletRequest request, @RequestParam("fhirServerUrl") String fhirServerBaseUrl) {
		
		LOGGER.info("Request Parameter for URL {}", request.getRequestURL(), fhirServerBaseUrl);
		
		List<ResourceResList> fhirResourcesListToPopulate = new ArrayList<>();
		String fhirResourceRequestUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_ENCOUNTER + "?_count=1000";
		
		fhirResourcesListToPopulate = fhirServerEngine.downloadFHIRResourcesObjFromFHIRServer(fhirResourceRequestUrl, fhirResourcesListToPopulate, fhirServerBaseUrl);
		
		return ResponseEntity.ok()
		        .contentType(MediaType.APPLICATION_JSON)
		        .body(fhirResourcesListToPopulate);		
	}
	
	
}
