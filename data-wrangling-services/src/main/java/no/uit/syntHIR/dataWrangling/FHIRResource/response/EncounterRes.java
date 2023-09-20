package no.uit.syntHIR.dataWrangling.FHIRResource.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class EncounterRes {

	private String resourceType;
	private String id;
	private MetaRes meta;
	private ArrayList<IdentifierRes> identifier;
	private String status;
	@JsonProperty("class") 
	private CodingRes myclass;
	private ArrayList<TypeRes> type;
	private ReferenceRes subject;
	private ArrayList<EncounterLocationRes> location;
	private EncounterHospitalizationRes hospitalization;
	private PeriodRes period;
	private ArrayList<EncounterDiagnosisRes> diagnosis;
	private ArrayList<EncounterParticipantRes> participant;
	
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
	public CodingRes getMyclass() {
		return myclass;
	}
	public void setMyclass(CodingRes myclass) {
		this.myclass = myclass;
	}
	public ArrayList<TypeRes> getType() {
		return type;
	}
	public void setType(ArrayList<TypeRes> type) {
		this.type = type;
	}
	public ReferenceRes getSubject() {
		return subject;
	}
	public void setSubject(ReferenceRes subject) {
		this.subject = subject;
	}
	public ArrayList<EncounterLocationRes> getLocation() {
		return location;
	}
	public void setLocation(ArrayList<EncounterLocationRes> location) {
		this.location = location;
	}
	public EncounterHospitalizationRes getHospitalization() {
		return hospitalization;
	}
	public void setHospitalization(EncounterHospitalizationRes hospitalization) {
		this.hospitalization = hospitalization;
	}
	public PeriodRes getPeriod() {
		return period;
	}
	public void setPeriod(PeriodRes period) {
		this.period = period;
	}
	public ArrayList<EncounterDiagnosisRes> getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(ArrayList<EncounterDiagnosisRes> diagnosis) {
		this.diagnosis = diagnosis;
	}
	public ArrayList<EncounterParticipantRes> getParticipant() {
		return participant;
	}
	public void setParticipant(ArrayList<EncounterParticipantRes> participant) {
		this.participant = participant;
	}
	@Override
	public String toString() {
		return "EncounterRes [resourceType=" + resourceType + ", id=" + id + ", meta=" + meta + ", identifier="
				+ identifier + ", status=" + status + ", myclass=" + myclass + ", type=" + type + ", subject=" + subject
				+ ", location=" + location + ", hospitalization=" + hospitalization + ", period=" + period
				+ ", diagnosis=" + diagnosis + ", participant=" + participant + "]";
	}
	
	
}
