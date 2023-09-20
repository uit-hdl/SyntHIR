package no.uit.syntHIR.dataWrangling.FHIRResource.request;

public class LocationReq {

	private String instituteName;

	public String getInstituteName() {
		return instituteName;
	}

	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	@Override
	public String toString() {
		return "FHIRResourceLocation [instituteName=" + instituteName + "]";
	}
	
}
