package no.uit.syntHIR.FHIRServer.FHIRResource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class IdentifierRes {

	private String use;
	private String system;
	private String value;
	
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "IdentifierRes [use=" + use + ", system=" + system + ", value=" + value + "]";
	}
	
	
}
