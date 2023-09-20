package no.uit.syntHIR.dataWrangling.FHIRResource.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DosageRes {

	private String text;
    private ArrayList<DoseAndRateRes> doseAndRate;
    
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public ArrayList<DoseAndRateRes> getDoseAndRate() {
		return doseAndRate;
	}
	public void setDoseAndRate(ArrayList<DoseAndRateRes> doseAndRate) {
		this.doseAndRate = doseAndRate;
	}
	
	@Override
	public String toString() {
		return "DosageRes [text=" + text + ", doseAndRate=" + doseAndRate + "]";
	}

    
}
