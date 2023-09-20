package no.uit.syntHIR.FHIRServer.FHIRResource;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicationDispenseRes {

	private String resourceType;
    private String id;
    private MetaRes meta;
    private String status;
    private ReferenceRes medicationReference;
    private ReferenceRes subject;
    private ArrayList<ReferenceRes> authorizingPrescription;
    private QuantityRes quantity;
    private QuantityRes daysSupply;
    private String whenHandedOver;
    
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ReferenceRes getMedicationReference() {
		return medicationReference;
	}
	public void setMedicationReference(ReferenceRes medicationReference) {
		this.medicationReference = medicationReference;
	}
	public ReferenceRes getSubject() {
		return subject;
	}
	public void setSubject(ReferenceRes subject) {
		this.subject = subject;
	}
	public ArrayList<ReferenceRes> getAuthorizingPrescription() {
		return authorizingPrescription;
	}
	public void setAuthorizingPrescription(ArrayList<ReferenceRes> authorizingPrescription) {
		this.authorizingPrescription = authorizingPrescription;
	}
	public QuantityRes getQuantity() {
		return quantity;
	}
	public void setQuantity(QuantityRes quantity) {
		this.quantity = quantity;
	}
	public QuantityRes getDaysSupply() {
		return daysSupply;
	}
	public void setDaysSupply(QuantityRes daysSupply) {
		this.daysSupply = daysSupply;
	}
	public String getWhenHandedOver() {
		return whenHandedOver;
	}
	public void setWhenHandedOver(String whenHandedOver) {
		this.whenHandedOver = whenHandedOver;
	}
	
	@Override
	public String toString() {
		return "MedicationDispenseRes [resourceType=" + resourceType + ", id=" + id + ", meta=" + meta + ", status="
				+ status + ", medicationReference=" + medicationReference + ", subject=" + subject
				+ ", authorizingPrescription=" + authorizingPrescription + ", quantity=" + quantity + ", daysSupply="
				+ daysSupply + ", whenHandedOver=" + whenHandedOver + "]";
	}
    
    
}
