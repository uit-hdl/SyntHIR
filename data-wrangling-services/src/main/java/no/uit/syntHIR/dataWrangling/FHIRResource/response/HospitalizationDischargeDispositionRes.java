package no.uit.syntHIR.dataWrangling.FHIRResource.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HospitalizationDischargeDispositionRes {
	
	private ArrayList<CodingRes> coding;
	private String text;
	
	public ArrayList<CodingRes> getCoding() {
		return coding;
	}
	public void setCoding(ArrayList<CodingRes> coding) {
		this.coding = coding;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return "HospitalizationDischargeDispositionRes [coding=" + coding + ", text=" + text + "]";
	}
	
}
