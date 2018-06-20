package com.xxb.util.jackson;

import java.io.IOException;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class HibernateProxyTypeAdapter extends TypeAdapter<HibernateProxy> {

	public HibernateProxyTypeAdapter() {
		
	}

	@Override
	public HibernateProxy read(JsonReader in) throws IOException {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public void write(JsonWriter out, HibernateProxy value) throws IOException {
//		if (value == null) {
			out.nullValue();
			return;
//		}
		// // Retrieve the original (not proxy) class
		// Class<?> baseType = Hibernate.getClass(value);
		// // Get the TypeAdapter of the original class, to delegate the serialization
		// TypeAdapter delegate = context.getAdapter(TypeToken.get(baseType));
		// // Get a filled instance of the original class
		// Object unproxiedValue = ((HibernateProxy)
		// value).getHibernateLazyInitializer()
		// .getImplementation();
		// // Serialize the value
		// delegate.write(out, unproxiedValue);
	}
}