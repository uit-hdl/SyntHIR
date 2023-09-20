package no.uit.syntHIR.FHIRServer.engine;

public class FHIRResourceResponse {

	private Object resource;
	private String fullUrl;
	private Object search;
	public Object getResource() {
		return resource;
	}
	public void setResource(Object resource) {
		this.resource = resource;
	}
	public String getFullUrl() {
		return fullUrl;
	}
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	public Object getSearch() {
		return search;
	}
	public void setSearch(Object search) {
		this.search = search;
	}
	@Override
	public String toString() {
		return "FHIRResourceResponse [resource=" + resource + ", fullUrl=" + fullUrl + ", search=" + search + "]";
	}
	
	
}
