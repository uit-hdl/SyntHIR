package no.uit.syntHIR.dataWrangling.engine;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import no.uit.syntHIR.dataWrangling.FHIRResource.request.AddressReq;
import no.uit.syntHIR.dataWrangling.FHIRResource.request.ConditionReq;
import no.uit.syntHIR.dataWrangling.FHIRResource.request.EncounterReq;
import no.uit.syntHIR.dataWrangling.FHIRResource.request.LocationReq;
import no.uit.syntHIR.dataWrangling.FHIRResource.request.MedicationDispenseReq;
import no.uit.syntHIR.dataWrangling.FHIRResource.request.MedicationReq;
import no.uit.syntHIR.dataWrangling.FHIRResource.request.MedicationRequestReq;
import no.uit.syntHIR.dataWrangling.FHIRResource.request.PatientReq;
import no.uit.syntHIR.dataWrangling.FHIRResource.request.PractitionerReq;
import no.uit.syntHIR.dataWrangling.FHIRResource.response.ConditionRes;
import no.uit.syntHIR.dataWrangling.FHIRResource.response.EncounterRes;
import no.uit.syntHIR.dataWrangling.FHIRResource.response.LocationRes;
import no.uit.syntHIR.dataWrangling.FHIRResource.response.MedicationDispenseRes;
import no.uit.syntHIR.dataWrangling.FHIRResource.response.MedicationRequestRes;
import no.uit.syntHIR.dataWrangling.FHIRResource.response.MedicationRes;
import no.uit.syntHIR.dataWrangling.FHIRResource.response.PatientRes;
import no.uit.syntHIR.dataWrangling.FHIRResource.response.PractitionerRes;
import no.uit.syntHIR.dataWrangling.FHIRResource.response.ResourceResList;
import no.uit.syntHIR.dataWrangling.persistence.AzureStorageService;
import no.uit.syntHIR.dataWrangling.registryData.CombinedNPRNorPDPojo;
import no.uit.syntHIR.dataWrangling.util.APIConstants;
import no.uit.syntHIR.dataWrangling.util.BasicConstants;
import no.uit.syntHIR.dataWrangling.util.BasicUtil;
import no.uit.syntHIR.dataWrangling.util.DataWranglingUtil;

@Service
public class DataWranglingEngineImpl implements DataWranglingEngine{
	
	final static Logger LOGGER = LoggerFactory.getLogger(DataWranglingEngineImpl.class);
	
	@Autowired 
	private AzureStorageService azureStorageService;	
	
	/**
	 *
	 * 
	 * Converts CSV file into FHIR resources. Templates are created using handlebar
	 * Each attributes of CSV file is mapped to corresponding FHIR resource and its 
	 * properties. Using openCSV to read the CSV. For each record of the input CSV,
	 * list of FHIR resources (nested JSON object) is created and which is further
	 * put in another List and return to user
	 * 
	 * 
	 * @param CSV file Buffered Reader
	 * @return List<List<String>>
	 * @author pavitra 
	 * 
	 */
	@Override
	public List<ResourceResList> convertCSVToFHIRResourcesJson(BufferedReader csvFileBufferedReader) {

		String templatesLocation = BasicConstants.FHIR_RESOURCES_TEMPLATE_LOCATION;
		TemplateLoader templateLoader = new FileTemplateLoader(templatesLocation, ".hbs");
		Handlebars handlebars = new Handlebars(templateLoader);
		// List of JSON strings for the csv file
		List<ResourceResList> nprNorPDRecords = new ArrayList<ResourceResList>();
		ObjectMapper objectMapper = new ObjectMapper();
		try {

			// Compile templates
			Template encounterTemplate = handlebars.compile("Encounter");
			Template locationTemplate = handlebars.compile("Location");
			Template medicationTemplate = handlebars.compile("Medication");
			Template medicationDispenseTemplate = handlebars.compile("MedicationDispense");
			Template medicationRequestTemplate = handlebars.compile("MedicationRequest");
			Template patientTemplate = handlebars.compile("Patient");
			Template practitionerTemplate = handlebars.compile("Practitioner");
			Template conditionTemplate = handlebars.compile("Condition");

			// Read CSV data file and map them to the beans
			List<CombinedNPRNorPDPojo> combinedNPRNorPDPojos = new ArrayList<CombinedNPRNorPDPojo>();

			combinedNPRNorPDPojos = DataWranglingUtil.readCsvToBeanList(csvFileBufferedReader, CombinedNPRNorPDPojo.class,
					combinedNPRNorPDPojos);

			for (CombinedNPRNorPDPojo combinedNPRNorPDPojo : combinedNPRNorPDPojos) {

				// List with respect to each record
				ResourceResList resourceResList = new ResourceResList();

				// Patient-related attributes
				//Stored in Patient FHIR resource
				String patientId = combinedNPRNorPDPojo.getPatiendId();
				String patientIdType = combinedNPRNorPDPojo.getPatientIdType();
				String patientDateOfBirth = combinedNPRNorPDPojo.getPatientDateOfBirth();
				String patientGender = combinedNPRNorPDPojo.getPatientGender();
				String patientCountyName = combinedNPRNorPDPojo.getPatientCountyName();
				String patientCountyNumber = combinedNPRNorPDPojo.getPatientCountyNumber();
				String patientDeathMonth = combinedNPRNorPDPojo.getPatientDeathMonth();
				String patientDeathYear = combinedNPRNorPDPojo.getPatientDeathYear();
				String patientAgeGroup = combinedNPRNorPDPojo.getPatientAgeGroup();

				// Practitioner-related attributes
				//Stored in Practitioner FHIR resource
				String practitionerId = combinedNPRNorPDPojo.getPractitionerId();
				String practitionerIdType = combinedNPRNorPDPojo.getPractitionerIdType();
				String practitionerGender = combinedNPRNorPDPojo.getPractitionerGender();
				String practitionerDateOfBirth = combinedNPRNorPDPojo.getPractitionerDateOfBirth();

				// Hospitalization-related attributes
				//Stored in Encounter FHIR resource
				String hospitalizationUrgency = combinedNPRNorPDPojo.getHospitalizationUrgency();
				String hospitalizationDischargeLocation = combinedNPRNorPDPojo.getHospitalizationPatientDischargeLocation();
				String hospitalizationLevelOfCare = combinedNPRNorPDPojo.getHospitalizationLevelOfCare();
				String hospitalizationPatientVisitStart = combinedNPRNorPDPojo.getHospitalizationPatientVisitStart();
				String hospitalizationPatientVisitEnd = combinedNPRNorPDPojo.getHospitalizationPatientVisitEnd();
				
				//Stored in Condition FHIR resource
				String hospitalizationMainDiagnosis = combinedNPRNorPDPojo.getHospitalizationMainDiagnosis();
				
				//Stored in Location FHIR resource
				String instituteName = combinedNPRNorPDPojo.getInstituteName();

				// Prescription-related attributes
				
				//Stored in MedicationRequest FHIR resource
				String prescriptionNumber = combinedNPRNorPDPojo.getPrescriptionNumber();
				String prescriptionLegalCategory = combinedNPRNorPDPojo.getPrescriptionLegalCategory();
				String prescriptionLegalCategoryCode = combinedNPRNorPDPojo.getPrescriptionLegalCategoryCode();
				String prescribedDrugATCCodeDDD = combinedNPRNorPDPojo.getPrescribedDrugATCCodeDDD();
				String prescribedDrugATCCodeDDDUnit = combinedNPRNorPDPojo.getPrescribedDrugATCCodeDDDDose();
				
				//Stored in Medication FHIR resource
				String prescribedDrugNumber = combinedNPRNorPDPojo.getPrescribedDrugNumber();
				String prescribedDrugName = combinedNPRNorPDPojo.getPrescribedDrugName();
				String prescribedDrugATCCode = combinedNPRNorPDPojo.getPrescribedDrugATCCode();
				String reimbursementLegalCategory = combinedNPRNorPDPojo.getReimbursementLegalCategory();
				String reimbursementLegalCategoryCode = combinedNPRNorPDPojo.getReimbursementLegalCategoryCode();
				String reimbursementICDCode = combinedNPRNorPDPojo.getReimbursementICDCode();
				String reimbursementICPCCode = combinedNPRNorPDPojo.getReimbursementICPCCode();
				
				
				//Stored in MedicationDispense FHIR resource
				String prescribedDrugDeliveryDate = combinedNPRNorPDPojo.getPrescribedDrugDeliveryDate();
				String numberOfPackagesDispensedForDrug = combinedNPRNorPDPojo.getNumberOfPackagesDispensedForDrug();
				String numberOfPackagesDispensedForDrugDDD = combinedNPRNorPDPojo.getNumberOfPackagesDispensedForDrugDDD();
				//String reimbursementLegalCategory = combinedNPRNorPDPojo.getReimbursementLegalCategory();
				
				/*
				 * For each record in the NPR and NorPD dataset, first create Patient and Practitioner FHIR resource
				 */

				// Populate FHIR resource Patient
				PatientReq fhirResourcePatient = new PatientReq();

				AddressReq fhirResourceAddress = new AddressReq();
				fhirResourceAddress.setAddressCity(patientCountyName);
				
				if(!(BasicUtil.isStringBlank(patientCountyNumber) || BasicUtil.isStringEmpty(patientCountyNumber) || BasicUtil.isStringNull(patientCountyNumber)))
					fhirResourceAddress.setAddressPostalCode(patientCountyNumber.replaceAll("\\.\\d+$", ""));
				fhirResourceAddress.setAddressCountry("Norway");
				
				fhirResourcePatient.setAddress(fhirResourceAddress);
				fhirResourcePatient.setIdentifierValue(patientId);
				
				if(patientIdType == "F")
					fhirResourcePatient.setIdentifierUse("official");  
				else
					fhirResourcePatient.setIdentifierUse("temp");

				String patientGenderFHIR = DataWranglingUtil.convertGenderEncoding(patientGender);
				fhirResourcePatient.setGender(patientGenderFHIR);
				fhirResourcePatient.setDateOfBirth(patientDateOfBirth);
				fhirResourcePatient.setAddress(fhirResourceAddress);
				String deathDateTime = "";
				
				if (!(BasicUtil.isStringBlank(patientDeathYear) || BasicUtil.isStringEmpty(patientDeathYear) || BasicUtil.isStringNull(patientDeathYear))) {
					if (!(BasicUtil.isStringBlank(patientDeathMonth) || BasicUtil.isStringEmpty(patientDeathMonth) || BasicUtil.isStringNull(patientDeathMonth))) {
						String deathMonth = String.format("%02d",
								Integer.parseInt(patientDeathMonth.replaceAll("\\.\\d+$", "")));
						deathDateTime = String.format("%s-%s", patientDeathYear.replaceAll("\\.\\d+$", ""), deathMonth);
					}
				}
				
				fhirResourcePatient.setDeathDateTime(deathDateTime);
				fhirResourcePatient.setAgeGroup(patientAgeGroup);

				// Populate the template and create FHIR resource JSON objects
				String patientJsonFHIRRes = patientTemplate.apply(fhirResourcePatient);
				//Convert Json to Resource DTO object
				PatientRes patienRes = objectMapper.readValue(patientJsonFHIRRes, PatientRes.class);
				resourceResList.setPatient(patienRes);

				// Populate FHIR resource Practitioner which has prescriber details
				PractitionerReq fhirResourcePractitioner = new PractitionerReq();
				
				if(practitionerIdType == "0")
					fhirResourcePractitioner.setIdentifierUse("official");  
				else
					fhirResourcePractitioner.setIdentifierUse("temp");
				
				fhirResourcePractitioner.setIdentifierValue(practitionerId);

				String practitionerGenderFHIR = DataWranglingUtil.convertGenderEncoding(practitionerGender);
				fhirResourcePractitioner.setGender(practitionerGenderFHIR);
				
				if (!(BasicUtil.isStringBlank(practitionerDateOfBirth) || BasicUtil.isStringEmpty(practitionerDateOfBirth) || BasicUtil.isStringNull(practitionerDateOfBirth))) {
						practitionerDateOfBirth = String.format("%.0f", Double.parseDouble(practitionerDateOfBirth));
						fhirResourcePractitioner.setBirthDate(practitionerDateOfBirth);
				}

				// Populate the template and create FHIR resource JSON objects
				String practitionerJsonFHIRRes = practitionerTemplate.apply(fhirResourcePractitioner);
				//Convert Json to Resource DTO object
				PractitionerRes practitionerRes = objectMapper.readValue(practitionerJsonFHIRRes, PractitionerRes.class);
				resourceResList.setPractitioner(practitionerRes);

				// Populate FHIR resource Location which has Hospital Name
				LocationReq fhirResourceLocation = new LocationReq();
				fhirResourceLocation.setInstituteName(instituteName);

				// Populate the template and create FHIR resource JSON objects
				String locationJsonFHIRRes = locationTemplate.apply(fhirResourceLocation);
				//Convert Json to Resource DTO object
				LocationRes locationRes = objectMapper.readValue(locationJsonFHIRRes, LocationRes.class);
				resourceResList.setLocation(locationRes);

				// Populate FHIR resource Encounter which has hospitalization details
				EncounterReq fhirResourceEncounter = new EncounterReq();
				{
					String levelOfCare = "";
					if (!(BasicUtil.isStringBlank(hospitalizationLevelOfCare) || BasicUtil.isStringEmpty(hospitalizationLevelOfCare) || BasicUtil.isStringNull(hospitalizationLevelOfCare))) 
						levelOfCare = Integer.parseInt(hospitalizationLevelOfCare) == 1 ? "in-progress" : "finished";
					fhirResourceEncounter.setLevelOfCare(levelOfCare);
				}
				
				{
					String levelOfUrgency = "";
					if (!(BasicUtil.isStringBlank(hospitalizationUrgency) || BasicUtil.isStringEmpty(hospitalizationUrgency) || BasicUtil.isStringNull(hospitalizationUrgency)))
						levelOfUrgency = Integer.parseInt(hospitalizationUrgency) == 1 ? "EMER" : "PRENC";
					fhirResourceEncounter.setLevelOfUrgency(levelOfUrgency);
				}
				
				
				fhirResourceEncounter.setAdmissionStartDate(hospitalizationPatientVisitStart);
				fhirResourceEncounter.setAdmissionEndDate(hospitalizationPatientVisitEnd);
				
				
				if(!(BasicUtil.isStringBlank(hospitalizationPatientVisitStart) || BasicUtil.isStringEmpty(hospitalizationPatientVisitStart) || BasicUtil.isStringNull(hospitalizationPatientVisitStart)) &&
						!(BasicUtil.isStringBlank(hospitalizationPatientVisitEnd) || BasicUtil.isStringEmpty(hospitalizationPatientVisitEnd) || BasicUtil.isStringNull(hospitalizationPatientVisitEnd))) {
					
					DateTime startDate = BasicUtil.convertStringDateToDateTimeFormat(hospitalizationPatientVisitStart,"yyyy-MM-dd");
					DateTime endDate = BasicUtil.convertStringDateToDateTimeFormat(hospitalizationPatientVisitEnd,"yyyy-MM-dd");		
					
					fhirResourceEncounter.setHospitalizationVisitLength(BasicUtil.differenceBetweenDates(startDate, endDate));
				}	
				
				// Populate FHIR resource Condition
				ConditionReq fhirResourceCondition = new ConditionReq();
				fhirResourceCondition.setIcd10Code(hospitalizationMainDiagnosis);

				// Populate the template and create FHIR resource JSON objects
				String conditionJsonFHIRRes = conditionTemplate.apply(fhirResourceCondition);
				//Convert Json to Resource DTO object
				ConditionRes consitionRes = objectMapper.readValue(conditionJsonFHIRRes, ConditionRes.class);
				resourceResList.setCondition(consitionRes);


				{
					String fhirCodeDischargeLocation = "";
					String dischargeLocation = "";

					if (!(BasicUtil.isStringBlank(hospitalizationDischargeLocation) || BasicUtil.isStringEmpty(hospitalizationDischargeLocation) || BasicUtil.isStringNull(hospitalizationDischargeLocation))) {
						
						int dischargeLocInt = Integer.parseInt(hospitalizationDischargeLocation);
						
						fhirCodeDischargeLocation = (dischargeLocInt == 1 ? "Other healthcare facility" : dischargeLocInt == 2 ? "Skilled nursing facility" : "Other");
						dischargeLocation = (dischargeLocInt == 1 ? "Another Medical Institution" : dischargeLocInt == 2 ? "Nursing homes, a municipal Unit" : "Others");
					}

					fhirResourceEncounter.setFhirCodeForDischargeLocation(fhirCodeDischargeLocation);
					fhirResourceEncounter.setDischargeLocation(dischargeLocation);
				}

				// Populate the template and create FHIR resource JSON objects
				String encounterJsonFHIRRes = encounterTemplate.apply(fhirResourceEncounter);
				//Convert Json to Resource DTO object
				EncounterRes encounterRes = objectMapper.readValue(encounterJsonFHIRRes, EncounterRes.class);
				resourceResList.setEncounter(encounterRes);

				// Populate MedicationRequest, Medication and MedicationDispense which has prescription details
				MedicationReq fhirResourceMedication = new MedicationReq();

				fhirResourceMedication.setMedicationNumber(prescribedDrugNumber);
				fhirResourceMedication.setMedicationATCCode(prescribedDrugATCCode);
				fhirResourceMedication.setMedicationName(prescribedDrugName);

				// Populate the template and create FHIR resource JSON objects
				String medicationJsonFHIRRes = medicationTemplate.apply(fhirResourceMedication);
				//Convert Json to Resource DTO object
				MedicationRes medicationRes = objectMapper.readValue(medicationJsonFHIRRes, MedicationRes.class);
				resourceResList.setMedication(medicationRes);

				MedicationRequestReq fhirResourceMedicationRequest = new MedicationRequestReq();

				fhirResourceMedicationRequest.setPrescriptionNumber(prescriptionNumber);
				fhirResourceMedicationRequest.setPrescriptionCategoryCode(prescriptionLegalCategoryCode);
				fhirResourceMedicationRequest.setPrescriptionCategory(prescriptionLegalCategory);
				
				fhirResourceMedicationRequest.setReimbursementLegalCategory(reimbursementLegalCategory);
				fhirResourceMedicationRequest.setReimbursementLegalCategoryCode(reimbursementLegalCategoryCode);
				
				{
					//set ICD or ICPC Code
					if(!(BasicUtil.isStringBlank(reimbursementICDCode) || BasicUtil.isStringEmpty(reimbursementICDCode) || BasicUtil.isStringNull(reimbursementICDCode)))
						fhirResourceMedicationRequest.setReimbursementIcdIcpcCode("ICD:" + reimbursementICDCode);
					else
						fhirResourceMedicationRequest.setReimbursementIcdIcpcCode("ICPC:" + reimbursementICPCCode);
						
				}
				fhirResourceMedicationRequest.setMedicationResourceUrl("");
				fhirResourceMedicationRequest.setPatientResourceUrl("");
				fhirResourceMedicationRequest.setEncounterResourceUrl("");
				fhirResourceMedicationRequest.setPractitionerResourceUrl("");

				if (!(BasicUtil.isStringBlank(prescribedDrugATCCodeDDD) || BasicUtil.isStringEmpty(prescribedDrugATCCodeDDD) || BasicUtil.isStringNull(prescribedDrugATCCodeDDD))) {
					
					String formattedDDD = BasicUtil.replaceDotWithComma(prescribedDrugATCCodeDDD);
					fhirResourceMedicationRequest.setPrescriptionDrugDosage(formattedDDD);

				}

				fhirResourceMedicationRequest.setPrescriptionDrugDosageUnit(prescribedDrugATCCodeDDDUnit);
				// Populate the template and create FHIR resource JSON objects
				String medicationRequestJsonFHIRRes = medicationRequestTemplate.apply(fhirResourceMedicationRequest);
				//Convert Json to Resource DTO object
				MedicationRequestRes medicationRequestRes = objectMapper.readValue(medicationRequestJsonFHIRRes, MedicationRequestRes.class);
				resourceResList.setMedicationRequest(medicationRequestRes);

				MedicationDispenseReq fhirResourceMedicationDispense = new MedicationDispenseReq();

				if (!(BasicUtil.isStringBlank(numberOfPackagesDispensedForDrug) || BasicUtil.isStringEmpty(numberOfPackagesDispensedForDrug) || BasicUtil.isStringNull(numberOfPackagesDispensedForDrug))) {
					
					String formattedNumberOfPackagesDispensed = BasicUtil.replaceDotWithComma(numberOfPackagesDispensedForDrug);
					fhirResourceMedicationDispense.setMedicationPackagesDispensed(formattedNumberOfPackagesDispensed);

				}

				if (!(BasicUtil.isStringBlank(numberOfPackagesDispensedForDrugDDD) || BasicUtil.isStringEmpty(numberOfPackagesDispensedForDrugDDD) || BasicUtil.isStringNull(numberOfPackagesDispensedForDrugDDD))) {
					
					String formattedNumberOfPackagesDispensedDDD = BasicUtil.replaceDotWithComma(numberOfPackagesDispensedForDrugDDD);
					fhirResourceMedicationDispense.setDddMedicationPackagesDispensed(formattedNumberOfPackagesDispensedDDD);

				}

				fhirResourceMedicationDispense.setDayYearOfDispense(prescribedDrugDeliveryDate); // update with anonymized dispensed day which is negative

				// Populate the template and create FHIR resource JSON objects
				String medicationDispenseJsonFHIRRes = medicationDispenseTemplate.apply(fhirResourceMedicationDispense);
				//Convert Json to Resource DTO object
				MedicationDispenseRes medicationDispenseRes = objectMapper.readValue(medicationDispenseJsonFHIRRes, MedicationDispenseRes.class);
				resourceResList.setMedicationDispense(medicationDispenseRes);

				// add to the master list, which will contain list of records
				nprNorPDRecords.add(resourceResList);

			}
			
			LOGGER.info("List of FHIR resources from the CSV {}", nprNorPDRecords);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nprNorPDRecords;

	}

	
	/**
	 * Converts the list of FHIR resources (List<List<String>>)
	 * to CSV file. It reads the each FHIR resource JSON object,
	 * identifies the resource type and maps it the corresponding
	 * field of the CSV file. 
	 * 
	 * 
	 * @param List of Json objects as List<List<String>>
	 * @return Byte array output stream of CSV file
	 *  
	 */
	@Override
	public ByteArrayOutputStream convertFHIRResourcesJsonToCSV(List<List<String>> fhirResourceRecords) {
		
		List<CombinedNPRNorPDPojo> combinedNPRNorPDPojoList = new ArrayList<CombinedNPRNorPDPojo>();
		
		for(List<String> fhirResourceRecord : fhirResourceRecords) {
			
			LOGGER.info("Single record with list of FHIR resources {}", fhirResourceRecord);

			CombinedNPRNorPDPojo combinedNPRNorPDPojo = new CombinedNPRNorPDPojo();
			
			for(String fhirResourceRecordStr : fhirResourceRecord) {
				
				if(!(BasicUtil.isStringBlank(fhirResourceRecordStr) || BasicUtil.isStringEmpty(fhirResourceRecordStr) || BasicUtil.isStringNull(fhirResourceRecordStr))) {
					
					ObjectMapper objectMapper = new ObjectMapper();
					
					try {
						
						JsonNode jsonObjNode = objectMapper.readTree(fhirResourceRecordStr);
						String fhirResourceType = jsonObjNode.get("resourceType").textValue();					
						
						LOGGER.info("FHIR resource type {}", fhirResourceType);
						LOGGER.info("FHIR resource downloaded {}", jsonObjNode.toString());
						
						//#1 check if FHIR resource type is 'Patient'
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_PATIENT)) {
							
							String patientGender =  BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("gender"));
							String patientDateOfBirth = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("birthDate"));
							String patientIdentifierValue = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("identifier").get(0).get("value"));
							String patientIdentifierType = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("identifier").get(0).get("type").get("text"));
							
							// Empty check on death date time, since it can be empty
							String patientDeathDateTime = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("deceasedDateTime"));
							
							String patientPostalCode = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("address").get(0).get("postalCode"));
							String patientCity = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("address").get(0).get("city"));
							//String patientCountry = jsonObjNode.get("address").get(0).get("country").textValue();
							
							JsonNode patientAgeGroupExtensionObj = jsonObjNode.get("extension");
							
							// Null/Empty/Blank check on patient age group
							if(patientAgeGroupExtensionObj != null){
								
								patientAgeGroupExtensionObj = patientAgeGroupExtensionObj.get(0);
								
								if(patientAgeGroupExtensionObj != null){
									
									String patientAgeGroup = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(patientAgeGroupExtensionObj.get("valueString"));
									combinedNPRNorPDPojo.setPatientAgeGroup(patientAgeGroup);
								}
							}
							
							combinedNPRNorPDPojo.setPatientGender(patientGender);
							combinedNPRNorPDPojo.setPatientDateOfBirth(patientDateOfBirth);
							combinedNPRNorPDPojo.setPatiendId(patientIdentifierValue);
							combinedNPRNorPDPojo.setPatientIdType(patientIdentifierType);
							combinedNPRNorPDPojo.setPatientCountyName(patientCity);
							combinedNPRNorPDPojo.setPatientCountyNumber(patientPostalCode);
							
							
							// Null/Empty/Blank check on patient death date-time
							if(!(BasicUtil.isStringBlank(patientDeathDateTime) || BasicUtil.isStringEmpty(patientDeathDateTime) || BasicUtil.isStringNull(patientDeathDateTime))){
								
								DateTime patientDeathDateTimeDt = BasicUtil.convertStringDateToDateTimeFormat(patientDeathDateTime, "yyyy-MM");
								combinedNPRNorPDPojo.setPatientDeathMonth(String.valueOf(patientDeathDateTimeDt.getMonthOfYear()));
								combinedNPRNorPDPojo.setPatientDeathYear(String.valueOf(patientDeathDateTimeDt.getYear()));
							}
						}
						
						//#2 check if FHIR resource type is 'Practitioner'
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_PRACTITIONER)) {
						
							//Empty and null check on Gender and date of birth, since these can have negative values
							
							String practitionerGender = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("gender"));
							String practitionerDateOfBirth = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("birthDate"));
							String practitionerIdentifierValue = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("identifier").get(0).get("value"));
							String practitionerIdentifierType = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("identifier").get(0).get("type").get("text"));
							
							combinedNPRNorPDPojo.setPractitionerGender(practitionerGender);
							combinedNPRNorPDPojo.setPractitionerDateOfBirth(practitionerDateOfBirth);
							combinedNPRNorPDPojo.setPractitionerId(practitionerIdentifierValue);
							combinedNPRNorPDPojo.setPractitionerIdType(practitionerIdentifierType);
							
						}
						
						//#3 check if FHIR resource type is 'Location'
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_LOCATION)) {
							
							String locationName = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("name"));
							combinedNPRNorPDPojo.setInstituteName(locationName);
						}
						
						//#4 check if FHIR resource type is 'Condition'
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_CONDITION)) {
							
							String diagnosisCode = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("code").get("coding").get(0).get("code"));
							combinedNPRNorPDPojo.setHospitalizationMainDiagnosis(diagnosisCode);
						}
						
						//#5 check if FHIR resource type is 'Encounter'
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_ENCOUNTER)) {
							
							String levelOfCare = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("status"));
							if(!(BasicUtil.isStringBlank(levelOfCare) || BasicUtil.isStringEmpty(levelOfCare) || BasicUtil.isStringNull(levelOfCare)))
								levelOfCare = levelOfCare.equals("in-progress") ? "1" : "2";
							
							String levelOfUrgency = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("priority").get("coding").get(0).get("code"));
							if(!(BasicUtil.isStringBlank(levelOfUrgency) || BasicUtil.isStringEmpty(levelOfUrgency) || BasicUtil.isStringNull(levelOfUrgency)))
								levelOfUrgency = levelOfUrgency.equals("EMER") ? "1" : "2";
							
							String hospitalizationStartDate = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("period").get("start"));
							String hospitalizationEndDate = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("period").get("end"));
							
							String hospitalizationDischargeLocation = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("hospitalization").get("dischargeDisposition").get("text"));
							
							if(!(BasicUtil.isStringBlank(hospitalizationDischargeLocation) || BasicUtil.isStringEmpty(hospitalizationDischargeLocation) || BasicUtil.isStringNull(hospitalizationDischargeLocation)))
								hospitalizationDischargeLocation = (hospitalizationDischargeLocation.equals("Institusjon or Sykehjem, kommunal enhet") ? "1" : hospitalizationDischargeLocation.equals("Nursing homes, a municipal Unit") ? "2"  : "3");
							
							
							combinedNPRNorPDPojo.setHospitalizationLevelOfCare(levelOfCare);
							combinedNPRNorPDPojo.setHospitalizationUrgency(levelOfUrgency);
							combinedNPRNorPDPojo.setHospitalizationPatientVisitStart(hospitalizationStartDate);
							combinedNPRNorPDPojo.setHospitalizationPatientVisitEnd(hospitalizationEndDate);
							combinedNPRNorPDPojo.setHospitalizationPatientDischargeLocation(hospitalizationDischargeLocation);
							
						}
						
						//#6 check if FHIR resource type is 'Medication'
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_MEDICATION)) {
						
							String prescribedDrugNumber = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("identifier").get(0).get("value"));
							String prescribedDrugATCCode = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("code").get("coding").get(0).get("code"));
							String prescribedDrugName = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("code").get("text"));
							
							combinedNPRNorPDPojo.setPrescribedDrugNumber(prescribedDrugNumber);
							combinedNPRNorPDPojo.setPrescribedDrugName(prescribedDrugName);
							combinedNPRNorPDPojo.setPrescribedDrugATCCode(prescribedDrugATCCode);
							
						}
						
						//#7 check if FHIR resource type is 'Medication Request'
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_MEDICATION_REQUEST)) {
							
							String prescriptionNumber = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("identifier").get(0).get("value"));
							String prescriptionLegalCategory = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("category").get(0).get("text"));;
							String prescriptionLegalCategoryCode = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("category").get(0).get("coding").get(0).get("code"));
							
							JsonNode reimburesementLegalCategoryObj = jsonObjNode.get("note").get(0).get("text");
							JsonNode reimbursementLegalCategoryCodeObj = jsonObjNode.get("note").get(1).get("text");
							
							combinedNPRNorPDPojo.setReimbursementLegalCategory(BasicUtil.checkIfJsonNodeObjectValueIsEmpty(reimburesementLegalCategoryObj));
							combinedNPRNorPDPojo.setReimbursementLegalCategoryCode(BasicUtil.checkIfJsonNodeObjectValueIsEmpty(reimbursementLegalCategoryCodeObj));
							
							
							String reimbursementICDCode = "";
							String reimbursementICPCCode = "";
							{
								String reimbursementICDIPCPCCode = BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("note").get(2).get("text"));
								
								if(!(BasicUtil.isStringBlank(reimbursementICDIPCPCCode) || BasicUtil.isStringEmpty(reimbursementICDIPCPCCode) || BasicUtil.isStringNull(reimbursementICDIPCPCCode))) {
									String[] reimbursementSplitArr = reimbursementICDIPCPCCode.split("\\:");
									
									if(reimbursementSplitArr.length > 1) {
										if(reimbursementSplitArr[0].contains("ICD"))
											reimbursementICDCode = reimbursementSplitArr[1];
										else
											reimbursementICPCCode = reimbursementSplitArr[1];
									}
								}
								
							}
							
							combinedNPRNorPDPojo.setPrescriptionNumber(prescriptionNumber);
							combinedNPRNorPDPojo.setPrescriptionLegalCategory(prescriptionLegalCategory);
							combinedNPRNorPDPojo.setPrescriptionLegalCategoryCode(prescriptionLegalCategoryCode);
							
							combinedNPRNorPDPojo.setReimbursementICDCode(reimbursementICDCode);
							combinedNPRNorPDPojo.setReimbursementICPCCode(reimbursementICPCCode);
							
							JsonNode prescribedDrugATCCodeDDDObj = jsonObjNode.get("dosageInstruction").get(0).get("doseAndRate").get(0).get("doseQuantity");
							JsonNode prescribedDrugATCCodeDDDDoseObj = jsonObjNode.get("dosageInstruction").get(0).get("doseAndRate").get(0).get("doseQuantity");
							
							if(prescribedDrugATCCodeDDDObj != null) 
								combinedNPRNorPDPojo.setPrescribedDrugATCCodeDDD(prescribedDrugATCCodeDDDObj.get("value").toString());
							
							if(prescribedDrugATCCodeDDDDoseObj != null) 
								combinedNPRNorPDPojo.setPrescribedDrugATCCodeDDDDose(prescribedDrugATCCodeDDDDoseObj.get("unit").textValue());
						}
						
						//#8 check if FHIR resource type is 'Medication Dispense'
						if(fhirResourceType.equalsIgnoreCase(APIConstants.FHIR_RESOURCE_MEDICATION_DISPENSE)) {
							
							JsonNode numberOfPackagesDispensedForDrugObj = jsonObjNode.get("quantity").get("value");
							JsonNode numberOfPackagesDispensedForDrugDDDObj = jsonObjNode.get("daysSupply");
							
							combinedNPRNorPDPojo.setPrescribedDrugDeliveryDate(BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonObjNode.get("whenHandedOver")));
							combinedNPRNorPDPojo.setNumberOfPackagesDispensedForDrug(numberOfPackagesDispensedForDrugObj.toString());
							
							if(numberOfPackagesDispensedForDrugDDDObj != null) 
								combinedNPRNorPDPojo.setNumberOfPackagesDispensedForDrugDDD(numberOfPackagesDispensedForDrugDDDObj.get("value").toString());
						}
						
						
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				
				}
				
			}
			combinedNPRNorPDPojoList.add(combinedNPRNorPDPojo);	
			
			LOGGER.info("Data to be written to the CSV {}", combinedNPRNorPDPojoList);
		}
		//Write to CSV and upload CSV file to Azure storage
		String csvFileLocation = BasicConstants.REGISTRY_DATA_DIRECTORY + "combined_registry_data_for_SDG.csv";
		ByteArrayOutputStream fileOutStreamFromStorage = null;
		
		try {
			//create CSV 
			File csvFile = new File(csvFileLocation);
			csvFile = DataWranglingUtil.readBeanToCSV(csvFile, CombinedNPRNorPDPojo.class, combinedNPRNorPDPojoList);
			InputStream convertedCSVFileInputStream = new FileInputStream(csvFile);
			String fileName = csvFile.getName();
			long fileLength  = csvFile.length();
			azureStorageService.uploadFileToStorage(convertedCSVFileInputStream, fileName, fileLength, BasicConstants.AZURE_STORAGE_BLOB_CONTAINER_HEALTH_DATA_FILES_NAME);
			
			//delete file from local resource location
			csvFile.delete();
			
			//download file as stream from the azure storage
			fileOutStreamFromStorage = azureStorageService.downloadFromStorage(BasicConstants.AZURE_STORAGE_BLOB_CONTAINER_HEALTH_DATA_FILES_NAME, fileName);
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		return fileOutStreamFromStorage;
	}


	@Override
	public String uploadFileToAzureStorage(InputStream fileInputStream, String fileOriginalName, long fileSize,
			String containerName) {
		
		return azureStorageService.uploadFileToStorage(fileInputStream, fileOriginalName, fileSize, containerName);
	
	}

	/**
	 * Update
	 */
	@Override
	public ByteArrayOutputStream convertFHIRResourcesJsonObjToCSV(List<ResourceResList> resourceResponseList) {
		
		List<CombinedNPRNorPDPojo> combinedNPRNorPDPojoList = new ArrayList<CombinedNPRNorPDPojo>();
		
		for(ResourceResList resourceResponseObj : resourceResponseList) {
			
			LOGGER.info("Single record with list of FHIR resources {}", resourceResponseObj);
			
			CombinedNPRNorPDPojo combinedNPRNorPDPojo = new CombinedNPRNorPDPojo();
			
			//Patient related details
			PatientRes patientRes = resourceResponseObj.getPatient();
			//Populate CSV Pojo object 
			combinedNPRNorPDPojo.setPatientGender(patientRes.getGender());
			combinedNPRNorPDPojo.setPatientDateOfBirth(patientRes.getBirthDate());
			combinedNPRNorPDPojo.setPatientAgeGroup(patientRes.getExtension().get(0).getValueString());
			combinedNPRNorPDPojo.setPatiendId(patientRes.getIdentifier().get(0).getValue());
			combinedNPRNorPDPojo.setPatientIdType(patientRes.getIdentifier().get(0).getUse());
			combinedNPRNorPDPojo.setPatientCountyName(patientRes.getAddress().get(0).getCity());
			combinedNPRNorPDPojo.setPatientCountyNumber(patientRes.getAddress().get(0).getPostalCode());
			
			String patientDeathDateTime = patientRes.getDeceasedDateTime();
			// Null/Empty/Blank check on patient death date-time
			if(!(BasicUtil.isStringNull(patientDeathDateTime))){
				
				DateTime patientDeathDateTimeDt = BasicUtil.convertStringDateToDateTimeFormat(patientDeathDateTime, "yyyy-MM");
				combinedNPRNorPDPojo.setPatientDeathMonth(String.valueOf(patientDeathDateTimeDt.getMonthOfYear()));
				combinedNPRNorPDPojo.setPatientDeathYear(String.valueOf(patientDeathDateTimeDt.getYear()));
			}
			
			
			//Pracitioner related details
			PractitionerRes practitionerRes = resourceResponseObj.getPractitioner();
			//Populate CSV Pojo object 
			combinedNPRNorPDPojo.setPractitionerGender(practitionerRes.getGender());
			combinedNPRNorPDPojo.setPractitionerDateOfBirth(practitionerRes.getBirthDate());
			combinedNPRNorPDPojo.setPractitionerId(practitionerRes.getIdentifier().get(0).getValue());
			combinedNPRNorPDPojo.setPractitionerIdType(practitionerRes.getIdentifier().get(0).getUse());
			
			//Location related details
			LocationRes locationRes = resourceResponseObj.getLocation();
			//Populate CSV Pojo object 
			combinedNPRNorPDPojo.setInstituteName(locationRes.getName());
			
			//Condition related details
			ConditionRes conditionRes = resourceResponseObj.getCondition();
			//Populate CSV Pojo object 
			combinedNPRNorPDPojo.setHospitalizationMainDiagnosis(conditionRes.getCode().getCoding().get(0).getCode());
		
			//Encounter related details
			EncounterRes encounterRes = resourceResponseObj.getEncounter();
			//Populate CSV Pojo object
			String levelOfCare = encounterRes.getStatus().equals("in-progress") ? "1" : "2";
			String levelOfUrgency = encounterRes.getMyclass().getCode().equals("EMER") ? "1" : "2";
			combinedNPRNorPDPojo.setHospitalizationLevelOfCare(levelOfCare);
			combinedNPRNorPDPojo.setHospitalizationUrgency(levelOfUrgency);
			
			combinedNPRNorPDPojo.setHospitalizationPatientVisitStart(encounterRes.getPeriod().getStart());
			combinedNPRNorPDPojo.setHospitalizationPatientVisitEnd(encounterRes.getPeriod().getEnd());
			
			String hospitalizationDischargeLocation = encounterRes.getHospitalization().getDischargeDisposition().getText();
			
			if(!(BasicUtil.isStringBlank(hospitalizationDischargeLocation) || BasicUtil.isStringEmpty(hospitalizationDischargeLocation) || BasicUtil.isStringNull(hospitalizationDischargeLocation)))
				hospitalizationDischargeLocation = (hospitalizationDischargeLocation.equals("Institusjon or Sykehjem, kommunal enhet") ? "1" : hospitalizationDischargeLocation.equals("Nursing homes, a municipal Unit") ? "2"  : "3");		
			
			combinedNPRNorPDPojo.setHospitalizationPatientDischargeLocation(hospitalizationDischargeLocation);
			
			//Medication related details
			MedicationRes medicationRes = resourceResponseObj.getMedication();
			//Populate CSV Pojo object
			combinedNPRNorPDPojo.setPrescribedDrugNumber(medicationRes.getIdentifier().get(0).getValue());
			combinedNPRNorPDPojo.setPrescribedDrugName(medicationRes.getCode().getText());
			combinedNPRNorPDPojo.setPrescribedDrugATCCode(medicationRes.getCode().getCoding().get(0).getCode());
			
			//MedicationRequest related details
			MedicationRequestRes medicationRequestRes = resourceResponseObj.getMedicationRequest();
			//Populate CSV Pojo object
			combinedNPRNorPDPojo.setPrescriptionNumber(medicationRequestRes.getIdentifier().get(0).getValue());
			combinedNPRNorPDPojo.setPrescriptionLegalCategory(medicationRequestRes.getCategory().get(0).getText());
			combinedNPRNorPDPojo.setPrescriptionLegalCategoryCode(medicationRequestRes.getCategory().get(0).getCoding().get(0).getCode());
			combinedNPRNorPDPojo.setReimbursementLegalCategory(medicationRequestRes.getNote().get(0).getText());
			combinedNPRNorPDPojo.setReimbursementLegalCategoryCode(medicationRequestRes.getNote().get(1).getText());
			
			String reimbursementICDCode = "";
			String reimbursementICPCCode = "";
			{
				String reimbursementICDIPCPCCode = medicationRequestRes.getNote().get(2).getText();
				
				if(!(BasicUtil.isStringBlank(reimbursementICDIPCPCCode) || BasicUtil.isStringEmpty(reimbursementICDIPCPCCode) || BasicUtil.isStringNull(reimbursementICDIPCPCCode))) {
					String[] reimbursementSplitArr = reimbursementICDIPCPCCode.split("\\:");
					
					if(reimbursementSplitArr.length > 1) {
						if(reimbursementSplitArr[0].contains("ICD"))
							reimbursementICDCode = reimbursementSplitArr[1];
						else
							reimbursementICPCCode = reimbursementSplitArr[1];
					}
				}
				
			}
			combinedNPRNorPDPojo.setReimbursementICDCode(reimbursementICDCode);
			combinedNPRNorPDPojo.setReimbursementICPCCode(reimbursementICPCCode);
			combinedNPRNorPDPojo.setPrescribedDrugATCCodeDDD(String.valueOf(medicationRequestRes.getDosageInstruction().get(0).getDoseAndRate().get(0).getDoseQuantity().getValue()));
			combinedNPRNorPDPojo.setPrescribedDrugATCCodeDDDDose(medicationRequestRes.getDosageInstruction().get(0).getDoseAndRate().get(0).getDoseQuantity().getUnit());
			
			//MedicationDispense related details
			MedicationDispenseRes medicationDispenseRes = resourceResponseObj.getMedicationDispense();
			//Populate CSV Pojo object
			combinedNPRNorPDPojo.setPrescribedDrugDeliveryDate(medicationDispenseRes.getWhenHandedOver());
			combinedNPRNorPDPojo.setNumberOfPackagesDispensedForDrug(String.valueOf(medicationDispenseRes.getQuantity().getValue()));
			combinedNPRNorPDPojo.setNumberOfPackagesDispensedForDrugDDD(String.valueOf(medicationDispenseRes.getDaysSupply().getValue()));
			
			
			combinedNPRNorPDPojoList.add(combinedNPRNorPDPojo);	
			
			LOGGER.info("Data to be written to the CSV {}", combinedNPRNorPDPojoList);
		
		}
		
		//Write to CSV and upload CSV file to Azure storage
		String csvFileLocation = BasicConstants.REGISTRY_DATA_DIRECTORY + "combined_registry_data_for_SDG.csv";
		ByteArrayOutputStream fileOutStreamFromStorage = null;
		
		try {
			//create CSV 
			File csvFile = new File(csvFileLocation);
			csvFile = DataWranglingUtil.readBeanToCSV(csvFile, CombinedNPRNorPDPojo.class, combinedNPRNorPDPojoList);
			InputStream convertedCSVFileInputStream = new FileInputStream(csvFile);
			String fileName = csvFile.getName();
			long fileLength  = csvFile.length();
			azureStorageService.uploadFileToStorage(convertedCSVFileInputStream, fileName, fileLength, BasicConstants.AZURE_STORAGE_BLOB_CONTAINER_HEALTH_DATA_FILES_NAME);
			
			//delete file from local resource location
			csvFile.delete();
			
			//download file as stream from the azure storage
			fileOutStreamFromStorage = azureStorageService.downloadFromStorage(BasicConstants.AZURE_STORAGE_BLOB_CONTAINER_HEALTH_DATA_FILES_NAME, fileName);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		return fileOutStreamFromStorage;
	}

}
