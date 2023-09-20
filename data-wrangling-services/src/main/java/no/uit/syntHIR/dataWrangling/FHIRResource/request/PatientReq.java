package no.uit.syntHIR.dataWrangling.FHIRResource.request;

public class PatientReq {

	private String identifierUse;
	private String identifierValue;
	private String gender;
	private String dateOfBirth;
	private AddressReq address;
	private String deathDateTime;
	private String ageGroup;
	
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
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public AddressReq getAddress() {
		return address;
	}
	public void setAddress(AddressReq address) {
		this.address = address;
	}
	public String getDeathDateTime() {
		return deathDateTime;
	}
	public void setDeathDateTime(String deathDateTime) {
		this.deathDateTime = deathDateTime;
	}
	public String getAgeGroup() {
		return ageGroup;
	}
	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}
	@Override
	public String toString() {
		return "PatientReq [identifierUse=" + identifierUse + ", identifierValue=" + identifierValue + ", gender="
				+ gender + ", dateOfBirth=" + dateOfBirth + ", address=" + address + ", deathDateTime=" + deathDateTime
				+ ", ageGroup=" + ageGroup + "]";
	}
	
	
	
}
