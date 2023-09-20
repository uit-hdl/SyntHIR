package no.uit.syntHIR.dataWrangling.FHIRResource.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PatientRes {
	
	private String resourceType;
	private String id;
	private MetaRes meta;
	private ArrayList<IdentifierRes> identifier;
	private boolean active;
	private ArrayList<NameRes> name;
	private String gender;
	private String birthDate;
	private boolean deceasedBoolean;
	private String deceasedDateTime;
	private ArrayList<AddressRes> address;
	private ArrayList<ExtensionPatientRes> extension;
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
	public boolean isDeceasedBoolean() {
		return deceasedBoolean;
	}
	public void setDeceasedBoolean(boolean deceasedBoolean) {
		this.deceasedBoolean = deceasedBoolean;
	}
	public String getDeceasedDateTime() {
		return deceasedDateTime;
	}
	public void setDeceasedDateTime(String deceasedDateTime) {
		this.deceasedDateTime = deceasedDateTime;
	}
	public ArrayList<AddressRes> getAddress() {
		return address;
	}
	public void setAddress(ArrayList<AddressRes> address) {
		this.address = address;
	}
	public ArrayList<ExtensionPatientRes> getExtension() {
		return extension;
	}
	public void setExtension(ArrayList<ExtensionPatientRes> extension) {
		this.extension = extension;
	}
	@Override
	public String toString() {
		return "PatientRes [resourceType=" + resourceType + ", id=" + id + ", meta=" + meta + ", identifier="
				+ identifier + ", active=" + active + ", name=" + name + ", gender=" + gender + ", birthDate="
				+ birthDate + ", deceasedBoolean=" + deceasedBoolean + ", deceasedDateTime=" + deceasedDateTime
				+ ", address=" + address + ", extension=" + extension + "]";
	}
	
	
	
}
