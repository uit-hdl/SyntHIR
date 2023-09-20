package no.uit.syntHIR.FHIRServer.FHIRResource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EncounterDiagnosisRes {

	private ReferenceRes condition;

	public ReferenceRes getCondition() {
		return condition;
	}

	public void setCondition(ReferenceRes condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "EncounterDiagnosisRes [condition=" + condition + "]";
	}
	
	
}
