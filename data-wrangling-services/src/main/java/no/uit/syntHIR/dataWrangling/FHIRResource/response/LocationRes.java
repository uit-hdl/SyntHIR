package no.uit.syntHIR.dataWrangling.FHIRResource.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class LocationRes {

	public String resourceType;
    public String id;
    public MetaRes meta;
    public ArrayList<IdentifierRes> identifier;
    public String status;
    public String name;
    public String mode;
    public AddressRes address;
    
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public AddressRes getAddress() {
		return address;
	}
	public void setAddress(AddressRes address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "LocationRes [resourceType=" + resourceType + ", id=" + id + ", meta=" + meta + ", identifier="
				+ identifier + ", status=" + status + ", name=" + name + ", mode=" + mode + ", address=" + address
				+ "]";
	}
    
    
}
