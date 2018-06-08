package com.xxb.freemarker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import freemarker.template.SimpleDate;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class MyConver implements TemplateMethodModelEx {
	
	private DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		if (null != arguments && 2 == arguments.size()) {
			String key = String.valueOf(arguments.get(0));
			try {
				switch (key) {
					case "simpleDate":
						Date date = ((SimpleDate)arguments.get(1)).getAsDate();
						return simpleDateFormat.format(date);
					default:
						return null;
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

}
