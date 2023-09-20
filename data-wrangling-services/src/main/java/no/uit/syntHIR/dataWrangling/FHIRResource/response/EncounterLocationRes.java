package no.uit.syntHIR.dataWrangling.FHIRResource.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EncounterLocationRes {

	private ReferenceRes location;
	private String status;
	
	public ReferenceRes getLocation() {
		return location;
	}
	public void setLocation(ReferenceRes location) {
		this.location = location;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "EncounterLocationRes [location=" + location + ", status=" + status + "]";
	}
	
	
}
