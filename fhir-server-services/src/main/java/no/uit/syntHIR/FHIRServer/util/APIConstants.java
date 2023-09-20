package no.uit.syntHIR.FHIRServer.util;

public class APIConstants {
	
	public static final String AZURE_ACTIVE_DIRECTORY_OAUTH_BASE_URL  = "https://login.microsoftonline.com";
	public static final String AZURE_ACTIVE_DIRECTORY_OAUTH_URL_PARAMS = "oauth2/token";	
	public static final String AZURE_FHIR_GRANT_TYPE_CLIENT_CREDENTIALS = "Client_Credentials";

	//Microsoft FHIR server details
	//fhirurl - The FHIR service full URL. For example, https://xxx.azurehealthcareapis.com. It's located from the FHIR service overview menu option.
	public static final String AZURE_SYNTHIR_FHIR_SERVER_BASE_URL = "https://synthir-server.azurehealthcareapis.com";
	public static final String AZURE_SYNTHIR_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME = "synthir.fhir.server.access.token";
	
	//fhirurl - The FHIR service full URL. For example, https://xxx.azurehealthcareapis.com. It's located from the FHIR service overview menu option.
	public static final String AZURE_PRIVATE_FHIR_SERVER_BASE_URL = "https://synthir-test-fhir-server.azurehealthcareapis.com";	
	public static final String AZURE_PRIVATE_FHIR_SERVER_ACCESS_TOKEN_KEY_NAME = "private.fhir.server.access.token";
	
	//FHIR resource names
	public static final String FHIR_RESOURCE_PATIENT = "Patient";
	public static final String FHIR_RESOURCE_PRACTITIONER = "Practitioner";
	public static final String FHIR_RESOURCE_LOCATION = "Location";
	public static final String FHIR_RESOURCE_CONDITION = "Condition";
	public static final String FHIR_RESOURCE_ENCOUNTER = "Encounter";
	public static final String FHIR_RESOURCE_MEDICATION = "Medication";
	public static final String FHIR_RESOURCE_MEDICATION_REQUEST = "MedicationRequest";	
	public static final String FHIR_RESOURCE_MEDICATION_DISPENSE = "MedicationDispense";		
	public static final String FHIR_RESOURCE_CLAIM = "Claim";
	
	//Application configuration keys
	
	public static final String APPLICATION_CONFIG_KEY_NAME_AZURE_ACTIVE_DIRECTORY_TENANT_ID = "azure.active.directory.tenant.id";
	public static final String APPLICATION_CONFIG_KEY_NAME_AZURE_ACTIVE_DIRECTORY_OAUTH_URL = "azure.active.directory.oauth.url";
	
	public static final String APPLICATION_CONFIG_KEY_NAME_PRIVATE_FHIR_SERVER_BASE_URL = "synthir.hdl.fhir.server.base.url";
	public static final String APPLICATION_CONFIG_KEY_NAME_PRIVATE_FHIR_SERVER_CLIENT_ID = "synthir.hdl.azure.app.client.reg.id";
	public static final String APPLICATION_CONFIG_KEY_NAME_PRIVATE_FHIR_SERVER_CLIENT_SECRET = "synthir.hdl.azure.app.client.reg.secret.value";
	
	public static final String APPLICATION_CONFIG_KEY_NAME_SYNTHIR_FHIR_SERVER_BASE_URL = "synthir.fhir.server.base.url";
	public static final String APPLICATION_CONFIG_KEY_NAME_SYNTHIR_FHIR_SERVER_CLIENT_ID = "synthir.app.client.reg.id";
	public static final String APPLICATION_CONFIG_KEY_NAME_SYNTHIR_FHIR_SERVER_CLIENT_SECRET = "synthir.app.client.reg.secret.value";
	

	//Misc
	public static final String REQUEST_SEPARATOR = "/";
	public static final String SPACE_SEPERATOR = " ";
	
	
	
}
