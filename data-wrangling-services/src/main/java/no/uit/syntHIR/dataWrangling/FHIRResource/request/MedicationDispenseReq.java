package no.uit.syntHIR.dataWrangling.FHIRResource.request;

public class MedicationDispenseReq {

	private String patientResourceUrl;
	private String medicationRequestResourceUrl;
	private String medicationPackagesDispensed;
	private String dddMedicationPackagesDispensed;
	private String dayYearOfDispense;
	
	
	public String getPatientResourceUrl() {
		return patientResourceUrl;
	}
	public void setPatientResourceUrl(String patientResourceUrl) {
		this.patientResourceUrl = patientResourceUrl;
	}
	public String getMedicationRequestResourceUrl() {
		return medicationRequestResourceUrl;
	}
	public void setMedicationRequestResourceUrl(String medicationRequestResourceUrl) {
		this.medicationRequestResourceUrl = medicationRequestResourceUrl;
	}
	public String getMedicationPackagesDispensed() {
		return medicationPackagesDispensed;
	}
	public void setMedicationPackagesDispensed(String medicationPackagesDispensed) {
		this.medicationPackagesDispensed = medicationPackagesDispensed;
	}
	public String getDddMedicationPackagesDispensed() {
		return dddMedicationPackagesDispensed;
	}
	public void setDddMedicationPackagesDispensed(String dddMedicationPackagesDispensed) {
		this.dddMedicationPackagesDispensed = dddMedicationPackagesDispensed;
	}
	public String getDayYearOfDispense() {
		return dayYearOfDispense;
	}
	public void setDayYearOfDispense(String dayYearOfDispense) {
		this.dayYearOfDispense = dayYearOfDispense;
	}
	@Override
	public String toString() {
		return "FHIRResourceMedicationDispense [patientResourceUrl=" + patientResourceUrl
				+ ", medicationRequestResourceUrl=" + medicationRequestResourceUrl + ", medicationPackagesDispensed="
				+ medicationPackagesDispensed + ", dddMedicationPackagesDispensed=" + dddMedicationPackagesDispensed
				+ ", dayYearOfDispense=" + dayYearOfDispense + "]";
	}
	
}
