package com.xxb.util.jackson;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JacksonJsonUtil {
	
	public static String toJson(Object value, Djson json) throws JsonProcessingException {
		CustomerJsonSerializer jsonSerializer = new CustomerJsonSerializer();
		jsonSerializer.filter(json.getType(), json.getInclude(), json.getFilter());
		return jsonSerializer.toJson(value);
	}
	
	public static String toJson(Object value, List<Djson> jsons) throws JsonProcessingException {
		CustomerJsonSerializer jsonSerializer = new CustomerJsonSerializer();
		for (Djson json:jsons) {
			jsonSerializer.filter(json.getType(), json.getInclude(), json.getFilter());
		}
		return jsonSerializer.toJson(value);
	}

}
