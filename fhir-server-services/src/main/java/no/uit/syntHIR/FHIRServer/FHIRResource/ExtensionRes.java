package no.uit.syntHIR.FHIRServer.FHIRResource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ExtensionRes {

	private String url;
	private String valueString;
    private ReferenceRes valueReference;
    private boolean valueBoolean;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getValueString() {
		return valueString;
	}
	public void setValueString(String valueString) {
		this.valueString = valueString;
	}
	public ReferenceRes getValueReference() {
		return valueReference;
	}
	public void setValueReference(ReferenceRes valueReference) {
		this.valueReference = valueReference;
	}
	public boolean isValueBoolean() {
		return valueBoolean;
	}
	public void setValueBoolean(boolean valueBoolean) {
		this.valueBoolean = valueBoolean;
	}
	
	@Override
	public String toString() {
		return "ExtensionRes [url=" + url + ", valueString=" + valueString + ", valueReference=" + valueReference
				+ ", valueBoolean=" + valueBoolean + "]";
	}
    
	
    
}
