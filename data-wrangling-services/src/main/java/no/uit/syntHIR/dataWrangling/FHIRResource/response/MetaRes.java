package no.uit.syntHIR.dataWrangling.FHIRResource.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MetaRes {

	 private ArrayList<String> profile;
	 private String versionId;
	 private String lastUpdated;
	 
	public ArrayList<String> getProfile() {
		return profile;
	}
	public void setProfile(ArrayList<String> profile) {
		this.profile = profile;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	@Override
	public String toString() {
		return "MetaRes [profile=" + profile + ", versionId=" + versionId + ", lastUpdated=" + lastUpdated + "]";
	}
	 
}
