package com.xxb.util;

import java.util.Date;

import org.hibernate.proxy.HibernateProxy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxb.util.Date.GsonDateTypeAdapter;
import com.xxb.util.jackson.HibernateProxyTypeAdapter;

public class JsonUtils {
	
	public static final Gson simpleGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	public static final Gson dateGson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateTypeAdapter()).registerTypeHierarchyAdapter(HibernateProxy.class, new HibernateProxyTypeAdapter()).create();
	
	public static String toJson(Object obj) {
		return dateGson.toJson(obj);
	}
	
	public static <T> T parseJson(String json,Class<T> clazz) {
		return simpleGson.fromJson(json, clazz);
	}

}
