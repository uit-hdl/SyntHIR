package no.uit.syntHIR.FHIRServer.FHIRResource;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class NameRes {

	private String use;
	private String text;
	private String family;
	private ArrayList<String> given;
	
	
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public ArrayList<String> getGiven() {
		return given;
	}
	public void setGiven(ArrayList<String> given) {
		this.given = given;
	}
	
	
	@Override
	public String toString() {
		return "NameRes [use=" + use + ", text=" + text + ", family=" + family + ", given=" + given + "]";
	}
	
	
	
    
}
