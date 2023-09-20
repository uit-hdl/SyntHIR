package no.uit.syntHIR.dataWrangling.configuration;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;

@Configuration
public class AzureStorageConfiguration {	
	
	    @Value("${azure.storage.blob.account-name}")
	    private String accountName;

	    @Value("${azure.storage.blob.account-key}")
	    private String accountKey;

	    @Bean
	    public BlobServiceClient getBlobServiceClient() {
	        return new BlobServiceClientBuilder()
	                .endpoint(String.format(Locale.ROOT, "https://%s.blob.core.windows.net", accountName))
	                .credential(new StorageSharedKeyCredential(accountName, accountKey))
	                .buildClient();
	    }


}
