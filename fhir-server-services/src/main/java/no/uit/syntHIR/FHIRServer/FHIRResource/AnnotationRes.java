package no.uit.syntHIR.FHIRServer.FHIRResource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnotationRes {
	
	private String authorString;
	private String text;
	
	public String getAuthorString() {
		return authorString;
	}
	public void setAuthorString(String authorString) {
		this.authorString = authorString;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return "AnnotationRes [authorString=" + authorString + ", text=" + text + "]";
	}
	
	
}
