package com.xxb.util.Date;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.google.gson.internal.bind.util.ISO8601Utils;

public class ISO8601DateEditor extends PropertyEditorSupport {
	private static final Logger logger = LoggerFactory.getLogger(ISO8601DateEditor.class);
	DateTimeFormatter ff = ISODateTimeFormat.dateTimeNoMillis();
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	private final boolean allowEmpty;
	private final int exactDateLength;

	public ISO8601DateEditor(boolean allowEmpty) {
		super();
		this.allowEmpty = allowEmpty;
		exactDateLength = -1;
	}

	/**
	 * Parse the Date from the given text, using the specified DateFormat.
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		} else if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
			throw new IllegalArgumentException(
					"Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
		} else {
			setValue(parse(text));
		}
	}

	private Date parse(String text) {
		Date date = null;
		try{	
			DateTime dt = ff.parseDateTime(text);
			date = dt.toDate();
		}catch(Exception ex){
			try{
				date = df.parse(text);
			}catch(ParseException ex1){
				logger.error("error date time format : "+text);
				throw new IllegalArgumentException(ex1);
			}
		}
		
		return date;
	}

	/**
	 * Format the Date as String, using the specified DateFormat.
	 */
	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		return (value != null ? ISO8601Utils.format(value) : "");
	}
}
