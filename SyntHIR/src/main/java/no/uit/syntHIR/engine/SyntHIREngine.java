package no.uit.syntHIR.engine;

import org.springframework.web.multipart.MultipartFile;

public interface SyntHIREngine {

	public void convertCSVtoFHIRResourcesAndUploadToFHIRServer(MultipartFile csvFile, String fhirServerBaseUrl);
	public byte[] downloadFHIRResourcesFromFHIRServerAndConvertToCSV(String fhirServerBaseUrl);
	public void generateSyntheticRecordsAndConvertToFHIRResourcesAndUploadToSyntHIRServer(MultipartFile csvFile, String numberOfSyntheticRecords, String fhirServerBaseUrl);
	
}
