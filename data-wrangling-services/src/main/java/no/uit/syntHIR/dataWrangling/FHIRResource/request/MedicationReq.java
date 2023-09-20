package no.uit.syntHIR.dataWrangling.FHIRResource.request;

public class MedicationReq {

	private String medicationNumber;
	private String medicationATCCode;
	private String medicationName;
	
	public String getMedicationNumber() {
		return medicationNumber;
	}
	public void setMedicationNumber(String medicationNumber) {
		this.medicationNumber = medicationNumber;
	}
	public String getMedicationATCCode() {
		return medicationATCCode;
	}
	public void setMedicationATCCode(String medicationATCCode) {
		this.medicationATCCode = medicationATCCode;
	}
	public String getMedicationName() {
		return medicationName;
	}
	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}
	@Override
	public String toString() {
		return "FHIRResourceMedication [medicationNumber=" + medicationNumber + ", medicationATCCode="
				+ medicationATCCode + ", medicationName=" + medicationName + "]";
	}
}
