package no.uit.syntHIR.FHIRServer.FHIRResource;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PractitionerRes {

	private String resourceType;
	private String id;
	private MetaRes meta;
	private ArrayList<IdentifierRes> identifier;
	private boolean active;
	private ArrayList<NameRes> name;
	private String gender;
	private String birthDate;
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public ArrayList<NameRes> getName() {
		return name;
	}
	public void setName(ArrayList<NameRes> name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	@Override
	public String toString() {
		return "PractitionerRes [resourceType=" + resourceType + ", id=" + id + ", meta=" + meta + ", identifier="
				+ identifier + ", active=" + active + ", name=" + name + ", gender=" + gender + ", birthDate="
				+ birthDate + "]";
	}
	
}
