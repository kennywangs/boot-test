package com.xxb.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class HttpRestFulUtils {
	
	public static String restRequest(String url, HttpMethod method, HttpHeaders headers, Object[] urlVars, String body) {
		ResponseEntity<String> response;
		RestTemplate client = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
		try {
			response = client.exchange(url, method, requestEntity , String.class, urlVars);
		} catch (HttpServerErrorException e) {
			response = new ResponseEntity<String>(e.getStatusCode());
		}
		return response.getBody();
	}
	
	public static ResponseEntity<String> restRequestResponse(String url, HttpMethod method, HttpHeaders headers, Object[] urlVars, String body) {
		ResponseEntity<String> response;
		RestTemplate client = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
		try {
			response = client.exchange(url, method, requestEntity , String.class, urlVars);
		} catch (HttpServerErrorException e) {
			response = new ResponseEntity<String>(e.getStatusCode());
		}
		return response;
	}

}
