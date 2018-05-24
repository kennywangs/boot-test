package com.xxb.test.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxb.base.ServiceResult;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@RequestMapping(value = "/hello",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ServiceResult hello(@RequestHeader String appId){
		ServiceResult ret = new ServiceResult();
		ret.setMsg("hello world.");
		ret.setData(appId);
		return ret;
	}

}
