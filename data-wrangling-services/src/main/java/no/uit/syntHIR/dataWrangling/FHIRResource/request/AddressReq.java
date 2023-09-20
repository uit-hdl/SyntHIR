package no.uit.syntHIR.dataWrangling.FHIRResource.request;

public class AddressReq {

	private String addressUse;
	private String addressText;
	private String addressCity;
	private String addressDistrict;
	private String addressState;
	private String addressPostalCode;
	private String addressCountry;
	
	public String getAddressUse() {
		return addressUse;
	}
	public void setAddressUse(String addressUse) {
		this.addressUse = addressUse;
	}
	public String getAddressText() {
		return addressText;
	}
	public void setAddressText(String addressText) {
		this.addressText = addressText;
	}
	public String getAddressCity() {
		return addressCity;
	}
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}
	public String getAddressDistrict() {
		return addressDistrict;
	}
	public void setAddressDistrict(String addressDistrict) {
		this.addressDistrict = addressDistrict;
	}
	public String getAddressState() {
		return addressState;
	}
	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}
	public String getAddressPostalCode() {
		return addressPostalCode;
	}
	public void setAddressPostalCode(String addressPostalCode) {
		this.addressPostalCode = addressPostalCode;
	}
	public String getAddressCountry() {
		return addressCountry;
	}
	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}
	@Override
	public String toString() {
		return "FHIRResourceAddress [addressUse=" + addressUse + ", addressText=" + addressText + ", addressCity="
				+ addressCity + ", addressDistrict=" + addressDistrict + ", addressState=" + addressState
				+ ", addressPostalCode=" + addressPostalCode + ", addressCountry=" + addressCountry + "]";
	}
	
	
	
}
