package no.uit.syntHIR.FHIRServer.engine;

import java.util.List;

import no.uit.syntHIR.FHIRServer.FHIRResource.ResourceResList;

public interface FHIRServerEngine {

	public void uploadFHIRResourcesToFHIRServer(List<List<String>> fhirResourceRecords, String fhirServerBaseUrl);
	public void uploadFHIRResourcesObjToFHIRServer(List<ResourceResList> fhirResourceRecords, String fhirServerBaseUrl);
	public List<List<String>> downloadFHIRResourcesFromFHIRServer(String fhirResourceRequestUrl, List<List<String>> fhirResourcesListToPopulate, String fhirServerBaseUrl);
	public List<ResourceResList> downloadFHIRResourcesObjFromFHIRServer(String fhirResourceRequestUrl, List<ResourceResList> fhirResourcesListToPopulate, String fhirServerBaseUrl);
}
