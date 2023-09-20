package no.uit.syntHIR.dataWrangling.FHIRResource.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicationRequestRes {

	private String resourceType;
	private String id;
	private MetaRes meta;
	private ArrayList<IdentifierRes> identifier;
	private String status;
	private String intent;
    private ArrayList<CodeRes> category;
    private ReferenceRes medicationReference;
    private ReferenceRes subject;
    private ReferenceRes encounter;
    private ReferenceRes recorder;
    private ArrayList<AnnotationRes> note;
    private ArrayList<DosageRes> dosageInstruction;
    
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
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public ArrayList<CodeRes> getCategory() {
		return category;
	}
	public void setCategory(ArrayList<CodeRes> category) {
		this.category = category;
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
	public ReferenceRes getEncounter() {
		return encounter;
	}
	public void setEncounter(ReferenceRes encounter) {
		this.encounter = encounter;
	}
	public ReferenceRes getRecorder() {
		return recorder;
	}
	public void setRecorder(ReferenceRes recorder) {
		this.recorder = recorder;
	}
	public ArrayList<AnnotationRes> getNote() {
		return note;
	}
	public void setNote(ArrayList<AnnotationRes> note) {
		this.note = note;
	}
	public ArrayList<DosageRes> getDosageInstruction() {
		return dosageInstruction;
	}
	public void setDosageInstruction(ArrayList<DosageRes> dosageInstruction) {
		this.dosageInstruction = dosageInstruction;
	}
	
	@Override
	public String toString() {
		return "MedicationRequestRes [resourceType=" + resourceType + ", id=" + id + ", meta=" + meta + ", identifier="
				+ identifier + ", status=" + status + ", intent=" + intent + ", category=" + category
				+ ", medicationReference=" + medicationReference + ", subject=" + subject + ", encounter=" + encounter
				+ ", recorder=" + recorder + ", note=" + note + ", dosageInstruction=" + dosageInstruction + "]";
	}
    
    
}
