package no.uit.syntHIR.dataWrangling.engine;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import no.uit.syntHIR.dataWrangling.FHIRResource.response.ResourceResList;

public interface DataWranglingEngine {

	public List<ResourceResList> convertCSVToFHIRResourcesJson(BufferedReader csvFileBufferedReader);
	public ByteArrayOutputStream convertFHIRResourcesJsonToCSV(List<List<String>> fhirResourceRecords);
	public String uploadFileToAzureStorage(InputStream fileInputStream, String fileOriginalName, long fileSize, String containerName);
	public ByteArrayOutputStream convertFHIRResourcesJsonObjToCSV(List<ResourceResList> resourceResponseList);
}
