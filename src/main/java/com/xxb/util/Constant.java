package com.xxb.util;

public class Constant {
	
	public static final String cacheAuthsKey = "project:authmap:";
	
	public static String getTokenkey(String userId, String token){
		return "Token:"+userId+":"+token;
	}

}
