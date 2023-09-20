package no.uit.syntHIR.dataWrangling.FHIRResource.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicationRes {

	private String resourceType;
    private String id;
    private MetaRes meta;
    private ArrayList<IdentifierRes> identifier;
    private CodeRes code;
    
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MetaRes getMeta() {
		return meta;
	}
	public void setMeta(MetaRes meta) {
		this.meta = meta;
	}
	public ArrayList<IdentifierRes> getIdentifier() {
		return identifier;
	}
	public void setIdentifier(ArrayList<IdentifierRes> identifier) {
		this.identifier = identifier;
	}
	public CodeRes getCode() {
		return code;
	}
	public void setCode(CodeRes code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return "MedicationRes [resourceType=" + resourceType + ", id=" + id + ", meta=" + meta + ", identifier="
				+ identifier + ", code=" + code + "]";
	}
    
    
}
