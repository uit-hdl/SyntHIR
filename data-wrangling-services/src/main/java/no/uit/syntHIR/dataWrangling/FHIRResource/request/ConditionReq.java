package no.uit.syntHIR.dataWrangling.FHIRResource.request;

public class ConditionReq {
	
	private String icd10Code;
	private String patientResourceUrl;
	private String encounterResourceUrl;
	private String encounterResourceIdentifier;
	public String getIcd10Code() {
		return icd10Code;
	}
	public void setIcd10Code(String icd10Code) {
		this.icd10Code = icd10Code;
	}
	public String getPatientResourceUrl() {
		return patientResourceUrl;
	}
	public void setPatientResourceUrl(String patientResourceUrl) {
		this.patientResourceUrl = patientResourceUrl;
	}
	public String getEncounterResourceUrl() {
		return encounterResourceUrl;
	}
	public void setEncounterResourceUrl(String encounterResourceUrl) {
		this.encounterResourceUrl = encounterResourceUrl;
	}
	public String getEncounterResourceIdentifier() {
		return encounterResourceIdentifier;
	}
	public void setEncounterResourceIdentifier(String encounterResourceIdentifier) {
		this.encounterResourceIdentifier = encounterResourceIdentifier;
	}
	@Override
	public String toString() {
		return "ConditionReq [icd10Code=" + icd10Code + ", patientResourceUrl=" + patientResourceUrl
				+ ", encounterResourceUrl=" + encounterResourceUrl + ", encounterResourceIdentifier="
				+ encounterResourceIdentifier + "]";
	}

}
