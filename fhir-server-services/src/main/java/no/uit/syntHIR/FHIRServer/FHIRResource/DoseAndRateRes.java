package no.uit.syntHIR.FHIRServer.FHIRResource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DoseAndRateRes {

	private QuantityRes doseQuantity;
    private QuantityRes rateQuantity;
    
	public QuantityRes getDoseQuantity() {
		return doseQuantity;
	}
	public void setDoseQuantity(QuantityRes doseQuantity) {
		this.doseQuantity = doseQuantity;
	}
	public QuantityRes getRateQuantity() {
		return rateQuantity;
	}
	public void setRateQuantity(QuantityRes rateQuantity) {
		this.rateQuantity = rateQuantity;
	}
	
	@Override
	public String toString() {
		return "DoseAndRateRes [doseQuantity=" + doseQuantity + ", rateQuantity=" + rateQuantity + "]";
	}
     
	
}
