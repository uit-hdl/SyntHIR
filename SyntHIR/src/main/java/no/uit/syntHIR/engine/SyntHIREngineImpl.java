package no.uit.syntHIR.engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import no.uit.syntHIR.util.APIConstants;
import no.uit.syntHIR.util.CustomMultipartFile;
import no.uit.syntHIR.util.SyntHIRUtil;

@Service
public class SyntHIREngineImpl implements SyntHIREngine{


	@Autowired
	private Environment environment;
	
	@Override
	public void convertCSVtoFHIRResourcesAndUploadToFHIRServer(MultipartFile csvFile, String fhirServerBaseUrl) {
		
		//convert CSV to FHIR resources using Data Wrangling API
		String dataWranglingServicesHost = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_SYNTHIR_DATAWRANGLING_SERVICES_HOST);
		String fileName = csvFile.getName();
		byte[] fileByteArray = null;
		try {
			fileByteArray =  csvFile.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Upload file and get list of FHIR resources using Data Wrangling Component
		List<List<String>> fhirResourceResponseBody = SyntHIRUtil.postRequestToUploadFile(dataWranglingServicesHost + APIConstants.DATAWRANGLING_CONVERT_CSV_TO_FHIR, fileName, fileByteArray);
		
		//Upload FHIR resources to Private FHIR server 
		String fhirServerServicesHost = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_SYNTHIR_FHIR_SERVER_SERVICES_HOST);
		String requestUrl = fhirServerServicesHost + APIConstants.FHIR_SERVER_UPLOAD;
		
		MultiValueMap<String,String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("fhirServerUrl", fhirServerBaseUrl);
		SyntHIRUtil.postRequestForObject(requestUrl, fhirResourceResponseBody, queryParams);
		
	}

	@Override
	public byte[] downloadFHIRResourcesFromFHIRServerAndConvertToCSV(String fhirServerBaseUrl) {
						
	
		//Download FHIR resources from FHIR server using FHIR Server Component
		String fhirServerServicesHost = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_SYNTHIR_FHIR_SERVER_SERVICES_HOST);
		String requestUrl = fhirServerServicesHost + APIConstants.FHIR_SERVER_DOWNLOAD;
		
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("fhirServerUrl", fhirServerBaseUrl);
		
		List<List<String>> fhirResourcesListResponseBodyObject = SyntHIRUtil.getRequestForObject(requestUrl, queryParams);
		
		//Convert FHIR resources to CSV using Data Wrangling Component
		String dataWranglingServicesHost = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_SYNTHIR_DATAWRANGLING_SERVICES_HOST);
		String dataWranglingRequestUrl = dataWranglingServicesHost +  APIConstants.DATAWRANGLING_CONVERT_FHIR_TO_CSV;
		
		byte[] csvFileByteArray = SyntHIRUtil.postRequestToDownloadFile(dataWranglingRequestUrl, fhirResourcesListResponseBodyObject);
		
		return csvFileByteArray;
	}

	@Override
	public void generateSyntheticRecordsAndConvertToFHIRResourcesAndUploadToSyntHIRServer(MultipartFile csvFile, String numberOfSyntheticRecords, String fhirServerBaseUrl) {
		
		String fileName = csvFile.getName();
		byte[] fileByteArray = null;
		try {
			fileByteArray =  csvFile.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Generate Synthetic data using Synthetic FHIR Data component
		String syntheticFHIRDataServicesHost = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_SYNTHIR_SYNTHETIC_FHIR_DATA_SERVICES_HOST);
		String generateSyntheticRecordsRequestUrl = syntheticFHIRDataServicesHost + APIConstants.SYNTHETIC_DATA_GENERATE;
		
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("numberOfSynRecords", numberOfSyntheticRecords);
		
		byte[] csvFileZipByteArray = SyntHIRUtil.postRequestToUploadFile(generateSyntheticRecordsRequestUrl, fileName, fileByteArray, queryParams);
		
		//unzip the data 
		File csvFileUnzip= null;
		try {
			csvFileUnzip = SyntHIRUtil.extractFile(csvFileZipByteArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		String multipartFileName = "synthetic_data_to_upload.csv";
		CustomMultipartFile customMultipartFile = null;
		try {
			customMultipartFile = new CustomMultipartFile(Files.readAllBytes(csvFileUnzip.toPath()), generateSyntheticRecordsRequestUrl, multipartFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    //Convert Synthetic data CSV file to FHIR resources using Data Wrangling component
		//Upload FHIR resources to SyntHIR FHIR server using FHIR Server component
		
		convertCSVtoFHIRResourcesAndUploadToFHIRServer(customMultipartFile, fhirServerBaseUrl);
		
	}
	
	
}
