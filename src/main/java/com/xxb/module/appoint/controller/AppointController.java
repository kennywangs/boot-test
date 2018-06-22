package com.xxb.module.appoint.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxb.base.BaseController;
import com.xxb.module.appoint.entity.Appoint;
import com.xxb.module.appoint.service.AppointService;
import com.xxb.module.identity.entity.User;

@RestController
@RequestMapping("/appoint")
public class AppointController extends BaseController {
	
	@Autowired
	private AppointService appointService;
	
	@RequestMapping(value="/start.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String startAppoint(@RequestBody Appoint appoint, HttpServletRequest request) {
		try {
			User user = getCurrentUser(request);
			if (appoint.getAttendant()==null) {
				User attendant = new User();
				attendant.setId("2c9ad0b6-de25-4b2f-a671-3c41025744dc");
			}
			appoint.setCustomer(user);
			appointService.startAppoint(appoint);
			return handleResult("登陆成功",appoint);
		} catch (Exception e) {
			return handleError("预约成功",e);
		}
	}

}
