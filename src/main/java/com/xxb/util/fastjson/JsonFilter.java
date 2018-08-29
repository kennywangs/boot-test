package com.xxb.util.fastjson;

public class JsonFilter {
	
	private Class<?> type;
	private String include = "";
	private String filter = "";
	
	public JsonFilter(Class<?> type,String include,String filter) {
		this.type = type;
		this.include = include;
		this.filter = filter;
	}
	
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
	public String getInclude() {
		return include;
	}
	public void setInclude(String include) {
		this.include = include;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}

}
