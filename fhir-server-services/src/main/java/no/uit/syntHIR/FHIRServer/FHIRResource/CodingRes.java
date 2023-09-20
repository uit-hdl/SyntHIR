package no.uit.syntHIR.FHIRServer.FHIRResource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CodingRes {

	private String code;
    private String system;
    private String display;
    
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	
	@Override
	public String toString() {
		return "CodingRes [code=" + code + ", system=" + system + ", display=" + display + "]";
	}
    
    
}
