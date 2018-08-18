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

import com.alibaba.fastjson.JSONObject;
import com.xxb.base.BaseController;
import com.xxb.base.ProjectException;
import com.xxb.module.appoint.entity.Appoint;
import com.xxb.module.appoint.service.AppointService;
import com.xxb.module.identity.entity.User;
import com.xxb.module.identity.service.UserService;
import com.xxb.util.fastjson.JsonFilter;

@RestController
@RequestMapping("/customer/appoint")
public class AppointController extends BaseController {
	
	@Autowired
	private AppointService appointService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 发起预约
	 * @param appoint
	 * @param request
	 * @return
	 */
	@PostMapping(value="/start",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
	@GetMapping(value="/mylist",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listCustomerAppoints(@RequestParam int page, @RequestParam int size, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date date, HttpServletRequest request) {
		try {
			User user = getCurrentUser(request);
			Pageable pageable = PageRequest.of(page, size, new Sort(Direction.DESC, "startDate"));
			Page<Appoint> resultPage = appointService.listAppointByDate(user.getId(),null,date,pageable);
			JsonFilter aFilter = new JsonFilter(Appoint.class,null,"customer,operator");
			JsonFilter uFilter = new JsonFilter(User.class,null,"password,roles,group,createDate,modifyDate");
			return handleFastJsonPage("获取成功", resultPage, aFilter, uFilter);
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
	@GetMapping(value="/cancel",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String cancelAppoint(@RequestParam String id, HttpServletRequest request) {
		try {
			User user = getCurrentUser(request);
			appointService.cancelAppoint(id,user);
			return handleResult("取消预约成功");
		} catch (Exception e) {
			return handleError("取消预约失败.",e);
		}
	}
	
	@PostMapping(value="/list-attendant",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listAttendant(HttpServletRequest request) {
		try {
			getCurrentUser(request);
			JSONObject searchParam = new JSONObject();
			searchParam.put("roleName", "attendant");
			Page<User> resultPage = userService.searchUser(searchParam, PageRequest.of(0, 1000));
			JsonFilter uFilter = new JsonFilter(User.class,null,"password,roles,group,createDate,modifyDate");
			return handleFastJsonPage("获取技师列表成功",resultPage,uFilter);
		} catch (Exception e) {
			return handleError("获取技师列表失败",e);
		}
	}
	
}
