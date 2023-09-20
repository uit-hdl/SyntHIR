package no.uit.syntHIR.dataWrangling.FHIRResource.request;

public class MedicationRequestReq {
	
	private String prescriptionNumber;
	private String prescriptionCategoryCode;
	private String prescriptionCategory;
	private String medicationResourceUrl;
	private String patientResourceUrl;
	private String encounterResourceUrl;
	private String practitionerResourceUrl;
	
	private String reimbursementLegalCategory;
	private String reimbursementLegalCategoryCode;
	private String reimbursementIcdIcpcCode;
	
	private String prescriptionDrugDosage;
	private String prescriptionDrugDosageUnit;
	
	public String getPrescriptionNumber() {
		return prescriptionNumber;
	}
	public void setPrescriptionNumber(String prescriptionNumber) {
		this.prescriptionNumber = prescriptionNumber;
	}
	public String getPrescriptionCategoryCode() {
		return prescriptionCategoryCode;
	}
	public void setPrescriptionCategoryCode(String prescriptionCategoryCode) {
		this.prescriptionCategoryCode = prescriptionCategoryCode;
	}
	public String getPrescriptionCategory() {
		return prescriptionCategory;
	}
	public void setPrescriptionCategory(String prescriptionCategory) {
		this.prescriptionCategory = prescriptionCategory;
	}
	public String getMedicationResourceUrl() {
		return medicationResourceUrl;
	}
	public void setMedicationResourceUrl(String medicationResourceUrl) {
		this.medicationResourceUrl = medicationResourceUrl;
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
	public String getPractitionerResourceUrl() {
		return practitionerResourceUrl;
	}
	public void setPractitionerResourceUrl(String practitionerResourceUrl) {
		this.practitionerResourceUrl = practitionerResourceUrl;
	}
	public String getReimbursementLegalCategory() {
		return reimbursementLegalCategory;
	}
	public void setReimbursementLegalCategory(String reimbursementLegalCategory) {
		this.reimbursementLegalCategory = reimbursementLegalCategory;
	}
	public String getReimbursementLegalCategoryCode() {
		return reimbursementLegalCategoryCode;
	}
	public void setReimbursementLegalCategoryCode(String reimbursementLegalCategoryCode) {
		this.reimbursementLegalCategoryCode = reimbursementLegalCategoryCode;
	}
	public String getReimbursementIcdIcpcCode() {
		return reimbursementIcdIcpcCode;
	}
	public void setReimbursementIcdIcpcCode(String reimbursementIcdIcpcCode) {
		this.reimbursementIcdIcpcCode = reimbursementIcdIcpcCode;
	}
	public String getPrescriptionDrugDosage() {
		return prescriptionDrugDosage;
	}
	public void setPrescriptionDrugDosage(String prescriptionDrugDosage) {
		this.prescriptionDrugDosage = prescriptionDrugDosage;
	}
	public String getPrescriptionDrugDosageUnit() {
		return prescriptionDrugDosageUnit;
	}
	public void setPrescriptionDrugDosageUnit(String prescriptionDrugDosageUnit) {
		this.prescriptionDrugDosageUnit = prescriptionDrugDosageUnit;
	}
	
	@Override
	public String toString() {
		return "FHIRResourceMedicationRequest [prescriptionNumber=" + prescriptionNumber + ", prescriptionCategoryCode="
				+ prescriptionCategoryCode + ", prescriptionCategory=" + prescriptionCategory
				+ ", medicationResourceUrl=" + medicationResourceUrl + ", patientResourceUrl=" + patientResourceUrl
				+ ", encounterResourceUrl=" + encounterResourceUrl + ", practitionerResourceUrl="
				+ practitionerResourceUrl + ", reimbursementLegalCategory=" + reimbursementLegalCategory
				+ ", reimbursementLegalCategoryCode=" + reimbursementLegalCategoryCode + ", reimbursementIcdIcpcCode="
				+ reimbursementIcdIcpcCode + ", prescriptionDrugDosage=" + prescriptionDrugDosage
				+ ", prescriptionDrugDosageUnit=" + prescriptionDrugDosageUnit + "]";
	}
	
	
}

