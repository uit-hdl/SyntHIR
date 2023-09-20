package no.uit.syntHIR.FHIRServer.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.uit.syntHIR.FHIRServer.engine.FHIRResourceResponse;

public class FHIRUtil {

	/**
	 * Creates a FHIR resource on the server 
	 * and returns the server generated identifier value
	 * which can be further used to retrieve the resource
	 * 
	 * @param Resource URL : FHIR resource URL to create resource
	 * @param JSON String : FHIR resource in JSON string format   
	 * @return FHIR resource identifier
	 */
	public static String createFHIRResourceOnFHIRServer(String createResourceRequestUrl, String accessToken, String resourceJsonStr, String fhirServerBaseUrl, String tenantID, String clientID, String clientSecret) {		
				
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Authorization", "Bearer "+ accessToken);
		
		HttpEntity<String> httpReqEntity = new HttpEntity<String>(resourceJsonStr, httpHeaders);		
		HttpStatusCode httpStatusCode = null;
		ResponseEntity<String> response = null;
		String responseBody = "";
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			response = restTemplate.postForEntity(createResourceRequestUrl, httpReqEntity, String.class);
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			httpStatusCode = httpClientOrServerExc.getStatusCode();
		} 
		
		if(httpStatusCode != null) {
			if(httpStatusCode.toString().equalsIgnoreCase("401 UNAUTHORIZED")) {
				//get and update new access token in cache
				accessToken= getAccessTokenFromAzureActiveDirectory(tenantID, fhirServerBaseUrl, clientID, clientSecret);
				createFHIRResourceOnFHIRServer(createResourceRequestUrl, accessToken, resourceJsonStr, fhirServerBaseUrl, tenantID, clientID, clientSecret); 
			}
		}else {
			responseBody = response.getBody();
		}
		return responseBody;
	
	}
	
	
	/**
	 * this method gets the FHIR resource with respect to the URL
	 * from the FHIR server
	 * @param getResourceUrl (FHIR resource URL) 
	 * @return full URL of the FHIR resource
	 */
	public static String getFHIRResourceFromFHIRServer(String getResourceRequestUrl, String accessToken, String fhirServerBaseUrl, String tenantID, String clientID, String clientSecret) {
		
		String responseBody = "";
		HttpStatusCode httpStatusCode = null;
		
		if(!(BasicUtil.isStringBlank(getResourceRequestUrl) || BasicUtil.isStringEmpty(getResourceRequestUrl) || BasicUtil.isStringNull(getResourceRequestUrl))) {
			
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			httpHeaders.set("Authorization", "Bearer "+ accessToken);
			
			HttpEntity<Object> httpReqEntity = new HttpEntity<Object>(httpHeaders);	
			
			RestTemplate restTemplate = new RestTemplate();		
			ResponseEntity<String> response = null;
			try {
				response = restTemplate.exchange(new URI(getResourceRequestUrl), HttpMethod.GET, httpReqEntity, String.class);
			} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
				httpStatusCode = httpClientOrServerExc.getStatusCode();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}		
				
			if(httpStatusCode != null) {
				if(httpStatusCode.toString().equalsIgnoreCase("401 UNAUTHORIZED")) {
					//get and update new access token in cache
					accessToken= getAccessTokenFromAzureActiveDirectory(tenantID, fhirServerBaseUrl, clientID, clientSecret);
					getFHIRResourceFromFHIRServer(getResourceRequestUrl, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret); 
				}
			}else {
				responseBody = response.getBody();
			}
			
		}
		return responseBody;
	}
	
	public static String getFHIRResourceFromFHIRServerWithQueryParams(String requestUrl, String queryParam, String accessToken, String fhirServerBaseUrl, String tenantID, String clientID, String clientSecret) {
		
		String responseBody = "";
		HttpStatusCode httpStatusCode = null;
		
		
		if(!(BasicUtil.isStringBlank(requestUrl) || BasicUtil.isStringEmpty(requestUrl) || BasicUtil.isStringNull(requestUrl))) {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			httpHeaders.set("Authorization", "Bearer "+ accessToken);
			
			HttpEntity<Object> httpReqEntity = new HttpEntity<Object>(httpHeaders);	
			
			URI uri = UriComponentsBuilder.fromHttpUrl(requestUrl)
			        .queryParam("name", queryParam)
			        .encode(StandardCharsets.UTF_8)
			        .build()
			        .toUri();
			
			RestTemplate restTemplate = new RestTemplate();		
			
			ResponseEntity<String> response = null;
			try {
				response = restTemplate.exchange(uri, HttpMethod.GET, httpReqEntity, String.class);
			} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
				httpStatusCode = httpClientOrServerExc.getStatusCode();
			} 
			if(httpStatusCode != null) {
				if(httpStatusCode.toString().equalsIgnoreCase("401 UNAUTHORIZED")) {
					//get and update new access token in cache
					accessToken= getAccessTokenFromAzureActiveDirectory(tenantID, fhirServerBaseUrl, clientID, clientSecret);
					getFHIRResourceFromFHIRServerWithQueryParams(requestUrl, queryParam, accessToken, fhirServerBaseUrl, tenantID, clientID, clientSecret); 
				}
			}else {
				responseBody = response.getBody();
			}			
		}
		return responseBody;
		
	}



	public static String getFHIRResourceUrlFromResourceBody(String responseBody) {
		
		String fullUrl = "";
		
		if(!(BasicUtil.isStringBlank(responseBody) || BasicUtil.isStringEmpty(responseBody) || BasicUtil.isStringNull(responseBody))) {
			
			JsonParser jsonParser = JsonParserFactory.getJsonParser(); 
			Map<String,Object> resMap = jsonParser.parseMap(responseBody);
			
			@SuppressWarnings("unchecked")
			ArrayList<Map<String, Object>> entryArrayList = (ArrayList<Map<String, Object>>) resMap.get("entry");
			
			if(entryArrayList != null && !entryArrayList.isEmpty()) {
				fullUrl = (String) entryArrayList.get(0).get("fullUrl");
			}
		}
		
		return fullUrl;
	}
	
	public static String getFHIRResourceIdentifierFromResponseBody(String responseBody) {
		
		String resourceIdentifier = "";
		
		if(!(BasicUtil.isStringBlank(responseBody) || BasicUtil.isStringEmpty(responseBody) || BasicUtil.isStringNull(responseBody))) {
			
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNodeObj = null;
			
			try {
				jsonNodeObj = objectMapper.readTree(responseBody);
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			resourceIdentifier =  BasicUtil.checkIfJsonNodeObjectValueIsEmpty(jsonNodeObj.get("id"));
		
		}
		return resourceIdentifier;
	}

	public static Object getResourceObjectFromResponse(String jsonResponseString) {
		Object resource = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonObjNode;
		try {
			jsonObjNode = objectMapper.readTree(jsonResponseString);
			JsonNode jsonNodeEntryObj = jsonObjNode.get("entry");
			if(jsonNodeEntryObj != null) {
				List<FHIRResourceResponse> fhirResourceResponseList = objectMapper.readValue(jsonNodeEntryObj.toString(), new TypeReference<List<FHIRResourceResponse>>(){});
				resource = fhirResourceResponseList.get(0).getResource();
				
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return resource;
	}
	

	@Cacheable (value = "fhiraccesstoken")
	public static String getAccessTokenFromAzureActiveDirectory(String tenantID, String fhirServerBaseUrl, String clientID, String clientSecret) {
		
		String accessToken = "";
		
		String getRequestUrl = APIConstants.AZURE_ACTIVE_DIRECTORY_OAUTH_BASE_URL + APIConstants.REQUEST_SEPARATOR + tenantID + APIConstants.REQUEST_SEPARATOR + APIConstants.AZURE_ACTIVE_DIRECTORY_OAUTH_URL_PARAMS;
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		
		body.add("grant_type", APIConstants.AZURE_FHIR_GRANT_TYPE_CLIENT_CREDENTIALS);
		body.add("resource", fhirServerBaseUrl);
		body.add("client_id", clientID);
		body.add("client_secret", clientSecret);
		
		HttpEntity<MultiValueMap<String, String>> httpReqEntity = new HttpEntity<MultiValueMap<String, String>>(body, httpHeaders);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(getRequestUrl, HttpMethod.POST, httpReqEntity, String.class);
		
		String resBody = response.getBody();
		
		JsonParser jsonParser = JsonParserFactory.getJsonParser();
	    Map<String, Object> resMap = jsonParser.parseMap(resBody);
	    accessToken = (String) resMap.get("access_token");
				
	    System.out.println("Access token  in FHIRUtil : "  + accessToken);
		return accessToken;
	}
	
		
}
