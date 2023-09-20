package no.uit.syntHIR.syntheticFHIRData.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import no.uit.syntHIR.syntheticFHIRData.util.APIConstants;
import no.uit.syntHIR.syntheticFHIRData.util.BasicConstants;
import no.uit.syntHIR.syntheticFHIRData.util.SyntheticDataUtil;

@Service
public class SyntheticFHIRDataEngineImpl implements SyntheticFHIRDataEngine{

	final static Logger LOGGER = LoggerFactory.getLogger(SyntheticFHIRDataEngineImpl.class);
	
	
	@Autowired
	private Environment environment;
	/**
	 * 
	 * 
	 * @param
	 * @return
	 * @author pavitra
	 */
	
	@Override
	public String generateSyntheticDataUsingGretel(String modelConfigurationFilePath, String inputDataFilePath, String gretelProjectName, String numberOfSyntheticRecords, String outputDataDirectory) {
		
		//First, Upload CSV file to the Gretel server
		String inputDataFilePathOnCloudServer=SyntheticDataUtil.uploadDataToCloudServer(inputDataFilePath, environment.getProperty(BasicConstants.APPLICATION_CONFIG_KEY_NAME_GRETEL_SERVER_INPUT_REAL_DATA_DIRECTORY_PATH)); 
		
		//Command to create a model with Gretel
		String createModelCommand = BasicConstants.GRETEL_CLI_CREATE_MODEL 
				+ BasicConstants.GRETEL_CLI_CREATE_MODEL_CONFIG_FILE_PATH + modelConfigurationFilePath + APIConstants.SPACE_SEPERATOR
				+ BasicConstants.GRETEL_CLI_CREATE_MODEL_NAME + "norpd-npr-syn-lstm-model" + APIConstants.SPACE_SEPERATOR 
				+ BasicConstants.GRETEL_CLI_CREATE_MODEL_INPUT_DIRECTORY + inputDataFilePathOnCloudServer + APIConstants.SPACE_SEPERATOR
				+ BasicConstants.GRETEL_CLI_CREATE_MODEL_OUTPUT_DIRECTORY + outputDataDirectory + APIConstants.SPACE_SEPERATOR 
				+ BasicConstants.GRETEL_CLI_CREATE_MODEL_PROJECT_NAME + gretelProjectName + APIConstants.SPACE_SEPERATOR 
				+ "\n";
		
		//Second, Establish connection with the synthetic data generator server and execute 'Create model' command
		String commandOutputFile = SyntheticDataUtil.establishConnectionAndExecuteCommandWithGretelCloud(BasicConstants.GRETEL_CLOUD_USER_NAME, BasicConstants.GRETEL_CLOUD_HOST_NAME, BasicConstants.GRETEL_CLOUD_PRIVATE_KEY, BasicConstants.GRETEL_CLOUD_PASSWORD,
				createModelCommand);	
		String modelIdString = SyntheticDataUtil.searchForTextInFile(commandOutputFile, "Model created with ID");	
		String gretelModelId = modelIdString.substring(modelIdString.lastIndexOf(APIConstants.SPACE_SEPERATOR) + 1, modelIdString.lastIndexOf("."));
		
		//Command to generate synthetic records
		String generateSyntheticRecordsCommand = BasicConstants.GRETEL_CLI_GENERATE_SYNTHETIC_RECORDS 
				+ BasicConstants.GRETEL_CLI_GENERATE_SYNTHETIC_RECORDS_MODEL_ID + gretelModelId + APIConstants.SPACE_SEPERATOR
				+ BasicConstants.GRETEL_CLI_GENERATE_SYNTHETIC_RECORDS_MODEL_PATH + outputDataDirectory + APIConstants.REQUEST_SEPARATOR + "model.tar.gz" + APIConstants.SPACE_SEPERATOR
				+ BasicConstants.GRETEL_CLI_GENERATE_SYNTHETIC_RECORDS_PARAM_NUMBER_OF_RECORDS + numberOfSyntheticRecords + APIConstants.SPACE_SEPERATOR
				+ BasicConstants.GRETEL_CLI_GENERATE_SYNTHETIC_RECORDS_OUTPUT_DIRECTORY + outputDataDirectory
				+ "\n";
		
		//Third,Generate Synthetic records using the model ID
		SyntheticDataUtil.establishConnectionAndExecuteCommandWithGretelCloud(BasicConstants.GRETEL_CLOUD_USER_NAME, BasicConstants.GRETEL_CLOUD_HOST_NAME, BasicConstants.GRETEL_CLOUD_PRIVATE_KEY, BasicConstants.GRETEL_CLOUD_PASSWORD,
				generateSyntheticRecordsCommand);
		
		String syntheticOutputDataFileDirectoryPathOnServer = outputDataDirectory + APIConstants.REQUEST_SEPARATOR + "data.gz";
		String syntheticOutputDataFileDirectoryPathOnLocal = BasicConstants.LOCAL_OUTPUT_SYNTHETIC_DATA_DIRECTORY_PATH;
		//Download synthetic data file from the server
		SyntheticDataUtil.downloadDataFromCloudServer(syntheticOutputDataFileDirectoryPathOnServer, syntheticOutputDataFileDirectoryPathOnLocal);
		
		
		return syntheticOutputDataFileDirectoryPathOnLocal;
	}
	


}
