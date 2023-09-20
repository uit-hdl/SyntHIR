package no.uit.syntHIR.dataWrangling.FHIRResource.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceRes {
	
	private String reference;
	private IdentifierRes identifier;
	private String display;
	private String type;
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public IdentifierRes getIdentifier() {
		return identifier;
	}
	public void setIdentifier(IdentifierRes identifier) {
		this.identifier = identifier;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "ReferenceRes [reference=" + reference + ", identifier=" + identifier + ", display=" + display
				+ ", type=" + type + "]";
	}
	
	
}
