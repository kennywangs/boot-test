package com.xxb.util.Date;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.google.gson.internal.bind.util.ISO8601Utils;

public class ISO8601DateFormatEx extends StdDateFormat{
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
    public Date parse(String source, ParsePosition pos) {
        try {
        	return df.parse(source);
        }
        catch (ParseException e) {
        	try {
				return ISO8601Utils.parse(source, pos);
			} catch (ParseException ex) {
				return null;
			}
        }
    }
	
	@Override
    public Date parse(String source) throws ParseException {
        return parse(source, new ParsePosition(0));
    }

}
