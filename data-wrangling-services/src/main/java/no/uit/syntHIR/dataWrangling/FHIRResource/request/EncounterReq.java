package no.uit.syntHIR.dataWrangling.FHIRResource.request;

public class EncounterReq {
	
	private String levelOfCare;
	private String patientFullUrl;
	private String conditionFullUrl;
	private String admissionStartDate;
	private String admissionEndDate;
	private int hospitalizationVisitLength;
	private String levelOfUrgency;
	private String practitionerResourceUrl;
	private String fhirCodeForDischargeLocation;
	private String dischargeLocation;
	private String locationResourceUrl;
	
	public String getLevelOfCare() {
		return levelOfCare;
	}
	public void setLevelOfCare(String levelOfCare) {
		this.levelOfCare = levelOfCare;
	}
	public String getPatientFullUrl() {
		return patientFullUrl;
	}
	public void setPatientFullUrl(String patientFullUrl) {
		this.patientFullUrl = patientFullUrl;
	}
	public String getConditionFullUrl() {
		return conditionFullUrl;
	}
	public void setConditionFullUrl(String conditionFullUrl) {
		this.conditionFullUrl = conditionFullUrl;
	}
	public String getAdmissionStartDate() {
		return admissionStartDate;
	}
	public void setAdmissionStartDate(String admissionStartDate) {
		this.admissionStartDate = admissionStartDate;
	}
	public String getAdmissionEndDate() {
		return admissionEndDate;
	}
	public void setAdmissionEndDate(String admissionEndDate) {
		this.admissionEndDate = admissionEndDate;
	}
	public int getHospitalizationVisitLength() {
		return hospitalizationVisitLength;
	}
	public void setHospitalizationVisitLength(int hospitalizationVisitLength) {
		this.hospitalizationVisitLength = hospitalizationVisitLength;
	}
	public String getLevelOfUrgency() {
		return levelOfUrgency;
	}
	public void setLevelOfUrgency(String levelOfUrgency) {
		this.levelOfUrgency = levelOfUrgency;
	}
	public String getPractitionerResourceUrl() {
		return practitionerResourceUrl;
	}
	public void setPractitionerResourceUrl(String practitionerResourceUrl) {
		this.practitionerResourceUrl = practitionerResourceUrl;
	}
	public String getFhirCodeForDischargeLocation() {
		return fhirCodeForDischargeLocation;
	}
	public void setFhirCodeForDischargeLocation(String fhirCodeForDischargeLocation) {
		this.fhirCodeForDischargeLocation = fhirCodeForDischargeLocation;
	}
	public String getDischargeLocation() {
		return dischargeLocation;
	}
	public void setDischargeLocation(String dischargeLocation) {
		this.dischargeLocation = dischargeLocation;
	}
	public String getLocationResourceUrl() {
		return locationResourceUrl;
	}
	public void setLocationResourceUrl(String locationResourceUrl) {
		this.locationResourceUrl = locationResourceUrl;
	}
	@Override
	public String toString() {
		return "FHIRResourceEncounter [levelOfCare=" + levelOfCare + ", patientFullUrl=" + patientFullUrl
				+ ", conditionFullUrl=" + conditionFullUrl + ", admissionStartDate=" + admissionStartDate
				+ ", admissionEndDate=" + admissionEndDate + ", hospitalizationVisitLength="
				+ hospitalizationVisitLength + ", levelOfUrgency=" + levelOfUrgency + ", practitionerResourceUrl="
				+ practitionerResourceUrl + ", fhirCodeForDischargeLocation=" + fhirCodeForDischargeLocation
				+ ", dischargeLocation=" + dischargeLocation + ", locationResourceUrl=" + locationResourceUrl + "]";
	}
}
