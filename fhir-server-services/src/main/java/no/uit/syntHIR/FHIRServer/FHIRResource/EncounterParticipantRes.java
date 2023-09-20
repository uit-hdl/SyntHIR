package no.uit.syntHIR.FHIRServer.FHIRResource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EncounterParticipantRes {

	private ReferenceRes individual;

	public ReferenceRes getIndividual() {
		return individual;
	}

	public void setIndividual(ReferenceRes individual) {
		this.individual = individual;
	}

	@Override
	public String toString() {
		return "EncounterParticipantRes [individual=" + individual + "]";
	}
	
}
