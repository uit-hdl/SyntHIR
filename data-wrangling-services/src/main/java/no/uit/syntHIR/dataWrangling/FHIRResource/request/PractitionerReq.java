package no.uit.syntHIR.dataWrangling.FHIRResource.request;

public class PractitionerReq {

	private String identifierUse;
	private String identifierValue;
	private String gender;
	private String birthDate;
	
	public String getIdentifierUse() {
		return identifierUse;
	}
	public void setIdentifierUse(String identifierUse) {
		this.identifierUse = identifierUse;
	}
	public String getIdentifierValue() {
		return identifierValue;
	}
	public void setIdentifierValue(String identifierValue) {
		this.identifierValue = identifierValue;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	@Override
	public String toString() {
		return "PractitionerReq [identifierUse=" + identifierUse + ", identifierValue=" + identifierValue + ", gender="
				+ gender + ", birthDate=" + birthDate + "]";
	}
	
	
	
}
