package no.uit.syntHIR.dataWrangling.registryData;

import com.opencsv.bean.CsvBindByName;

public class CombinedNPRNorPDPojo {

	@CsvBindByName(column = "innmate")
	private String hospitalizationUrgency;
	
	@CsvBindByName(column = "aldersgrp")
	private String patientAgeGroup;
	
	@CsvBindByName(column = "hovedtilstand")
	private String hospitalizationMainDiagnosis;
	
	@CsvBindByName(column = "ut_til")
	private String hospitalizationPatientDischargeLocation;
	
	@CsvBindByName(column = "omsorgsnivå")
	private String hospitalizationLevelOfCare;
	
	@CsvBindByName(column = "pasientlopenr")
	private String patiendId;
	
	@CsvBindByName(column = "fnrgyldig")
	private String patientIdType;
	
	@CsvBindByName(column = "diffdager_inn")
	private String hospitalizationPatientVisitStart;
	
	@CsvBindByName(column = "diffdager_ut")
	private String hospitalizationPatientVisitEnd;
	
	@CsvBindByName(column = "pasientfodtar")
	private String patientDateOfBirth;
	
	@CsvBindByName(column = "pasientkjonn")
	private String patientGender;
	
	@CsvBindByName(column = "pasientdodsar")
	private String patientDeathYear;
	
	@CsvBindByName(column = "pasientdodsmnd")
	private String patientDeathMonth;
	
	@CsvBindByName(column = "pasientbostedfylkenr")
	private String patientCountyNumber;
	
	@CsvBindByName(column = "pasientbostedfylkenavn")
	private String patientCountyName;
	
	@CsvBindByName(column = "forskriverlopenr")
	private String practitionerId;
	
	@CsvBindByName(column = "forskriverutenid")
	private String practitionerIdType;
	
	@CsvBindByName(column = "forskriverfodtar")
	private String practitionerDateOfBirth;
	
	@CsvBindByName(column = "forskriverkjonn")
	private String practitionerGender;
	
	@CsvBindByName(column = "kategori")
	private String prescriptionLegalCategory;
	
	@CsvBindByName(column = "kategorinr")
	private String prescriptionLegalCategoryCode;
	
	@CsvBindByName(column = "hjemmel")
	private String reimbursementLegalCategory;
	
	@CsvBindByName(column = "hjemmelnr")
	private String reimbursementLegalCategoryCode;
	
	@CsvBindByName(column = "refusjonkodeicdnr")
	private String reimbursementICDCode;
	
	@CsvBindByName(column = "refusjonkodeicpcnr")
	private String reimbursementICPCCode;	
	
	@CsvBindByName(column = "varenr")
	private String prescribedDrugNumber;
	
	@CsvBindByName(column = "varenavn")
	private String prescribedDrugName;
	
	@CsvBindByName(column = "atckode")
	private String prescribedDrugATCCode;
	
	@CsvBindByName(column = "atckodedddverdi")
	private String prescribedDrugATCCodeDDD;
	
	@CsvBindByName(column = "atckodedddenhet")
	private String prescribedDrugATCCodeDDDDose;
	
	@CsvBindByName(column = "ordinasjonnr")
	private String prescriptionNumber;
	
	@CsvBindByName(column = "institusjon_navn")
	private String instituteName;
	
	@CsvBindByName(column = "utleveringdato")
	private String prescribedDrugDeliveryDate;
	
	@CsvBindByName(column = "ordinasjonantallpakninger")
	private String numberOfPackagesDispensedForDrug;
	
	@CsvBindByName(column = "ordinasjonantallddd")
	private String numberOfPackagesDispensedForDrugDDD;

	public String getHospitalizationUrgency() {
		return hospitalizationUrgency;
	}

	public void setHospitalizationUrgency(String hospitalizationUrgency) {
		this.hospitalizationUrgency = hospitalizationUrgency;
	}

	

	public String getPatientAgeGroup() {
		return patientAgeGroup;
	}

	public void setPatientAgeGroup(String patientAgeGroup) {
		this.patientAgeGroup = patientAgeGroup;
	}

	public String getHospitalizationMainDiagnosis() {
		return hospitalizationMainDiagnosis;
	}

	public void setHospitalizationMainDiagnosis(String hospitalizationMainDiagnosis) {
		this.hospitalizationMainDiagnosis = hospitalizationMainDiagnosis;
	}

	public String getHospitalizationPatientDischargeLocation() {
		return hospitalizationPatientDischargeLocation;
	}

	public void setHospitalizationPatientDischargeLocation(String hospitalizationPatientDischargeLocation) {
		this.hospitalizationPatientDischargeLocation = hospitalizationPatientDischargeLocation;
	}

	public String getHospitalizationLevelOfCare() {
		return hospitalizationLevelOfCare;
	}

	public void setHospitalizationLevelOfCare(String hospitalizationLevelOfCare) {
		this.hospitalizationLevelOfCare = hospitalizationLevelOfCare;
	}

	public String getPatiendId() {
		return patiendId;
	}

	public void setPatiendId(String patiendId) {
		this.patiendId = patiendId;
	}

	public String getPatientIdType() {
		return patientIdType;
	}

	public void setPatientIdType(String patientIdType) {
		this.patientIdType = patientIdType;
	}

	public String getHospitalizationPatientVisitStart() {
		return hospitalizationPatientVisitStart;
	}

	public void setHospitalizationPatientVisitStart(String hospitalizationPatientVisitStart) {
		this.hospitalizationPatientVisitStart = hospitalizationPatientVisitStart;
	}

	public String getHospitalizationPatientVisitEnd() {
		return hospitalizationPatientVisitEnd;
	}

	public void setHospitalizationPatientVisitEnd(String hospitalizationPatientVisitEnd) {
		this.hospitalizationPatientVisitEnd = hospitalizationPatientVisitEnd;
	}

	public String getPatientDateOfBirth() {
		return patientDateOfBirth;
	}

	public void setPatientDateOfBirth(String patientDateOfBirth) {
		this.patientDateOfBirth = patientDateOfBirth;
	}

	public String getPatientGender() {
		return patientGender;
	}

	public void setPatientGender(String patientGender) {
		this.patientGender = patientGender;
	}

	public String getPatientDeathYear() {
		return patientDeathYear;
	}

	public void setPatientDeathYear(String patientDeathYear) {
		this.patientDeathYear = patientDeathYear;
	}

	public String getPatientDeathMonth() {
		return patientDeathMonth;
	}

	public void setPatientDeathMonth(String patientDeathMonth) {
		this.patientDeathMonth = patientDeathMonth;
	}

	public String getPatientCountyNumber() {
		return patientCountyNumber;
	}

	public void setPatientCountyNumber(String patientCountyNumber) {
		this.patientCountyNumber = patientCountyNumber;
	}

	public String getPatientCountyName() {
		return patientCountyName;
	}

	public void setPatientCountyName(String patientCountyName) {
		this.patientCountyName = patientCountyName;
	}

	public String getPractitionerId() {
		return practitionerId;
	}

	public void setPractitionerId(String practitionerId) {
		this.practitionerId = practitionerId;
	}

	public String getPractitionerIdType() {
		return practitionerIdType;
	}

	public void setPractitionerIdType(String practitionerIdType) {
		this.practitionerIdType = practitionerIdType;
	}

	public String getPractitionerDateOfBirth() {
		return practitionerDateOfBirth;
	}

	public void setPractitionerDateOfBirth(String practitionerDateOfBirth) {
		this.practitionerDateOfBirth = practitionerDateOfBirth;
	}

	public String getPractitionerGender() {
		return practitionerGender;
	}

	public void setPractitionerGender(String practitionerGender) {
		this.practitionerGender = practitionerGender;
	}

	public String getPrescriptionLegalCategory() {
		return prescriptionLegalCategory;
	}

	public void setPrescriptionLegalCategory(String prescriptionLegalCategory) {
		this.prescriptionLegalCategory = prescriptionLegalCategory;
	}

	public String getPrescriptionLegalCategoryCode() {
		return prescriptionLegalCategoryCode;
	}

	public void setPrescriptionLegalCategoryCode(String prescriptionLegalCategoryCode) {
		this.prescriptionLegalCategoryCode = prescriptionLegalCategoryCode;
	}

	public String getReimbursementLegalCategory() {
		return reimbursementLegalCategory;
	}

	public void setReimbursementLegalCategory(String reimbursementLegalCategory) {
		this.reimbursementLegalCategory = reimbursementLegalCategory;
	}

	public String getReimbursementLegalCategoryCode() {
		return reimbursementLegalCategoryCode;
	}

	public void setReimbursementLegalCategoryCode(String reimbursementLegalCategoryCode) {
		this.reimbursementLegalCategoryCode = reimbursementLegalCategoryCode;
	}

	public String getReimbursementICDCode() {
		return reimbursementICDCode;
	}

	public void setReimbursementICDCode(String reimbursementICDCode) {
		this.reimbursementICDCode = reimbursementICDCode;
	}

	public String getReimbursementICPCCode() {
		return reimbursementICPCCode;
	}

	public void setReimbursementICPCCode(String reimbursementICPCCode) {
		this.reimbursementICPCCode = reimbursementICPCCode;
	}

	public String getPrescribedDrugNumber() {
		return prescribedDrugNumber;
	}

	public void setPrescribedDrugNumber(String prescribedDrugNumber) {
		this.prescribedDrugNumber = prescribedDrugNumber;
	}

	public String getPrescribedDrugName() {
		return prescribedDrugName;
	}

	public void setPrescribedDrugName(String prescribedDrugName) {
		this.prescribedDrugName = prescribedDrugName;
	}

	public String getPrescribedDrugATCCode() {
		return prescribedDrugATCCode;
	}

	public void setPrescribedDrugATCCode(String prescribedDrugATCCode) {
		this.prescribedDrugATCCode = prescribedDrugATCCode;
	}

	public String getPrescribedDrugATCCodeDDD() {
		return prescribedDrugATCCodeDDD;
	}

	public void setPrescribedDrugATCCodeDDD(String prescribedDrugATCCodeDDD) {
		this.prescribedDrugATCCodeDDD = prescribedDrugATCCodeDDD;
	}

	public String getPrescribedDrugATCCodeDDDDose() {
		return prescribedDrugATCCodeDDDDose;
	}

	public void setPrescribedDrugATCCodeDDDDose(String prescribedDrugATCCodeDDDDose) {
		this.prescribedDrugATCCodeDDDDose = prescribedDrugATCCodeDDDDose;
	}

	public String getPrescriptionNumber() {
		return prescriptionNumber;
	}

	public void setPrescriptionNumber(String prescriptionNumber) {
		this.prescriptionNumber = prescriptionNumber;
	}

	public String getInstituteName() {
		return instituteName;
	}

	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	public String getPrescribedDrugDeliveryDate() {
		return prescribedDrugDeliveryDate;
	}

	public void setPrescribedDrugDeliveryDate(String prescribedDrugDeliveryDate) {
		this.prescribedDrugDeliveryDate = prescribedDrugDeliveryDate;
	}

	public String getNumberOfPackagesDispensedForDrug() {
		return numberOfPackagesDispensedForDrug;
	}

	public void setNumberOfPackagesDispensedForDrug(String numberOfPackagesDispensedForDrug) {
		this.numberOfPackagesDispensedForDrug = numberOfPackagesDispensedForDrug;
	}

	public String getNumberOfPackagesDispensedForDrugDDD() {
		return numberOfPackagesDispensedForDrugDDD;
	}

	public void setNumberOfPackagesDispensedForDrugDDD(String numberOfPackagesDispensedForDrugDDD) {
		this.numberOfPackagesDispensedForDrugDDD = numberOfPackagesDispensedForDrugDDD;
	}

	@Override
	public String toString() {
		return "CombinedNPRNorPDPojo [hospitalizationUrgency=" + hospitalizationUrgency + ", patientAgeGroup="
				+ patientAgeGroup + ", hospitalizationMainDiagnosis=" + hospitalizationMainDiagnosis
				+ ", hospitalizationPatientDischargeLocation=" + hospitalizationPatientDischargeLocation
				+ ", hospitalizationLevelOfCare=" + hospitalizationLevelOfCare + ", patiendId=" + patiendId
				+ ", patientIdType=" + patientIdType + ", hospitalizationPatientVisitStart="
				+ hospitalizationPatientVisitStart + ", hospitalizationPatientVisitEnd="
				+ hospitalizationPatientVisitEnd + ", patientDateOfBirth=" + patientDateOfBirth + ", patientGender="
				+ patientGender + ", patientDeathYear=" + patientDeathYear + ", patientDeathMonth=" + patientDeathMonth
				+ ", patientCountyNumber=" + patientCountyNumber + ", patientCountyName=" + patientCountyName
				+ ", practitionerId=" + practitionerId + ", practitionerIdType=" + practitionerIdType
				+ ", practitionerDateOfBirth=" + practitionerDateOfBirth + ", practitionerGender=" + practitionerGender
				+ ", prescriptionLegalCategory=" + prescriptionLegalCategory + ", prescriptionLegalCategoryCode="
				+ prescriptionLegalCategoryCode + ", reimbursementLegalCategory=" + reimbursementLegalCategory
				+ ", reimbursementLegalCategoryCode=" + reimbursementLegalCategoryCode + ", reimbursementICDCode="
				+ reimbursementICDCode + ", reimbursementICPCCode=" + reimbursementICPCCode + ", prescribedDrugNumber="
				+ prescribedDrugNumber + ", prescribedDrugName=" + prescribedDrugName + ", prescribedDrugATCCode="
				+ prescribedDrugATCCode + ", prescribedDrugATCCodeDDD=" + prescribedDrugATCCodeDDD
				+ ", prescribedDrugATCCodeDDDDose=" + prescribedDrugATCCodeDDDDose + ", prescriptionNumber="
				+ prescriptionNumber + ", instituteName=" + instituteName + ", prescribedDrugDeliveryDate="
				+ prescribedDrugDeliveryDate + ", numberOfPackagesDispensedForDrug=" + numberOfPackagesDispensedForDrug
				+ ", numberOfPackagesDispensedForDrugDDD=" + numberOfPackagesDispensedForDrugDDD
				+ ", getHospitalizationUrgency()=" + getHospitalizationUrgency() + ", getPatientAgeGroup()="
				+ getPatientAgeGroup() + ", getHospitalizationMainDiagnosis()=" + getHospitalizationMainDiagnosis()
				+ ", getHospitalizationPatientDischargeLocation()=" + getHospitalizationPatientDischargeLocation()
				+ ", getHospitalizationLevelOfCare()=" + getHospitalizationLevelOfCare() + ", getPatiendId()="
				+ getPatiendId() + ", getPatientIdType()=" + getPatientIdType()
				+ ", getHospitalizationPatientVisitStart()=" + getHospitalizationPatientVisitStart()
				+ ", getHospitalizationPatientVisitEnd()=" + getHospitalizationPatientVisitEnd()
				+ ", getPatientDateOfBirth()=" + getPatientDateOfBirth() + ", getPatientGender()=" + getPatientGender()
				+ ", getPatientDeathYear()=" + getPatientDeathYear() + ", getPatientDeathMonth()="
				+ getPatientDeathMonth() + ", getPatientCountyNumber()=" + getPatientCountyNumber()
				+ ", getPatientCountyName()=" + getPatientCountyName() + ", getPractitionerId()=" + getPractitionerId()
				+ ", getPractitionerIdType()=" + getPractitionerIdType() + ", getPractitionerDateOfBirth()="
				+ getPractitionerDateOfBirth() + ", getPractitionerGender()=" + getPractitionerGender()
				+ ", getPrescriptionLegalCategory()=" + getPrescriptionLegalCategory()
				+ ", getPrescriptionLegalCategoryCode()=" + getPrescriptionLegalCategoryCode()
				+ ", getReimbursementLegalCategory()=" + getReimbursementLegalCategory()
				+ ", getReimbursementLegalCategoryCode()=" + getReimbursementLegalCategoryCode()
				+ ", getReimbursementICDCode()=" + getReimbursementICDCode() + ", getReimbursementICPCCode()="
				+ getReimbursementICPCCode() + ", getPrescribedDrugNumber()=" + getPrescribedDrugNumber()
				+ ", getPrescribedDrugName()=" + getPrescribedDrugName() + ", getPrescribedDrugATCCode()="
				+ getPrescribedDrugATCCode() + ", getPrescribedDrugATCCodeDDD()=" + getPrescribedDrugATCCodeDDD()
				+ ", getPrescribedDrugATCCodeDDDDose()=" + getPrescribedDrugATCCodeDDDDose()
				+ ", getPrescriptionNumber()=" + getPrescriptionNumber() + ", getInstituteName()=" + getInstituteName()
				+ ", getPrescribedDrugDeliveryDate()=" + getPrescribedDrugDeliveryDate()
				+ ", getNumberOfPackagesDispensedForDrug()=" + getNumberOfPackagesDispensedForDrug()
				+ ", getNumberOfPackagesDispensedForDrugDDD()=" + getNumberOfPackagesDispensedForDrugDDD()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}


	
}