package no.uit.syntHIR.dataWrangling.persistence;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public interface AzureStorageService {

	public ByteArrayOutputStream downloadFromStorage(String containerName, String storageObjectName);
	public String uploadFileToStorage(InputStream fileInputStream, String fileOriginalName, long fileSize, String containerName);
	
}
