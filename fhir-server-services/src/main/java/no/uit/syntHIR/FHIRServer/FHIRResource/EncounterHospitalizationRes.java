package no.uit.syntHIR.FHIRServer.FHIRResource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EncounterHospitalizationRes {

	private HospitalizationDischargeDispositionRes dischargeDisposition;

	public HospitalizationDischargeDispositionRes getDischargeDisposition() {
		return dischargeDisposition;
	}

	public void setDischargeDisposition(HospitalizationDischargeDispositionRes dischargeDisposition) {
		this.dischargeDisposition = dischargeDisposition;
	}

	@Override
	public String toString() {
		return "EncounterHospitalizationRes [dischargeDisposition=" + dischargeDisposition + "]";
	}

	
	
}
