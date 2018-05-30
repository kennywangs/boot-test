package com.xxb.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.util.VersionUtils;

public class GsonDateTypeAdapter extends TypeAdapter<Date> {
	
	private final List<DateFormat> dateFormats = new ArrayList<DateFormat>();
	
	public GsonDateTypeAdapter() {
	    dateFormats.add(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US));
	    if (!Locale.getDefault().equals(Locale.US)) {
	      dateFormats.add(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
	    }
	    if (VersionUtils.isJava9OrLater()) {
	      dateFormats.add(PreJava9DateFormatProvider.getUSDateTimeFormat(DateFormat.DEFAULT, DateFormat.DEFAULT));
	    }
	  }

	@Override
	public void write(JsonWriter out, Date value) throws IOException {
		if (value == null) {
		      out.nullValue();
		      return;
		    }
		    String dateFormatAsString = ISO8601Utils.format(value);
		    out.value(dateFormatAsString);
	}

	@Override
	public Date read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
		      in.nextNull();
		      return null;
		    }
		    return deserializeToDate(in.nextString());
	}
	
	private synchronized Date deserializeToDate(String json) {
	    for (DateFormat dateFormat : dateFormats) {
	      try {
	        return dateFormat.parse(json);
	      } catch (ParseException ignored) {}
	    }
	    try {
	    	return ISO8601Utils.parse(json, new ParsePosition(0));
	    } catch (ParseException e) {
	      throw new JsonSyntaxException(json, e);
	    }
	  }

}
