package com.xxb.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	public static final String KEY_MD5 = "MD5";
	
	public static String crypt(String str) {
		if(str == null || str.length() == 0) {
			throw new IllegalArgumentException("String to encript cannot be null or zero length");
		}

		byte[] hash = encryptMD5(str.getBytes());
		String hexString = "";
		for(byte element : hash) {
			if((0xff & element) < 0x10) {
				hexString += "0" + Integer.toHexString((0xFF & element));
			} else {
				hexString += Integer.toHexString(0xFF & element);
			}
		}

		return hexString;
	}
	
	public static byte[] encryptMD5(byte[] data) {
		try {
			MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
			md5.update(data);
			return md5.digest();
		} catch(NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
