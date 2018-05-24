package com.xxb.web.jsonp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxb.base.ServiceResult;

@RestController
@RequestMapping("/jsonp")
public class JsonpController {
	
	@RequestMapping(value = "/testJsonp",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ServiceResult testJsonp(String appId,HttpServletRequest req){
		ServiceResult ret = new ServiceResult();
		ret.setMsg("jsonp test successful.");
		ret.setData(appId);
		return ret;
	}

}
