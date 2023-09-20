package no.uit.syntHIR.syntheticFHIRData.engine;

public interface SyntheticFHIRDataEngine {
	
	public String generateSyntheticDataUsingGretel(String modelConfigurationFilePath, String inputDataFilePath, String gretelProjectName, String numberOfSyntheticRecords, String outputDataDirectory);
}
