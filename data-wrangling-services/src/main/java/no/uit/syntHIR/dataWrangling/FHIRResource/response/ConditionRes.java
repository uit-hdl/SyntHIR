package no.uit.syntHIR.dataWrangling.FHIRResource.response;

import java.util.ArrayList;
import java.util.Locale.Category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ConditionRes {

	private String resourceType;
	private String id;
	private MetaRes meta;
	private ArrayList<IdentifierRes> identifier;
	private ArrayList<Category> category;
	private CodeRes code;
	private ReferenceRes subject;
	private ReferenceRes encounter;
	
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
	public ArrayList<Category> getCategory() {
		return category;
	}
	public void setCategory(ArrayList<Category> category) {
		this.category = category;
	}
	public CodeRes getCode() {
		return code;
	}
	public void setCode(CodeRes code) {
		this.code = code;
	}
	public ReferenceRes getSubject() {
		return subject;
	}
	public void setSubject(ReferenceRes subject) {
		this.subject = subject;
	}
	public ReferenceRes getEncounter() {
		return encounter;
	}
	public void setEncounter(ReferenceRes encounter) {
		this.encounter = encounter;
	}
	
	@Override
	public String toString() {
		return "ConditionRes [resourceType=" + resourceType + ", id=" + id + ", meta=" + meta + ", identifier="
				+ identifier + ", category=" + category + ", code=" + code + ", subject=" + subject + ", encounter="
				+ encounter + "]";
	}

	
}
