package com.xxb.util.jackson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.proxy.HibernateProxy;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

@SuppressWarnings("deprecation")
@JsonFilter("JacksonFilter")
public class JacksonJsonFilter extends FilterProvider {

	Map<Class<?>, Set<String>> includeMap = new HashMap<>();
	Map<Class<?>, Set<String>> filterMap = new HashMap<>();

	public JacksonJsonFilter() {
		addToMap(filterMap, HibernateProxy.class, new String[] { "handler", "hibernateLazyInitializer" });
	}

	public void include(Class<?> type, String[] fields) {
		addToMap(includeMap, type, fields);
	}

	public void filter(Class<?> type, String[] fields) {
		addToMap(filterMap, type, fields);
	}

	private void addToMap(Map<Class<?>, Set<String>> map, Class<?> type, String[] fields) {
		Set<String> fieldSet = map.getOrDefault(type, new HashSet<>());
		fieldSet.addAll(Arrays.asList(fields));
		map.put(type, fieldSet);
	}

	@Override
	public BeanPropertyFilter findFilter(Object filterId) {
		throw new UnsupportedOperationException("Access to deprecated filters not supported");
	}

	@Override
	public PropertyFilter findPropertyFilter(Object filterId, Object valueToFilter) {

		return new SimpleBeanPropertyFilter() {

			@Override
			public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider prov,
					PropertyWriter writer) throws Exception {
				Class<?> type = pojo.getClass();
				if (applyHibernate(writer.getName())) {
					return;
				}
				boolean isProxy = false;
				if (pojo instanceof HibernateProxy) {
					isProxy = true;
				}
				if (apply(isProxy, type, writer.getName())) {
					writer.serializeAsField(pojo, jgen, prov);
				} else if (!jgen.canOmitFields()) {
					writer.serializeAsOmittedField(pojo, jgen, prov);
				}
			}
		};
	}

	public boolean apply(boolean isProxy, Class<?> type, String name) {
		if (isProxy) {
			type = type.getSuperclass();
		}
		Set<String> includeFields = includeMap.get(type);
		Set<String> filterFields = filterMap.get(type);
		if (includeFields != null && includeFields.contains(name)) {
			return true;
		} else if (filterFields != null && !filterFields.contains(name)) {
			return true;
		} else if (includeFields == null && filterFields == null) {
			return true;
		}
		return false;
	}

	public boolean applyHibernate(String name) {
		Set<String> filterFields = filterMap.get(HibernateProxy.class);
		if (filterFields.contains(name)) {
			return true;
		}
		return false;
	}

}
