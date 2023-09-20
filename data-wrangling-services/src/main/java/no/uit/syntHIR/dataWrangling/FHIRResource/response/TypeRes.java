package no.uit.syntHIR.dataWrangling.FHIRResource.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeRes {

	private String id;
    private ArrayList<CodingRes> coding;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<CodingRes> getCoding() {
		return coding;
	}
	public void setCoding(ArrayList<CodingRes> coding) {
		this.coding = coding;
	}
	
	@Override
	public String toString() {
		return "TypeRes [id=" + id + ", coding=" + coding + "]";
	}
    
    
}
