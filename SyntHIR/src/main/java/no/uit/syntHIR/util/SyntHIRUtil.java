package no.uit.syntHIR.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class SyntHIRUtil {

	public static List<List<String>> postRequestToUploadFile(String requestUrl, String fileName, byte[] fileByteArray) {		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
	
		// This nested HttpEntiy is important to create the correct
        // Content-Disposition entry with metadata "name" and "filename"
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("file")
                .filename(fileName)
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
       
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileByteArray, fileMap);
        
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileEntity);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, httpHeaders);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<List<String>>>  response = restTemplate.exchange(requestUrl, HttpMethod.POST,requestEntity, new ParameterizedTypeReference<List<List<String>>>(){});		
		
		return response.getBody();
	
	}
	
	public static byte[] postRequestToUploadFile(String requestUrl, String fileName, byte[] fileByteArray, MultiValueMap<String, String> queryParams) {		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
	
		// This nested HttpEntiy is important to create the correct
        // Content-Disposition entry with metadata "name" and "filename"
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("file")
                .filename(fileName)
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
       
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileByteArray, fileMap);
        
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileEntity);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, httpHeaders);
		
        URI uri = UriComponentsBuilder.fromHttpUrl(requestUrl)
			    .queryParams(queryParams)
			    .build().toUri();
        
        
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<byte[]>  response = restTemplate.exchange(uri, HttpMethod.POST,requestEntity, byte[].class);		
		
		return response.getBody();
	
	}
	
	public static byte[] postRequestToDownloadFile(String requestUrl, Object body) {		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(body, httpHeaders);	
		
		RestTemplate restTemplate = new RestTemplate();		
		ResponseEntity<byte[]> response = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, byte[].class);
		
		return response.getBody();
			
	}
	
	public static String postRequestForObject(String requestUrl, Object body, MultiValueMap<String, String> queryParams) {		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(body, httpHeaders);	

		URI uri = UriComponentsBuilder.fromHttpUrl(requestUrl)
			    .queryParams(queryParams)
			    .build().toUri();
		
		RestTemplate restTemplate = new RestTemplate();		
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);
		return response.getBody();
			
	}
	
	
	
	public static List<List<String>> getRequestForObject(String requestUrl, MultiValueMap<String, String> queryParams) {		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);	

		URI uri = UriComponentsBuilder.fromHttpUrl(requestUrl)
			    .queryParams(queryParams)
			    .build().toUri();
		
		RestTemplate restTemplate = new RestTemplate();		
		ResponseEntity<List<List<String>>> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<List<String>>>(){});
		
		return response.getBody();
			
	}
	
	public static File extractFile(byte[] fileByteArray) throws IOException {
		
		ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(fileByteArray));
	    ZipEntry entry = null;
	    File newFile = null;
	    while ((entry = zipStream.getNextEntry()) != null) {
	    	
	    	String fileName = entry.getName();
	    	newFile = new File(BasicConstants.LOCAL_OUTPUT_SYNTHETIC_DATA_DIRECTORY_PATH + File.separator + fileName);
	        System.out.println("file unzip : " + newFile.getAbsoluteFile());
	        
	        byte[] buffer = new byte[1024];
	        
	        try (FileOutputStream fos = new FileOutputStream(newFile)) {
	            int len;
	            while ((len = zipStream.read(buffer)) > 0) {
	             fos.write(buffer, 0, len);
	            }
	           }
	        	entry = zipStream.getNextEntry();
	          }

		    zipStream.closeEntry();
		    zipStream.close();
		    
			return newFile;
	    	
		}

	
	
}
