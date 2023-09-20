package no.uit.syntHIR.util;

public class APIConstants {
	
	//Application configuration keys	
	public static final String APPLICATION_CONFIG_KEY_NAME_SYNTHIR_DATAWRANGLING_SERVICES_HOST = "synthir.data.wrangling.services.host";
	public static final String APPLICATION_CONFIG_KEY_NAME_SYNTHIR_FHIR_SERVER_SERVICES_HOST = "synthir.fhir.server.services.host";
	public static final String APPLICATION_CONFIG_KEY_NAME_SYNTHIR_SYNTHETIC_FHIR_DATA_SERVICES_HOST = "synthir.synthetic.fhir.data.services.host";

	//Misc
	public static final String REQUEST_SEPARATOR = "/";
	public static final String SPACE_SEPERATOR = " ";
	
	
	//SyntHIR Components API Request URLs
	
	//Data Wrangling component API's
	public static final String DATAWRANGLING_CONVERT_CSV_TO_FHIR = "/api/v1/data-wrangling/convert/npr/csv-to-fhir";
	public static final String DATAWRANGLING_CONVERT_FHIR_TO_CSV = "/api/v1/data-wrangling/convert/npr/fhir-to-csv";
	
	//FHIR Server Component API's
	public static final String FHIR_SERVER_UPLOAD = "/api/v1/fhir-server/upload";
	public static final String FHIR_SERVER_DOWNLOAD = "/api/v1/fhir-server/download";
	
	//Synthetic FHIR Data Component API's
	public static final String SYNTHETIC_DATA_GENERATE = "/api/v1/synthetic/generate-data";
}
