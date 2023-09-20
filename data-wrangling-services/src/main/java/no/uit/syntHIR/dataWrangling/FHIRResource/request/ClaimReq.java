package no.uit.syntHIR.dataWrangling.FHIRResource.request;

public class ClaimReq {

	private String legalReimbursementCategoryCode;
	private String legalReimbursementCategory;
	private String icdCodeReimbursement;
	private String icpcCodeReimbursement;
	private String medicationRequestResourceUrl;
	private String resourceCreatedDateTime;
	
	public String getLegalReimbursementCategoryCode() {
		return legalReimbursementCategoryCode;
	}
	public void setLegalReimbursementCategoryCode(String legalReimbursementCategoryCode) {
		this.legalReimbursementCategoryCode = legalReimbursementCategoryCode;
	}
	public String getLegalReimbursementCategory() {
		return legalReimbursementCategory;
	}
	public void setLegalReimbursementCategory(String legalReimbursementCategory) {
		this.legalReimbursementCategory = legalReimbursementCategory;
	}
	public String getIcdCodeReimbursement() {
		return icdCodeReimbursement;
	}
	public void setIcdCodeReimbursement(String icdCodeReimbursement) {
		this.icdCodeReimbursement = icdCodeReimbursement;
	}
	public String getIcpcCodeReimbursement() {
		return icpcCodeReimbursement;
	}
	public void setIcpcCodeReimbursement(String icpcCodeReimbursement) {
		this.icpcCodeReimbursement = icpcCodeReimbursement;
	}
	public String getMedicationRequestResourceUrl() {
		return medicationRequestResourceUrl;
	}
	public void setMedicationRequestResourceUrl(String medicationRequestResourceUrl) {
		this.medicationRequestResourceUrl = medicationRequestResourceUrl;
	}
	public String getResourceCreatedDateTime() {
		return resourceCreatedDateTime;
	}
	public void setResourceCreatedDateTime(String resourceCreatedDateTime) {
		this.resourceCreatedDateTime = resourceCreatedDateTime;
	}
	@Override
	public String toString() {
		return "FHIRResourceClaim [legalReimbursementCategoryCode=" + legalReimbursementCategoryCode
				+ ", legalReimbursementCategory=" + legalReimbursementCategory + ", icdCodeReimbursement="
				+ icdCodeReimbursement + ", icpcCodeReimbursement=" + icpcCodeReimbursement
				+ ", medicationRequestResourceUrl=" + medicationRequestResourceUrl + ", resourceCreatedDateTime="
				+ resourceCreatedDateTime + "]";
	}
	
}
