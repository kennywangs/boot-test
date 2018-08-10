package com.xxb.util.fastjson;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastJsonUtils {
	
	public static String writeJson(Object obj, JsonFilter... filters) {
		JsonPropertyFilter profilter = new JsonPropertyFilter();
		for (JsonFilter filter:filters) {
			profilter.filter(filter.getType(), filter.getInclude(), filter.getFilter());
		}
		String json = JSON.toJSONString(obj, profilter, SerializerFeature.DisableCircularReferenceDetect);
		return json;
	}
	
	public static String writeJson(Object obj, List<JsonFilter> filters) {
		JsonPropertyFilter profilter = new JsonPropertyFilter();
		for (JsonFilter filter:filters) {
			profilter.filter(filter.getType(), filter.getInclude(), filter.getFilter());
		}
		String json = JSON.toJSONString(obj, profilter, SerializerFeature.DisableCircularReferenceDetect);
		return json;
	}

}
