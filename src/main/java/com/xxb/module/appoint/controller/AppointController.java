package com.xxb.module.appoint.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xxb.base.BaseController;
import com.xxb.base.ProjectException;
import com.xxb.module.appoint.entity.Appoint;
import com.xxb.module.appoint.service.AppointService;
import com.xxb.module.identity.entity.User;
import com.xxb.util.jackson.Djson;

@RestController
@RequestMapping("/appoint/customer")
public class AppointController extends BaseController {
	
	@Autowired
	private AppointService appointService;
	
	/**
	 * 发起预约
	 * @param appoint
	 * @param request
	 * @return
	 */
	@PostMapping(value="/start.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String startAppoint(@RequestBody Appoint appoint, HttpServletRequest request) {
		try {
			User user = getCurrentUser(request);
			if (appoint.getAttendant()==null) {
				throw new ProjectException("没有指定技师!");
			}
			appoint.setCustomer(user);
			appoint.setOperator(user.getId());
			appointService.startAppoint(appoint);
			return handleResult("预约成功",appoint);
		} catch (Exception e) {
			return handleError("预约失败.",e);
		}
	}
	
	/**
	 * 我的预约
	 * @param page
	 * @param size
	 * @param date
	 * @param request
	 * @return
	 */
	@GetMapping(value="/mylist.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listCustomerAppoints(@RequestParam int page, @RequestParam int size, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date date, HttpServletRequest request) {
		try {
			User user = getCurrentUser(request);
			Pageable pageable = PageRequest.of(page, size, new Sort(Direction.DESC, "startDate"));
			Page<Appoint> resultPage = appointService.listAppointByDate(user.getId(),null,date,pageable);
			Djson ajson = new Djson(Appoint.class,null,"customer");
			Djson ujson = new Djson(User.class,null,"password,roles,group");
			return handleJsonPageResult("获取成功",resultPage,ajson,ujson);
		} catch (Exception e) {
			return handleError("获取失败.",e);
		}
	}
	
	/**
	 * 取消预约
	 * @param id
	 * @param request
	 * @return
	 */
	@PostMapping(value="/cancel.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String cancelAppoint(@RequestParam String id, HttpServletRequest request) {
		try {
			User user = getCurrentUser(request);
			appointService.cancelAppoint(id,user);
			return handleResult("取消预约成功");
		} catch (Exception e) {
			return handleError("取消预约失败.",e);
		}
	}

}
