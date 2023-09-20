package no.uit.syntHIR.dataWrangling.persistence;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;

@Service
public class AzureStorageServiceImpl implements AzureStorageService{

	@Autowired
	BlobServiceClient blobServiceClient;
	
	@Override
	public ByteArrayOutputStream downloadFromStorage(String containerName, String storageObjectName) {
		
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = blobContainerClient.getBlobClient(storageObjectName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.downloadStream(outputStream);
		
        return outputStream;
	}

	@Override
	public String uploadFileToStorage(InputStream fileInputStream, String fileOriginalName, long fileSize, String containerName) {
		
		BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = blobContainerClient.getBlobClient(fileOriginalName);
        blobClient.upload(fileInputStream, fileSize, true);
		
        return blobClient.getBlobUrl();
	}

}
