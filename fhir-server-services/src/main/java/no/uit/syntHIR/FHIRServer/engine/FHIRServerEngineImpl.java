package no.uit.syntHIR.FHIRServer.engine;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import no.uit.syntHIR.FHIRServer.FHIRResource.ConditionRes;
import no.uit.syntHIR.FHIRServer.FHIRResource.EncounterRes;
import no.uit.syntHIR.FHIRServer.FHIRResource.LocationRes;
import no.uit.syntHIR.FHIRServer.FHIRResource.MedicationDispenseRes;
import no.uit.syntHIR.FHIRServer.FHIRResource.MedicationRequestRes;
import no.uit.syntHIR.FHIRServer.FHIRResource.MedicationRes;
import no.uit.syntHIR.FHIRServer.FHIRResource.PatientRes;
import no.uit.syntHIR.FHIRServer.FHIRResource.PractitionerRes;
import no.uit.syntHIR.FHIRServer.FHIRResource.ResourceResList;
import no.uit.syntHIR.FHIRServer.util.APIConstants;
import no.uit.syntHIR.FHIRServer.util.BasicUtil;
import no.uit.syntHIR.FHIRServer.util.FHIRUtil;

@Service
public class FHIRServerEngineImpl implements FHIRServerEngine{
	
	final static Logger LOGGER = LoggerFactory.getLogger(FHIRServerEngineImpl.class);
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private CacheManager cacheManager;
	
	/**
	 * It uploads the list of FHIR resources to the FHIR server
	 * based on the FHIR server URL. Depending on the FHIR server
	 * URL it fetches server credentials from the application
	 * configuration file. FHIR resources upload conditions:
	 * Patient : checks if already exists for the identifier value
	 * Practitioner : checks if already exists for the identifier value
	 * Location : checks if already exists for the Location name
	 * Condition : checks if already exists for the diagnosis code (ICD-10 codes)
	 * Encounter : checks if already exists for patient ID and visit start date
	 * Medication : Checks if already exists for the identifier value
	 * MedicationRequest : Checks if already exists for the identifier value (prescription number)
	 * MedicationDispense :  no condition check
	 * 
	 * While uploading they are linked to each other using
	 * uploaded resource URL. First Patient, Practitioner, 
	 * Location and Condition FHIR resources are uploaded. Then, Encounter
	 * FHIR resource is uploaded and updated with resource URL of Patient,
	 * Practitioner, Location and Condition. Then Medication FHIR resource 
	 * is uploaded. After that, MedicationRequest FHIR resource is uploaded 
	 * and updated with resource URL of Medication, Patient, Practitioner 
	 * and Encounter. Last, MedicationDispense is uploaded and updated
	 * with resource URL of Patient, Medication and MedicationRequest
	 * 
	 * 
	 * @param List<List<String>> and fhir server URL
	 * @return 
	 * @author pavitra
	 */
	@Override
	public void uploadFHIRResourcesToFHIRServer(List<List<String>> fhirResourceRecords, String fhirServerBaseUrl) {
		
		String accessToken, tenantID, clientID, clientSecret, accessTokenKeyName = "";
		
		if(fhirServerBaseUrl.equalsIgnoreCase(APIConstants.AZURE_PRIVATE_FHIR_SERVER_BASE_URL)) {
			
			tenantID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_AZURE_ACTIVE_DIRECTORY_TENANT_ID);
			clientID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_PRIVATE_FHIR_SERVER_CLIENT_ID);
			clientSecret = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_PRIVATE_FHIR_SERVER_CLIENT_SECRET);		
			accessTokenKeyName =  APIConstants.AZURE_PRIVATE_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME;
		}else {
			
			tenantID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_AZURE_ACTIVE_DIRECTORY_TENANT_ID);
			clientID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_SYNTHIR_FHIR_SERVER_CLIENT_ID);
			clientSecret = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_SYNTHIR_FHIR_SERVER_CLIENT_SECRET);
			accessTokenKeyName =  APIConstants.AZURE_SYNTHIR_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME;
		}
		
		//Get access token based on FHIR server URL
		//get from cache 
		Object accessTokenCacheObject = cacheManager.getCache("fhirServer_accessToken").get(accessTokenKeyName);
		
		if(accessTokenCacheObject == null) {
			
			//get access token from Azure AD based on FHIR server URL and save the data into cache
			accessToken = FHIRUtil.getAccessTokenFromAzureActiveDirectory(tenantID, fhirServerBaseUrl, clientID, clientSecret);
			
		}else {
			
			//get access token from cache since it is not null
			accessToken = fhirServerBaseUrl.equalsIgnoreCase(APIConstants.AZURE_PRIVATE_FHIR_SERVER_BASE_URL) ? cacheManager.getCache("fhirServer_accessToken").get(APIConstants.AZURE_PRIVATE_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME).toString() : 
					cacheManager.getCache("fhirServer_accessToken").get(APIConstants.AZURE_SYNTHIR_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME).toString();
		}
		//System.out.println("Token from cache ::" + cacheManager.getCache("fhirServer_accessToken").get(accessTokenKeyName));
		
		
		for(List<String> fhirResourceRecord : fhirResourceRecords) {
			
			LOGGER.info("Single record with list of FHIR resources {}", fhirResourceRecord);
						
			String patientResourceFullUrl = "";
			String practitionerResourceFullUrl = "";
			String locationResourceFullUrl = "";
			String conditionResourceFullUrl = "";
			String encounterResourceFullUrl = "";
			String medicationResourceFullUrl = "";
			String medicationRequestResourceFullUrl = "";
			
			
			for(String fhirResourceRecordStr : fhirResourceRecord) {
				
				if(!(BasicUtil.isStringBlank(fhirResourceRecordStr) || BasicUtil.isStringEmpty(fhirResourceRecordStr) || BasicUtil.isStringNull(fhirResourceRecordStr))) {
					
					ObjectMapper objectMapper = new ObjectMapper();
					
					try {
						JsonNode jsonObjNode = objectMapper.readTree(fhirResourceRecordStr);
						
						String fhirResourceType = jsonObjNode.get("resourceType").textValue();
						String fhirResourceJsonStr = jsonObjNode.toString();
						
						LOGGER.info("FHIR resource type {}", fhirResourceType);
						LOGGER.info("FHIR resource to upload {}", jsonObjNode.toString());
						
						//check if FHIR resource type is 'Patient'
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_PATIENT)) {
							
							// Check if Patient already exists on the FHIR server using identifier (Patient ID)
							String identifierValuePatient = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("identifier").get(0).get("value"));
							String fhirResourceQueryUrlPatient = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType + "?identifier=" + identifierValuePatient;
							String resourceUrlFromGetRequestPatient = FHIRUtil.getFHIRResourceUrlFromResourceBody(FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceQueryUrlPatient, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret));
													
							//if URL from get request is null that means this patient does not exist on the FHIR Server, then create patient FHIR resource
							if(BasicUtil.isStringNull(resourceUrlFromGetRequestPatient) || BasicUtil.isStringEmpty(resourceUrlFromGetRequestPatient) || BasicUtil.isStringBlank(resourceUrlFromGetRequestPatient)) {

								String resourceUrlforCreateRequestPatient = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType;
								String createdResourcePatientResponseBody = FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestPatient, accessToken, fhirResourceJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
								String createdResourceIdentifierPatient = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(createdResourcePatientResponseBody);
								patientResourceFullUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType + APIConstants.REQUEST_SEPARATOR + createdResourceIdentifierPatient;
								 							
							}else {
								patientResourceFullUrl = resourceUrlFromGetRequestPatient;
							}
							
						}
						
						
						//check if FHIR resource type is 'Practitioner'
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_PRACTITIONER)) {
							
							// Check if Practitioner already exists on the FHIR server using identifier (Practitioner ID)
							String identifierValuePractitioner = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("identifier").get(0).get("value"));
							String fhirResourceQueryUrlPractitioner = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType + "?identifier=" + identifierValuePractitioner;
							String resourceUrlFromGetRequestPractitioner = FHIRUtil.getFHIRResourceUrlFromResourceBody(FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceQueryUrlPractitioner, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret));
							
							//if URL from get request is null that means this Practitioner does not exist on the FHIR Server, then create Practitioner FHIR resource
							if(BasicUtil.isStringNull(resourceUrlFromGetRequestPractitioner) || BasicUtil.isStringEmpty(medicationRequestResourceFullUrl) || BasicUtil.isStringBlank(resourceUrlFromGetRequestPractitioner)) {
								
								String resourceUrlforCreateRequestPracitioner = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType;
								String createdResourcePracitionerResponseBody = FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestPracitioner, accessToken, fhirResourceJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
								String createdResourceIdentifierPracitioner = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(createdResourcePracitionerResponseBody);
								practitionerResourceFullUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType + APIConstants.REQUEST_SEPARATOR + createdResourceIdentifierPracitioner;
								 
							}else {
								practitionerResourceFullUrl = resourceUrlFromGetRequestPractitioner;
							}
							
						}
						
						//check if FHIR resource type is 'Location'
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_LOCATION)) {
							
							// Check if Location already exists on the FHIR server using Hospital Name
							String nameOfLocation = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("name"));
							
							if(!(BasicUtil.isStringBlank(nameOfLocation) || BasicUtil.isStringEmpty(nameOfLocation) || BasicUtil.isStringNull(nameOfLocation))) {
								
								String fhirResourceQueryUrlLocation = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType;
								String fhirResourecLocationStr = FHIRUtil.getFHIRResourceFromFHIRServerWithQueryParams(fhirResourceQueryUrlLocation, nameOfLocation, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
								//String fhirResourecLocationStr = FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceQueryUrlLocation, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
								String resourceUrlFromGetRequestLocation = FHIRUtil.getFHIRResourceUrlFromResourceBody(fhirResourecLocationStr);
								
								//if URL from get request is null that means this location does not exist on the FHIR Server, then create Location FHIR resource
								if(BasicUtil.isStringNull(resourceUrlFromGetRequestLocation) || BasicUtil.isStringEmpty(resourceUrlFromGetRequestLocation) || BasicUtil.isStringBlank(resourceUrlFromGetRequestLocation)) {
									
									String resourceUrlforCreateRequestLocation = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType;
									String createdResourceLocationResponseBody = FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestLocation, accessToken, fhirResourceJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
									String createdResourceIdentifierLocation = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(createdResourceLocationResponseBody);
									locationResourceFullUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType + APIConstants.REQUEST_SEPARATOR + createdResourceIdentifierLocation;
									 
								}else {
									locationResourceFullUrl = resourceUrlFromGetRequestLocation;
								}
							}

						}
						
						//check if FHIR resource is 'Condition
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_CONDITION)) {
							
							//Check if Condition already exists for a patient ID and diagnosis code.
							String diagnosisCode = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("code").get("coding").get(0).get("code"));
							
							if(!(BasicUtil.isStringBlank(diagnosisCode) || BasicUtil.isStringEmpty(diagnosisCode) || BasicUtil.isStringNull(diagnosisCode))) {
								
								String fhirResourceQueryUrlCondition = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType + "?code=" + diagnosisCode + "&subject=" + patientResourceFullUrl;
								String resourceUrlFromGetRequestCondition = FHIRUtil.getFHIRResourceUrlFromResourceBody(FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceQueryUrlCondition, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret));
								
								//if URL from get request is null that means this condition does not exist on the FHIR Server, then create Condition FHIR resource
								if(BasicUtil.isStringNull(resourceUrlFromGetRequestCondition) || BasicUtil.isStringEmpty(resourceUrlFromGetRequestCondition) || BasicUtil.isStringBlank(resourceUrlFromGetRequestCondition)) {

									String resourceUrlforCreateRequestCondition = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType;
									
									//Update the JSON with PatientFull URL details
									((ObjectNode) jsonObjNode.get("subject")).put("reference", patientResourceFullUrl);
									
									fhirResourceJsonStr = jsonObjNode.toString();
									
									//Each condition resource is with respect to patient and the prescribing practitioner
									String createdResourceConditionResponseBody = FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestCondition, accessToken, fhirResourceJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
									String createdResourceIdentifierCondition = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(createdResourceConditionResponseBody);
									conditionResourceFullUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType + APIConstants.REQUEST_SEPARATOR + createdResourceIdentifierCondition;
									 							
								}else {
									conditionResourceFullUrl = resourceUrlFromGetRequestCondition;
								}
							}
							
						}
						
						//check if FHIR resource is 'Encounter'
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_ENCOUNTER)) {
							
							//Check if Encounter already exists for a patient ID and visit start date
							String startDate = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("period").get("start"));
							
							if(!(BasicUtil.isStringBlank(startDate) || BasicUtil.isStringEmpty(startDate) || BasicUtil.isStringNull(startDate))) {
								
								String fhirResourceQueryUrlEncounter = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType + "?subject=" + patientResourceFullUrl + "&date=" + startDate;
								String resourceUrlFromGetRequestEncounter = FHIRUtil.getFHIRResourceUrlFromResourceBody(FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceQueryUrlEncounter, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret));
								
								//if URL from get request is null that means this encounter does not exist on the FHIR Server, then create Encounter FHIR resource
								if(BasicUtil.isStringNull(resourceUrlFromGetRequestEncounter) || BasicUtil.isStringEmpty(resourceUrlFromGetRequestEncounter) || BasicUtil.isStringBlank(resourceUrlFromGetRequestEncounter)) {
									
									String resourceUrlforCreateRequestEncounter = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType;
									
									//Update the JSON with Patient, Condition, Practitioner, Location Full URL details
									((ObjectNode) jsonObjNode.get("subject")).put("reference", patientResourceFullUrl);
									((ObjectNode) jsonObjNode.get("diagnosis").get("condition")).put("reference", conditionResourceFullUrl);
									((ObjectNode) jsonObjNode.get("participant").get(0).get("individual")).put("reference", practitionerResourceFullUrl);
									((ObjectNode) jsonObjNode.get("location").get(0).get("location")).put("reference", locationResourceFullUrl);
									
									fhirResourceJsonStr = jsonObjNode.toString();
									
									String createdResourceEncounterResponseBody = FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestEncounter, accessToken, fhirResourceJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
									String createdResourceIdentifierEncounter = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(createdResourceEncounterResponseBody);
									encounterResourceFullUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType + APIConstants.REQUEST_SEPARATOR + createdResourceIdentifierEncounter;
								}else {
									encounterResourceFullUrl = resourceUrlFromGetRequestEncounter;
								}
							}
							
						}
						
						//check if FHIR resource is 'Medication
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_MEDICATION)) {
						
							// Check if Medication already exists on the FHIR server using identifier value
							String identifierValueMedication = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("identifier").get(0).get("value"));
							String fhirResourceQueryUrlMedication = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType + "?identifier=" + identifierValueMedication;
							String resourceUrlFromGetRequestMedication = FHIRUtil.getFHIRResourceUrlFromResourceBody(FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceQueryUrlMedication, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret));
							
							//if URL from get request is null that means this Medication does not exist on the FHIR Server, then create Medication FHIR resource
							if(BasicUtil.isStringNull(resourceUrlFromGetRequestMedication) || BasicUtil.isStringEmpty(resourceUrlFromGetRequestMedication) || BasicUtil.isStringBlank(resourceUrlFromGetRequestMedication)) {
							
								String resourceUrlforCreateRequestMedication = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType;
								String createdResourceMedicationResponseBody = FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestMedication, accessToken, fhirResourceJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
								String createdResourceIdentifierMedication = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(createdResourceMedicationResponseBody);
								medicationResourceFullUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType + APIConstants.REQUEST_SEPARATOR + createdResourceIdentifierMedication;
								 
							}else {
								medicationResourceFullUrl = resourceUrlFromGetRequestMedication;
							}
						}
						
						//check if FHIR resource is MedicationRequest
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_MEDICATION_REQUEST)) {
							
							//check if Medication Request already exists on the FHIR server using Prescription number
							String identifierValueMedicationRequest = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("identifier").get(0).get("value"));
							String fhirResourceQueryUrlMedicationRequest = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType + "?identifier=" + identifierValueMedicationRequest;
							String resourceUrlFromGetRequestMedicationRequest = FHIRUtil.getFHIRResourceUrlFromResourceBody(FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceQueryUrlMedicationRequest, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret));
							
							//if URL from get request is null that means this Medication Request (Prescription) does not exist on the FHIR Server, then create MedicatioRequest FHIR resource
							if(BasicUtil.isStringNull(resourceUrlFromGetRequestMedicationRequest) || BasicUtil.isStringEmpty(resourceUrlFromGetRequestMedicationRequest) || BasicUtil.isStringBlank(resourceUrlFromGetRequestMedicationRequest)) {
								//Create Medication Request on FHIR server
								String resourceUrlforCreateRequestMedicationRequest = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType;
								
								//Update the JSON with Medication, Patient, Encounter(hospitalization), Practitioner Full URL details
								((ObjectNode) jsonObjNode.get("medicationReference")).put("reference", medicationResourceFullUrl);
								((ObjectNode) jsonObjNode.get("subject")).put("reference", patientResourceFullUrl);
								((ObjectNode) jsonObjNode.get("encounter")).put("reference", encounterResourceFullUrl);
								((ObjectNode) jsonObjNode.get("recorder")).put("reference", practitionerResourceFullUrl);
								
								fhirResourceJsonStr = jsonObjNode.toString();
								
								String createdResourceMedicationRequestResponseBody  = FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestMedicationRequest, accessToken, fhirResourceJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
								String createdResourceIdentifierMedicationRequest = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(createdResourceMedicationRequestResponseBody);
								medicationRequestResourceFullUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType + APIConstants.REQUEST_SEPARATOR + createdResourceIdentifierMedicationRequest;
								
							}else {
								medicationRequestResourceFullUrl = resourceUrlFromGetRequestMedicationRequest;
							}
						}
						
						//check if FHIR resource is MedicationDispense
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_MEDICATION_DISPENSE)) {
							
							//Create Medication Dispense on FHIR server
							String resourceUrlforCreateRequestMedicationDispense = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + fhirResourceType;
							
							//Update the JSON with Patient, Medication, MedicationRequest Full URL details
							((ObjectNode) jsonObjNode.get("subject")).put("reference", patientResourceFullUrl);
							((ObjectNode) jsonObjNode.get("medicationReference")).put("reference", medicationResourceFullUrl);
							((ObjectNode) jsonObjNode.get("authorizingPrescription")).put("reference", medicationRequestResourceFullUrl);
							
							fhirResourceJsonStr = jsonObjNode.toString();
							FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestMedicationDispense, accessToken, fhirResourceJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
						}
						
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				
				}
				
				
			}
			LOGGER.info("End of one record uploaded to FHIR server");
		}
	}
	
	
	/**
	 * Downloads FHIR resources from the FHIR Server. 
	 * based on the FHIR server URL. Depending on the FHIR server
	 * URL it fetches server credentials from the application
	 * configuration file. First it fetches the resources with respect
	 * to the resource request URL. Azure Server limits download of 
	 * upto 1000 resources and provides URL for next fetch request.
	 * Hence the method is called recursively till all the resources
	 * are downloaded. The request URL is a fetch request for Encounter
	 * FHIR resource since it connected to all other resources. 
	 * First, it gets list of all the Encounter resources. For each
	 * Encounter, get list MedicationRequest resources using the Encounter
	 * resource URL. Further resources such as Patient, Practitioner,
	 * Location and Condition are retrieved from Encounter. Using
	 * MedicationRequest we get Medication resource and MedicationDispense
	 * is retrieved using MedicationRequest resource URL as the query param
	 * 
	 * 
	 * @param FHIR resource request URL,empty list to populate with FHIR resources (List<List<String>>),FHIR server URL
	 * @return downloaded FHIR resources (List<List<String>>)
	 * @author pavitra
	 */
	
	@Override
	public List<List<String>> downloadFHIRResourcesFromFHIRServer(String fhirResourceRequestUrl ,List<List<String>> fhirResourcesListToPopulate, String fhirServerBaseUrl) {

		clearCache();
		if(!BasicUtil.isStringNull(fhirResourceRequestUrl)) {
			
			String accessToken, tenantID, clientID, clientSecret, accessTokenKeyName = "";
			
			if(fhirServerBaseUrl.equalsIgnoreCase(APIConstants.AZURE_PRIVATE_FHIR_SERVER_BASE_URL)) {
				
				tenantID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_AZURE_ACTIVE_DIRECTORY_TENANT_ID);
				clientID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_PRIVATE_FHIR_SERVER_CLIENT_ID);
				clientSecret = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_PRIVATE_FHIR_SERVER_CLIENT_SECRET);		
				accessTokenKeyName =  APIConstants.AZURE_PRIVATE_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME;
				
			}else {
				
				tenantID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_AZURE_ACTIVE_DIRECTORY_TENANT_ID);
				clientID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_SYNTHIR_FHIR_SERVER_CLIENT_ID);
				clientSecret = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_SYNTHIR_FHIR_SERVER_CLIENT_SECRET);
				accessTokenKeyName =  APIConstants.AZURE_SYNTHIR_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME;

			}
			//Get access token based on FHIR server URL
			//get from cache 
			Object accessTokenCacheObject = cacheManager.getCache("fhirServer_accessToken").get(accessTokenKeyName);
			
			if(accessTokenCacheObject == null) {
				
				//get access token from Azure AD based on FHIR server URL and save the data into cache
				accessToken = FHIRUtil.getAccessTokenFromAzureActiveDirectory(tenantID, fhirServerBaseUrl, clientID, clientSecret);
				
			}else {
				
				
				accessToken = fhirServerBaseUrl.equalsIgnoreCase(APIConstants.AZURE_PRIVATE_FHIR_SERVER_BASE_URL) ? cacheManager.getCache("fhirServer_accessToken").get(APIConstants.AZURE_PRIVATE_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME).toString() : 
						cacheManager.getCache("fhirServer_accessToken").get(APIConstants.AZURE_SYNTHIR_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME).toString();
			}
			//System.out.println("Token from cache ::" + cacheManager.getCache("fhirServer_accessToken").get(accessTokenKeyName));
			
			//System.out.println(cacheManager.getCache("fhiraccesstoken"));
			//First get all the encounters (which has hospitalizations) with respect to a hospital server
			String responseBody =  FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceRequestUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
			
			if(!BasicUtil.isStringNull(responseBody)) {
					
					ObjectMapper objectMapper = new ObjectMapper();
					
					try {
						
						JsonNode jsonObjNode = objectMapper.readTree(responseBody);
								
							String entryObject = jsonObjNode.get("entry").toString();
							List<FHIRResourceResponse> fhirResourceResponseList = objectMapper.readValue(entryObject, new TypeReference<List<FHIRResourceResponse>>(){});
							
							for(FHIRResourceResponse encounterResourceResponseObj : fhirResourceResponseList) {
								
								String encounterResourceRequestFullUrl = encounterResourceResponseObj.getFullUrl();
								
								//Get MedicationRequest on the basis of Encounter full URL. Since there will be multiple medication request for each encounter
								String medicationRequestResourceRequestUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_MEDICATION_REQUEST +"?encounter=" + encounterResourceRequestFullUrl;
								String medicationRequestResponseBody =  FHIRUtil.getFHIRResourceFromFHIRServer(medicationRequestResourceRequestUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
								//System.out.println("medicationRequestResponseBody::" + medicationRequestResponseBody);
								JsonNode medicationRequestResponseJsonNode = objectMapper.readTree(medicationRequestResponseBody);
								JsonNode medicationRequestResponseEntryJsonObject = medicationRequestResponseJsonNode.get("entry");
								
								//check if medication request exists for the given encounter
								
								if(medicationRequestResponseEntryJsonObject != null) {
									
									String medicationRequestResponseEntryObject = medicationRequestResponseJsonNode.get("entry").toString();
									
									List<FHIRResourceResponse> medicationRequestResourceResponseList = objectMapper.readValue(medicationRequestResponseEntryObject, new TypeReference<List<FHIRResourceResponse>>(){});
									
									for(FHIRResourceResponse medicationRequestResourceResponseObj : medicationRequestResourceResponseList) {
										
										List<String> fhirResourceRecord = new ArrayList<String>();
										
										//Fetch details from the Encounter FHIR resource
										Object encounterFHIRResourceObject = encounterResourceResponseObj.getResource();
										ObjectWriter encounterRequestOw = objectMapper.writer().withDefaultPrettyPrinter();
										String encounterFHIRResource = encounterRequestOw.writeValueAsString(encounterFHIRResourceObject);
										fhirResourceRecord.add(BasicUtil.parseJsonStrToJsonNode(encounterFHIRResource).toString());
										
										JsonNode encounterFHIRResourceJsonObjNode = objectMapper.readTree(encounterFHIRResource);
										//From each FHIR resource 'Encounter', get Patient, Practitioner, Condition and Location details
										String patientResourceFullUrl = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(encounterFHIRResourceJsonObjNode.get("subject").get("reference"));
										String patientFHIRResource = FHIRUtil.getFHIRResourceFromFHIRServer(patientResourceFullUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
										fhirResourceRecord.add(BasicUtil.parseJsonStrToJsonNode(patientFHIRResource).toString());
										
										String practitionerResourceFullUrl = encounterFHIRResourceJsonObjNode.get("participant") != null ? encounterFHIRResourceJsonObjNode.get("participant").get(0).get("individual").get("reference").textValue() : "";
										String practitionerFHIRResource = FHIRUtil.getFHIRResourceFromFHIRServer(practitionerResourceFullUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
										fhirResourceRecord.add(BasicUtil.parseJsonStrToJsonNode(practitionerFHIRResource).toString());
										
										
										{
											//check it Location exists for the Encounter
											JsonNode locationResourecFullUrlNodeObj = encounterFHIRResourceJsonObjNode.get("location").get(0).get("location").get("reference");
											if(locationResourecFullUrlNodeObj != null) {
												
												String LocationResourceFullUrl = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(locationResourecFullUrlNodeObj);
												String locationFHIRResource = FHIRUtil.getFHIRResourceFromFHIRServer(LocationResourceFullUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
												fhirResourceRecord.add(BasicUtil.parseJsonStrToJsonNode(locationFHIRResource).toString());
											}
										}
										
										
										String conditionResourceFullUrl = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(encounterFHIRResourceJsonObjNode.get("diagnosis").get(0).get("condition").get("reference"));
										String conditionFHIRResource = FHIRUtil.getFHIRResourceFromFHIRServer(conditionResourceFullUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
										fhirResourceRecord.add(BasicUtil.parseJsonStrToJsonNode(conditionFHIRResource).toString());
										
										//Medication Request
										String medicationRequestResourceRequestFullUrl = medicationRequestResourceResponseObj.getFullUrl();
										Object medicationRequestFHIRResourceObject = medicationRequestResourceResponseObj.getResource();
										ObjectWriter medicationRequestOw = objectMapper.writer().withDefaultPrettyPrinter();
										String medicationRequestFHIRResource = medicationRequestOw.writeValueAsString(medicationRequestFHIRResourceObject);
										fhirResourceRecord.add(BasicUtil.parseJsonStrToJsonNode(medicationRequestFHIRResource).toString());
										JsonNode medicationRequestfhirResourceJsonObjNode = objectMapper.readTree(medicationRequestFHIRResource);
										
										//Fetch Medication for a prescription from MedicationRequest FHIR resource
										String medicationResourceFullUrl = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(medicationRequestfhirResourceJsonObjNode.get("medicationReference").get("reference"));
										String medicationFHIRResource = FHIRUtil.getFHIRResourceFromFHIRServer(medicationResourceFullUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
										fhirResourceRecord.add(BasicUtil.parseJsonStrToJsonNode(medicationFHIRResource).toString());
										
										{
											//Fetch Medication Dispensed. Medication dispensed (FHIR resource) for a Medication. It is obtained with medicationRequest as the query parameter
											String medicationDispenseResourceRequestUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_MEDICATION_DISPENSE +"?prescription=" + medicationRequestResourceRequestFullUrl;
											String medicationDispensedResponseBody = FHIRUtil.getFHIRResourceFromFHIRServer(medicationDispenseResourceRequestUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);;
											JsonNode medicationDispensedResponseJsonObjNode = objectMapper.readTree(medicationDispensedResponseBody);
											String medicationDispensedResponseEntryObject = medicationDispensedResponseJsonObjNode.get("entry").toString();
											List<FHIRResourceResponse> medicationDispensedResponseList = objectMapper.readValue(medicationDispensedResponseEntryObject, new TypeReference<List<FHIRResourceResponse>>(){});
											ObjectWriter medicationDispenseRequestOw = objectMapper.writer().withDefaultPrettyPrinter();
											String medicationDispenseFHIRResource = medicationDispenseRequestOw.writeValueAsString(medicationDispensedResponseList.get(0).getResource());
											fhirResourceRecord.add(BasicUtil.parseJsonStrToJsonNode(medicationDispenseFHIRResource).toString());
										}
										
										
										//Add to the final FHIR resource list
										fhirResourcesListToPopulate.add(fhirResourceRecord);
										
									}
								}
								//LOGGER.info("End of one Encounter downloaded from FHIR server");
							}
							
							{
								//check relation attribute in link to identify if there are more resources
								//the value of relation attribute will be next if there are more resources, else self
								String linkRelationValue = jsonObjNode.get("link").get(0).get("relation").textValue();
								fhirResourceRequestUrl = (linkRelationValue.equalsIgnoreCase("next")) ? jsonObjNode.get("link").get(0).get("url").textValue() : null;
								
								if (!BasicUtil.isStringNull(fhirResourceRequestUrl)) {
									LOGGER.info("Recuresively calling next request URL to fetch resources from FHIR server {}", fhirResourceRequestUrl);
									//Call the method recursively
									downloadFHIRResourcesFromFHIRServer(fhirResourceRequestUrl, fhirResourcesListToPopulate, fhirServerBaseUrl);
								}
								
							}
							
						} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}

					LOGGER.info("FHIR resources downloaded from FHIR server {}", fhirResourcesListToPopulate);
				
				}
			
		}
		
		//clear cache 
		clearCache();
		return fhirResourcesListToPopulate;
	}
		
	@CacheEvict(value = { "fhirServer_accessToken" })
	public void clearCache() {  
	   System.out.println("Cache '{}' cleared.");    
	}


	@Override
	public void uploadFHIRResourcesObjToFHIRServer(List<ResourceResList> fhirResourceRecords, String fhirServerBaseUrl) {
		
		String accessToken, tenantID, clientID, clientSecret, accessTokenKeyName = "";
		
		if(fhirServerBaseUrl.equalsIgnoreCase(APIConstants.AZURE_PRIVATE_FHIR_SERVER_BASE_URL)) {
			
			tenantID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_AZURE_ACTIVE_DIRECTORY_TENANT_ID);
			clientID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_PRIVATE_FHIR_SERVER_CLIENT_ID);
			clientSecret = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_PRIVATE_FHIR_SERVER_CLIENT_SECRET);		
			accessTokenKeyName =  APIConstants.AZURE_PRIVATE_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME;
		}else {
			
			tenantID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_AZURE_ACTIVE_DIRECTORY_TENANT_ID);
			clientID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_SYNTHIR_FHIR_SERVER_CLIENT_ID);
			clientSecret = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_SYNTHIR_FHIR_SERVER_CLIENT_SECRET);
			accessTokenKeyName =  APIConstants.AZURE_SYNTHIR_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME;
		}
		
		//Get access token based on FHIR server URL
		//get from cache 
		Object accessTokenCacheObject = cacheManager.getCache("fhirServer_accessToken").get(accessTokenKeyName);
		
		if(accessTokenCacheObject == null) {
			
			//get access token from Azure AD based on FHIR server URL and save the data into cache
			accessToken = FHIRUtil.getAccessTokenFromAzureActiveDirectory(tenantID, fhirServerBaseUrl, clientID, clientSecret);
			
		}else {
			
			//get access token from cache since it is not null
			accessToken = fhirServerBaseUrl.equalsIgnoreCase(APIConstants.AZURE_PRIVATE_FHIR_SERVER_BASE_URL) ? cacheManager.getCache("fhirServer_accessToken").get(APIConstants.AZURE_PRIVATE_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME).toString() : 
					cacheManager.getCache("fhirServer_accessToken").get(APIConstants.AZURE_SYNTHIR_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME).toString();
		}
		
		for(ResourceResList resourceResponseObj : fhirResourceRecords) {
			
			LOGGER.info("Single record with list of FHIR resources {}", resourceResponseObj);
			
			String patientResourceFullUrl = "";
			String patientResourceIdentifier = "";
			String practitionerResourceFullUrl = "";
			String practitionerResourceIdentifier = "";
			String locationResourceFullUrl = "";
			String locationResourceIdentifier = "";
			//String conditionResourceFullUrl = "";
			//String conditionResourceIdentifier = "";
			String encounterResourceFullUrl = "";
			String encounterResourceIdentifier = "";
			String medicationResourceFullUrl = "";
			String medicationResourceIdentifier = "";
			String medicationRequestResourceFullUrl = "";
			String medicationRequestResourceIdentifier = "";

			PatientRes patientRes = resourceResponseObj.getPatient();
			// Check if Patient already exists on the FHIR server using identifier (Patient ID)
			String identifierValuePatient = patientRes.getIdentifier().get(0).getValue();
			String fhirResourceQueryUrlPatient = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_PATIENT + "?identifier=" + identifierValuePatient;
			String fhirResourceResBodyPatient = FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceQueryUrlPatient, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
			Object resourceResObjPatient =  FHIRUtil.getResourceObjectFromResponse(fhirResourceResBodyPatient);
			
			if(resourceResObjPatient == null) {
				
				String resourceUrlforCreateRequestPatient = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_PATIENT;
				String patientResJsonStr = BasicUtil.convertPojoObjectToJsonStr(patientRes);
				String createdResourcePatientResponseBody = FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestPatient, accessToken, patientResJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
				patientResourceIdentifier = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(createdResourcePatientResponseBody);
			}else {
				String patientResJsonStr = BasicUtil.convertPojoObjectToJsonStr(resourceResObjPatient);
				patientResourceIdentifier = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(patientResJsonStr);
			}

			patientResourceFullUrl = APIConstants.FHIR_RESOURCE_PATIENT + APIConstants.REQUEST_SEPARATOR + patientResourceIdentifier;
			
			//check if FHIR resource type is 'Practitioner'
			PractitionerRes practitionerRes = resourceResponseObj.getPractitioner();
			
			// Check if Practitioner already exists on the FHIR server using identifier (Practitioner ID)
			String identifierValuePractitioner = practitionerRes.getIdentifier().get(0).getValue();
			String fhirResourceQueryUrlPractitioner = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_PRACTITIONER + "?identifier=" + identifierValuePractitioner;
			String fhirResourceResBodyPractitioner = FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceQueryUrlPractitioner, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
			Object resourceResObjPractitioner =  FHIRUtil.getResourceObjectFromResponse(fhirResourceResBodyPractitioner);
			
			if(resourceResObjPractitioner == null) {
				
				String resourceUrlforCreateRequestPracitioner = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_PRACTITIONER;
				String practitionerResJsonStr = BasicUtil.convertPojoObjectToJsonStr(practitionerRes);
				String createdResourcePracitionerResponseBody = FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestPracitioner, accessToken, practitionerResJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
				practitionerResourceIdentifier = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(createdResourcePracitionerResponseBody);
				 
			}else {
				String practitionerResJsonStr = BasicUtil.convertPojoObjectToJsonStr(resourceResObjPractitioner);
				practitionerResourceIdentifier = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(practitionerResJsonStr);
			}
			practitionerResourceFullUrl = APIConstants.FHIR_RESOURCE_PRACTITIONER + APIConstants.REQUEST_SEPARATOR + practitionerResourceIdentifier;;
			
			
			LocationRes locationRes = resourceResponseObj.getLocation();
			// Check if Location already exists on the FHIR server using Hospital Name
			String nameOfLocation = locationRes.getName();
			
			if(!(BasicUtil.isStringBlank(nameOfLocation) || BasicUtil.isStringEmpty(nameOfLocation) || BasicUtil.isStringNull(nameOfLocation))) {
				
				String fhirResourceQueryUrlLocation = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_LOCATION;
				String fhirResourceResBodyLocation = FHIRUtil.getFHIRResourceFromFHIRServerWithQueryParams(fhirResourceQueryUrlLocation, nameOfLocation, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
				Object resourceResObjLocation =  FHIRUtil.getResourceObjectFromResponse(fhirResourceResBodyLocation);
				
				if(resourceResObjLocation == null) {
					
					String resourceUrlforCreateRequestLocation = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_LOCATION;
					String locationResJsonStr = BasicUtil.convertPojoObjectToJsonStr(locationRes);
					String createdResourceLocationResponseBody = FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestLocation, accessToken, locationResJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
					locationResourceIdentifier = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(createdResourceLocationResponseBody);
					 
				}else {
					String locationResJsonStr = BasicUtil.convertPojoObjectToJsonStr(resourceResObjLocation);
					locationResourceIdentifier = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(locationResJsonStr);
				}
				locationResourceFullUrl = APIConstants.FHIR_RESOURCE_LOCATION + APIConstants.REQUEST_SEPARATOR + locationResourceIdentifier;;
				
			}
			
			//check if FHIR resource is 'Encounter'
			EncounterRes encounterRes = resourceResponseObj.getEncounter();
				
			//Check if Encounter already exists for a patient ID and visit start date
			String startDate = encounterRes.getPeriod().getStart();
			
			if(!(BasicUtil.isStringBlank(startDate) || BasicUtil.isStringEmpty(startDate) || BasicUtil.isStringNull(startDate))) {
				
				//String fhirResourceQueryUrlEncounter = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_ENCOUNTER + "?subject=" + patientResourceFullUrl + "&date=" + startDate;
				String fhirResourceQueryUrlEncounter = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_ENCOUNTER + "?subject:Patient.identifier=" + identifierValuePatient + "&date=" + startDate;
				String fhirResourceResBodyEncounter = FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceQueryUrlEncounter, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
				Object resourceResObjEncounter =  FHIRUtil.getResourceObjectFromResponse(fhirResourceResBodyEncounter);
				
				if(resourceResObjEncounter == null) {
					
					String resourceUrlforCreateRequestEncounter = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_ENCOUNTER;
					
					//Update the JSON with Patient, Condition, Practitioner, Location Full URL details
					encounterRes.getSubject().setReference(patientResourceFullUrl);
					encounterRes.getSubject().getIdentifier().setValue(patientResourceIdentifier);;
					
					encounterRes.getParticipant().get(0).getIndividual().setReference(practitionerResourceFullUrl);
					encounterRes.getParticipant().get(0).getIndividual().getIdentifier().setValue(practitionerResourceIdentifier);
					
					encounterRes.getLocation().get(0).getLocation().setReference(locationResourceFullUrl);
					encounterRes.getLocation().get(0).getLocation().getIdentifier().setValue(locationResourceIdentifier);
					
					String encounterResJsonStr = BasicUtil.convertPojoObjectToJsonStr(encounterRes);
					
					String createdResourceEncounterResponseBody = FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestEncounter, accessToken, encounterResJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
					encounterResourceIdentifier = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(createdResourceEncounterResponseBody);
				}else {
					String encounterResJsonStr = BasicUtil.convertPojoObjectToJsonStr(resourceResObjEncounter);
					encounterResourceIdentifier = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(encounterResJsonStr);
				}
				encounterResourceFullUrl = APIConstants.FHIR_RESOURCE_ENCOUNTER + APIConstants.REQUEST_SEPARATOR + encounterResourceIdentifier;;
				
			}
			
			
			ConditionRes conditionRes = resourceResponseObj.getCondition();
	
			//Check if Condition already exists for a patient ID and encounter ID.
			String diagnosisCode = conditionRes.getCode().getCoding().get(0).getCode();
			
			if(!(BasicUtil.isStringBlank(diagnosisCode) || BasicUtil.isStringEmpty(diagnosisCode) || BasicUtil.isStringNull(diagnosisCode))) {
				
				String fhirResourceQueryUrlCondition = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_CONDITION + "?encounter=" + encounterResourceIdentifier + "&subject=" + patientResourceIdentifier;
				String fhirResourceResBodyCondition = FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceQueryUrlCondition, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
				Object resourceResObjCondition =  FHIRUtil.getResourceObjectFromResponse(fhirResourceResBodyCondition);
				
				//if URL from get request is null that means this condition does not exist on the FHIR Server, then create Condition FHIR resource
				if(resourceResObjCondition == null) {

					String resourceUrlforCreateRequestCondition = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_CONDITION;
					
					//Update the JSON with PatientFull URL details
					conditionRes.getSubject().setReference(patientResourceFullUrl);
					conditionRes.getSubject().getIdentifier().setValue(patientResourceIdentifier);
					
					conditionRes.getEncounter().setReference(encounterResourceFullUrl);
					conditionRes.getEncounter().getIdentifier().setValue(encounterResourceIdentifier);
					
					//Each condition resource is with respect to patient and the prescribing practitioner
					String conditionResJsonStr = BasicUtil.convertPojoObjectToJsonStr(conditionRes);
					FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestCondition, accessToken, conditionResJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
					//String createdResourceConditionResponseBody = FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestCondition, accessToken, conditionRes.toString(), fhirServerBaseUrl, tenantID, clientID, clientSecret);
					//conditionResourceIdentifier = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(createdResourceConditionResponseBody);
					//conditionResourceFullUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_CONDITION + APIConstants.REQUEST_SEPARATOR + createdResourceIdentifierCondition;
					 							
				}
				//conditionResourceFullUrl = APIConstants.FHIR_RESOURCE_CONDITION + APIConstants.REQUEST_SEPARATOR + conditionResourceIdentifier;
			}
			
			//check if FHIR resource is 'Medication
			MedicationRes medicationRes = resourceResponseObj.getMedication();
			
			// Check if Medication already exists on the FHIR server using identifier value
			String identifierValueMedication = medicationRes.getIdentifier().get(0).getValue();
			String fhirResourceQueryUrlMedication = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_MEDICATION + "?identifier=" + identifierValueMedication;
			String fhirResourceResBodyMedication = FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceQueryUrlMedication, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
			Object resourceResObjMedication =  FHIRUtil.getResourceObjectFromResponse(fhirResourceResBodyMedication);

			if(resourceResObjMedication == null) {
			
				String resourceUrlforCreateRequestMedication = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_MEDICATION;
				String medicationResJsonStr = BasicUtil.convertPojoObjectToJsonStr(medicationRes);
				String createdResourceMedicationResponseBody = FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestMedication, accessToken, medicationResJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
				medicationResourceIdentifier = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(createdResourceMedicationResponseBody);
				 
			}else {
				String medicationResJsonStr = BasicUtil.convertPojoObjectToJsonStr(resourceResObjMedication);
				medicationResourceIdentifier = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(medicationResJsonStr);
				
			}
			medicationResourceFullUrl = APIConstants.FHIR_RESOURCE_MEDICATION + APIConstants.REQUEST_SEPARATOR + medicationResourceIdentifier;
				
				
			//check if FHIR resource is MedicationRequest
			MedicationRequestRes medicationRequestRes = resourceResponseObj.getMedicationRequest();
				
			//check if Medication Request already exists on the FHIR server using Prescription number
			String identifierValueMedicationRequest = medicationRequestRes.getIdentifier().get(0).getValue();
			String fhirResourceQueryUrlMedicationRequest = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_MEDICATION_REQUEST + "?identifier=" + identifierValueMedicationRequest;
			String fhirResourceResBodyMedicationRequest = FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceQueryUrlMedicationRequest, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
			Object resourceResObjMedicationRequest =  FHIRUtil.getResourceObjectFromResponse(fhirResourceResBodyMedicationRequest);
			
			if(resourceResObjMedicationRequest == null) {
				//Create Medication Request on FHIR server
				String resourceUrlforCreateRequestMedicationRequest = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_MEDICATION_REQUEST;
				
				//Update the JSON with Medication, Patient, Encounter(hospitalization), Practitioner Full URL details
				medicationRequestRes.getMedicationReference().setReference(medicationResourceFullUrl);
				medicationRequestRes.getMedicationReference().getIdentifier().setValue(medicationResourceIdentifier);
				
				medicationRequestRes.getSubject().setReference(patientResourceFullUrl);
				medicationRequestRes.getSubject().getIdentifier().setValue(patientResourceIdentifier);
				
				medicationRequestRes.getEncounter().setReference(encounterResourceFullUrl);
				medicationRequestRes.getEncounter().getIdentifier().setValue(encounterResourceIdentifier);
				
				medicationRequestRes.getRecorder().setReference(practitionerResourceFullUrl);
				medicationRequestRes.getRecorder().getIdentifier().setValue(practitionerResourceIdentifier);
				
				String medicationRequestResJsonStr = BasicUtil.convertPojoObjectToJsonStr(medicationRequestRes);
				String createdResourceMedicationRequestResponseBody  = FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestMedicationRequest, accessToken, medicationRequestResJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
				medicationRequestResourceIdentifier = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(createdResourceMedicationRequestResponseBody);
				
			}else {
				String medicationRequestResJsonStr = BasicUtil.convertPojoObjectToJsonStr(resourceResObjMedicationRequest);
				medicationRequestResourceIdentifier = FHIRUtil.getFHIRResourceIdentifierFromResponseBody(medicationRequestResJsonStr);
			}
			medicationRequestResourceFullUrl = APIConstants.FHIR_RESOURCE_MEDICATION_REQUEST + APIConstants.REQUEST_SEPARATOR + medicationRequestResourceIdentifier;;
			
			//check if FHIR resource is MedicationDispense
			MedicationDispenseRes medicationDispenseRes = resourceResponseObj.getMedicationDispense();		
			//Create Medication Dispense on FHIR server
			String resourceUrlforCreateRequestMedicationDispense = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_MEDICATION_DISPENSE;
			
			//Update the JSON with Patient, Medication, MedicationRequest Full URL details
			medicationDispenseRes.getSubject().setReference(patientResourceFullUrl);
			medicationDispenseRes.getSubject().getIdentifier().setValue(patientResourceIdentifier);
			
			medicationDispenseRes.getMedicationReference().setReference(medicationResourceFullUrl);
			medicationDispenseRes.getMedicationReference().getIdentifier().setValue(medicationResourceIdentifier);
			
			medicationDispenseRes.getAuthorizingPrescription().get(0).setReference(medicationRequestResourceFullUrl);
			medicationDispenseRes.getAuthorizingPrescription().get(0).getIdentifier().setValue(medicationRequestResourceIdentifier);
			
			String medicationDispenseResJsonStr = BasicUtil.convertPojoObjectToJsonStr(medicationDispenseRes);
			FHIRUtil.createFHIRResourceOnFHIRServer(resourceUrlforCreateRequestMedicationDispense, accessToken, medicationDispenseResJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret);
			
			LOGGER.info("End of one record uploaded to FHIR server");
		}
		
	}


	@Override
	public List<ResourceResList> downloadFHIRResourcesObjFromFHIRServer(String fhirResourceRequestUrl, List<ResourceResList> fhirResourcesListToPopulate, String fhirServerBaseUrl) {

		clearCache();
		if(!BasicUtil.isStringNull(fhirResourceRequestUrl)) {
			
			String accessToken, tenantID, clientID, clientSecret, accessTokenKeyName = "";
			
			if(fhirServerBaseUrl.equalsIgnoreCase(APIConstants.AZURE_PRIVATE_FHIR_SERVER_BASE_URL)) {
				
				tenantID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_AZURE_ACTIVE_DIRECTORY_TENANT_ID);
				clientID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_PRIVATE_FHIR_SERVER_CLIENT_ID);
				clientSecret = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_PRIVATE_FHIR_SERVER_CLIENT_SECRET);		
				accessTokenKeyName =  APIConstants.AZURE_PRIVATE_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME;
				
			}else {
				
				tenantID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_AZURE_ACTIVE_DIRECTORY_TENANT_ID);
				clientID = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_SYNTHIR_FHIR_SERVER_CLIENT_ID);
				clientSecret = environment.getProperty(APIConstants.APPLICATION_CONFIG_KEY_NAME_SYNTHIR_FHIR_SERVER_CLIENT_SECRET);
				accessTokenKeyName =  APIConstants.AZURE_SYNTHIR_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME;

			}
			//Get access token based on FHIR server URL
			//get from cache 
			Object accessTokenCacheObject = cacheManager.getCache("fhirServer_accessToken").get(accessTokenKeyName);
			
			if(accessTokenCacheObject == null) {
				
				//get access token from Azure AD based on FHIR server URL and save the data into cache
				accessToken = FHIRUtil.getAccessTokenFromAzureActiveDirectory(tenantID, fhirServerBaseUrl, clientID, clientSecret);
				
			}else {
				
				
				accessToken = fhirServerBaseUrl.equalsIgnoreCase(APIConstants.AZURE_PRIVATE_FHIR_SERVER_BASE_URL) ? cacheManager.getCache("fhirServer_accessToken").get(APIConstants.AZURE_PRIVATE_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME).toString() : 
						cacheManager.getCache("fhirServer_accessToken").get(APIConstants.AZURE_SYNTHIR_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME).toString();
			}
			//System.out.println("Token from cache ::" + cacheManager.getCache("fhirServer_accessToken").get(accessTokenKeyName));
			
			//System.out.println(cacheManager.getCache("fhiraccesstoken"));
			//First get all the encounters (which has hospitalizations) with respect to a hospital server
			String responseBody =  FHIRUtil.getFHIRResourceFromFHIRServer(fhirResourceRequestUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
			
			if(!BasicUtil.isStringNull(responseBody)) {
					
					ObjectMapper objectMapper = new ObjectMapper();
					
					try {
						JsonNode jsonObjNode = objectMapper.readTree(responseBody);
						JsonNode jsonNodeEntryObj = jsonObjNode.get("entry");
						if(jsonNodeEntryObj != null) {
									
								String entryObject = jsonNodeEntryObj.toString();
								List<FHIRResourceResponse> fhirResourceResponseList = objectMapper.readValue(entryObject, new TypeReference<List<FHIRResourceResponse>>(){});
								
								for(FHIRResourceResponse encounterResourceResponseObj : fhirResourceResponseList) {
									
									String encounterResJsonStr = BasicUtil.convertPojoObjectToJsonStr(encounterResourceResponseObj.getResource());
									EncounterRes encounterRes = objectMapper.readValue(encounterResJsonStr, EncounterRes.class);
									//Get MedicationRequest on the basis of Encounter identifier. Since there will be multiple medication request for each encounter
									String encounterResourceIdentifier = encounterRes.getId();
									String medicationRequestResourceRequestUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_MEDICATION_REQUEST +"?encounter=" + encounterResourceIdentifier;
									String medicationRequestListResponseBody =  FHIRUtil.getFHIRResourceFromFHIRServer(medicationRequestResourceRequestUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
									//List of MedicationRequests
									JsonNode medicationRequestResponseJsonNode = objectMapper.readTree(medicationRequestListResponseBody);
									JsonNode medicationRequestResponseEntryJsonObject = medicationRequestResponseJsonNode.get("entry");
									
									if(medicationRequestResponseEntryJsonObject != null) {
										
										String medicationRequestResponseEntryObject = medicationRequestResponseEntryJsonObject.toString();
										List<FHIRResourceResponse> medicationRequestResourceResponseList = objectMapper.readValue(medicationRequestResponseEntryObject, new TypeReference<List<FHIRResourceResponse>>(){});
										
										for(FHIRResourceResponse medicationRequestResourceResponseObj : medicationRequestResourceResponseList) {
										
											ResourceResList resourceResList = new ResourceResList();
											
											//Fetch details from the Encounter FHIR resource
											resourceResList.setEncounter(encounterRes);
											
											//From each FHIR resource 'Encounter', get Patient, Practitioner, Condition and Location details
											String patientResourceIdentifier = encounterRes.getSubject().getIdentifier().getValue();
											String patientResourceFullUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_PATIENT + APIConstants.REQUEST_SEPARATOR + patientResourceIdentifier;
											String fhirResourceResBodyPatient = FHIRUtil.getFHIRResourceFromFHIRServer(patientResourceFullUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
											PatientRes patientRes = objectMapper.readValue(fhirResourceResBodyPatient, PatientRes.class);
											resourceResList.setPatient(patientRes);
											
											String practitionerResourceIdentifier = encounterRes.getParticipant().get(0).getIndividual().getIdentifier().getValue();
											String practitionerResourceFullUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_PRACTITIONER + APIConstants.REQUEST_SEPARATOR + practitionerResourceIdentifier;
											String fhirResourceResBodyPractitioner = FHIRUtil.getFHIRResourceFromFHIRServer(practitionerResourceFullUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
											PractitionerRes practitionerRes = objectMapper.readValue(fhirResourceResBodyPractitioner, PractitionerRes.class);
											resourceResList.setPractitioner(practitionerRes);
											//check it Location exists for the Encounter
											String locationResourceIdentifier = encounterRes.getLocation().get(0).getLocation().getIdentifier().getValue();
											
											if(!(BasicUtil.isStringBlank(locationResourceIdentifier) || BasicUtil.isStringEmpty(locationResourceIdentifier) || BasicUtil.isStringNull(locationResourceIdentifier))) {
												
												String LocationResourceFullUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_LOCATION + APIConstants.REQUEST_SEPARATOR + locationResourceIdentifier;
												String fhirResourceResBodyLocation = FHIRUtil.getFHIRResourceFromFHIRServer(LocationResourceFullUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
												LocationRes locationRes = objectMapper.readValue(fhirResourceResBodyLocation, LocationRes.class);
												resourceResList.setLocation(locationRes);
												
											}
											
											//Condition
											String conditionResourceRequestUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_CONDITION +"?subject=" + patientResourceIdentifier + "&encounter=" + encounterResourceIdentifier;
											String fhirResourceResBodyConditionList = FHIRUtil.getFHIRResourceFromFHIRServer(conditionResourceRequestUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);;
											JsonNode conditionResponseJsonObjNode = objectMapper.readTree(fhirResourceResBodyConditionList);
											//JsonNode conditionResponseEntryObject = conditionResponseJsonObjNode.get("entry");
											
											//if(conditionResponseEntryObject!= null) {
												String conditionResponseEntryObjectStr = conditionResponseJsonObjNode.get("entry").toString();
												List<FHIRResourceResponse> conditionResponseList = objectMapper.readValue(conditionResponseEntryObjectStr, new TypeReference<List<FHIRResourceResponse>>(){});
												String fhirResourceResBodyCondition = BasicUtil.convertPojoObjectToJsonStr(conditionResponseList.get(0).getResource());
												ConditionRes conditionRes  = objectMapper.readValue(fhirResourceResBodyCondition, ConditionRes.class);
												resourceResList.setCondition(conditionRes);
											//}
											
											
											
											//Medication Request
											String fhirResourceResBodyMedicationRequest  = BasicUtil.convertPojoObjectToJsonStr(medicationRequestResourceResponseObj.getResource());
											MedicationRequestRes medicationRequestRes = objectMapper.readValue(fhirResourceResBodyMedicationRequest, MedicationRequestRes.class);
											resourceResList.setMedicationRequest(medicationRequestRes);
											
											//Medication
											String medicationResourceIdentifier = medicationRequestRes.getMedicationReference().getIdentifier().getValue();
											String medicationResourceFullUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_MEDICATION + APIConstants.REQUEST_SEPARATOR + medicationResourceIdentifier;
											String fhirResourceResBodyMedication = FHIRUtil.getFHIRResourceFromFHIRServer(medicationResourceFullUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);
											MedicationRes medicationRes = objectMapper.readValue(fhirResourceResBodyMedication, MedicationRes.class);
											resourceResList.setMedication(medicationRes);
											
											//Fetch Medication Dispensed. Medication dispensed (FHIR resource) for a Medication. It is obtained with medicationRequest identifier as the query parameter
											String medicationDispenseResourceRequestUrl = fhirServerBaseUrl + APIConstants.REQUEST_SEPARATOR + APIConstants.FHIR_RESOURCE_MEDICATION_DISPENSE +"?prescription=" + medicationRequestRes.getId();
											String fhirResourceResBodyMedicationDispensedList = FHIRUtil.getFHIRResourceFromFHIRServer(medicationDispenseResourceRequestUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret);;
											JsonNode medicationDispensedResponseJsonObjNode = objectMapper.readTree(fhirResourceResBodyMedicationDispensedList);
											//JsonNode medicationDispensedResponseEntryObject = medicationDispensedResponseJsonObjNode.get("entry");
											//if(medicationDispensedResponseEntryObject != null) {
												String medicationDispensedResponseEntryObjectStr = medicationDispensedResponseJsonObjNode.get("entry").toString();
												List<FHIRResourceResponse> medicationDispensedResponseList = objectMapper.readValue(medicationDispensedResponseEntryObjectStr, new TypeReference<List<FHIRResourceResponse>>(){});
												String fhirResourceResBodyMedicationDispensed = BasicUtil.convertPojoObjectToJsonStr(medicationDispensedResponseList.get(0).getResource());
												MedicationDispenseRes medicationDispenseRes = objectMapper.readValue(fhirResourceResBodyMedicationDispensed, MedicationDispenseRes.class);
												resourceResList.setMedicationDispense(medicationDispenseRes);
											//}
											
											//Add to the final FHIR resource list
											fhirResourcesListToPopulate.add(resourceResList);
											
									}			
											
								}
							} 
								
							{
								//check relation attribute in link to identify if there are more resources
								//the value of relation attribute will be next if there are more resources, else self
								String linkRelationValue = jsonObjNode.get("link").get(0).get("relation").textValue();
								fhirResourceRequestUrl = (linkRelationValue.equalsIgnoreCase("next")) ? jsonObjNode.get("link").get(0).get("url").textValue() : null;
								
								if (!BasicUtil.isStringNull(fhirResourceRequestUrl)) {
									LOGGER.info("Recuresively calling next request URL to fetch resources from FHIR server {}", fhirResourceRequestUrl);
									//Call the method recursively
									downloadFHIRResourcesObjFromFHIRServer(fhirResourceRequestUrl, fhirResourcesListToPopulate, fhirServerBaseUrl);
								}
								
							}
						
						}
						
					}catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}

					LOGGER.info("FHIR resources downloaded from FHIR server {}", fhirResourcesListToPopulate);
				
				}
			
		}
		
		//clear cache 
		clearCache();
		return fhirResourcesListToPopulate;
	}
	
	
	
	
}
