package com.xxb.util.fastjson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.proxy.HibernateProxy;

import com.alibaba.fastjson.serializer.PropertyFilter;

public class JsonPropertyFilter implements PropertyFilter {
	
	Map<Class<?>, Set<String>> includeMap = new HashMap<>();
	Map<Class<?>, Set<String>> filterMap = new HashMap<>();
	
	public JsonPropertyFilter() {
//		addToMap(filterMap, HibernateProxy.class, new String[] { "handler", "hibernateLazyInitializer" });
	}
	
	private void addToMap(Map<Class<?>, Set<String>> map, Class<?> type, String[] fields) {
		Set<String> fieldSet = map.getOrDefault(type, new HashSet<>());
		fieldSet.addAll(Arrays.asList(fields));
		map.put(type, fieldSet);
	}

	@Override
	public boolean apply(Object object, String name, Object value) {
		Class<?> type = object.getClass();
//		if (applyHibernate(name)) {
//			return false;
//		}
		boolean isProxy = false;
		if (object instanceof HibernateProxy) {
			isProxy = true;
		}
		if (apply(isProxy, type, name)) {
			return true;
		}
		return false;
	}
	
	private boolean apply(boolean isProxy, Class<?> type, String name) {
		if (isProxy) {
			type = type.getSuperclass();
		}
		Set<String> includeFields = includeMap.get(type);
		Set<String> filterFields = filterMap.get(type);
		if (includeFields == null && filterFields == null) {
			return true;
		}
		if (includeFields != null && includeFields.contains(name)) {
			return true;
		} else if (filterFields != null && !filterFields.contains(name)) {
			return true;
		}
		return false;
	}
	
//	private boolean applyHibernate(String name) {
//		Set<String> filterFields = filterMap.get(HibernateProxy.class);
//		if (filterFields.contains(name)) {
//			return true;
//		}
//		return false;
//	}

	public void filter(Class<?> clazz, String include, String filter) {
		if (clazz == null) return;
        if (StringUtils.isNotBlank(include)) {
            include(clazz, include.split(","));
        }
        if (StringUtils.isNotBlank(filter)) {
            filter(clazz, filter.split(","));
        }
	}
	
	private void include(Class<?> type, String[] fields) {
		addToMap(includeMap, type, fields);
	}
	
	public void filter(Class<?> type, String[] fields) {
		addToMap(filterMap, type, fields);
	}

}
