package no.uit.syntHIR.FHIRServer.FHIRResource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ExtensionPatientRes {

	private String valueString;

	public String getValueString() {
		return valueString;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	@Override
	public String toString() {
		return "ExtensionPatientRes [valueString=" + valueString + "]";
	}
	
}
