package com.xxb.util;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {
	
	public static final Gson simpleGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	public static final Gson dateGson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateTypeAdapter()).create();
	
	public static String toJson(Object obj) {
		return dateGson.toJson(obj);
	}
	
	public static <T> T parseJson(String json,Class<T> clazz) {
		return simpleGson.fromJson(json, clazz);
	}

}
