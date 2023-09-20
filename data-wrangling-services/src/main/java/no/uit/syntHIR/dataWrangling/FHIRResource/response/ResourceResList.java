package no.uit.syntHIR.dataWrangling.FHIRResource.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceResList {

	private PatientRes patient;
	private PractitionerRes practitioner;
	private LocationRes location;
	private ConditionRes condition;
	private EncounterRes encounter;
	private MedicationRes medication;
	private MedicationRequestRes medicationRequest;
	private MedicationDispenseRes medicationDispense;
	
	public PatientRes getPatient() {
		return patient;
	}
	public void setPatient(PatientRes patient) {
		this.patient = patient;
	}
	public PractitionerRes getPractitioner() {
		return practitioner;
	}
	public void setPractitioner(PractitionerRes practitioner) {
		this.practitioner = practitioner;
	}
	public LocationRes getLocation() {
		return location;
	}
	public void setLocation(LocationRes location) {
		this.location = location;
	}
	public ConditionRes getCondition() {
		return condition;
	}
	public void setCondition(ConditionRes condition) {
		this.condition = condition;
	}
	public EncounterRes getEncounter() {
		return encounter;
	}
	public void setEncounter(EncounterRes encounter) {
		this.encounter = encounter;
	}
	public MedicationRes getMedication() {
		return medication;
	}
	public void setMedication(MedicationRes medication) {
		this.medication = medication;
	}
	public MedicationRequestRes getMedicationRequest() {
		return medicationRequest;
	}
	public void setMedicationRequest(MedicationRequestRes medicationRequest) {
		this.medicationRequest = medicationRequest;
	}
	public MedicationDispenseRes getMedicationDispense() {
		return medicationDispense;
	}
	public void setMedicationDispense(MedicationDispenseRes medicationDispense) {
		this.medicationDispense = medicationDispense;
	}
	@Override
	public String toString() {
		return "ResourceResList [patient=" + patient + ", practitioner=" + practitioner + ", location=" + location
				+ ", condition=" + condition + ", encounter=" + encounter + ", medication=" + medication
				+ ", medicationRequest=" + medicationRequest + ", medicationDispense=" + medicationDispense + "]";
	}
	
		
}
