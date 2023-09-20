package no.uit.syntHIR.dataWrangling.FHIRResource.request;

public class IdentifierReq {


	private String identifierUse; //official for personal number (F) and temp for D-number (D)
	private String value;// number 
	
	
	public String getIdentifierUse() {
		return identifierUse;
	}
	public void setIdentifierUse(String identifierUse) {
		this.identifierUse = identifierUse;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "FHIRResourceIdentifier [identifierUse=" + identifierUse + ", value=" + value + "]";
	}
	
	
	
	
}
