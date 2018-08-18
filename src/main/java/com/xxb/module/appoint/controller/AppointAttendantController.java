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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xxb.base.BaseController;
import com.xxb.module.appoint.entity.Appoint;
import com.xxb.module.appoint.service.AppointService;
import com.xxb.module.identity.entity.User;
import com.xxb.util.fastjson.JsonFilter;

@RestController
@RequestMapping("/appoint/attendant")
public class AppointAttendantController extends BaseController {
	
	@Autowired
	private AppointService appointService;
	
	@GetMapping(value="/confirm.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String confirmAppoint(@RequestParam String id, HttpServletRequest request) {
		try {
			User user = getCurrentUser(request);
			appointService.confirmAppoint(id,user);
			return handleResult("确认预约成功");
		} catch (Exception e) {
			return handleError("确认预约失败.",e);
		}
	}
	
	@GetMapping(value="/delete.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteAppoint(@RequestParam String id, HttpServletRequest request) {
		try {
			User user = getCurrentUser(request);
			if (user.getType()!=User.USER_TYPE_SUPER) {
				return handleError("您无权删除");
			}
			appointService.deleteAppoint(id);
			return handleResult("确认预约成功");
		} catch (Exception e) {
			return handleError("确认预约失败",e);
		}
	}
	
	/**
	 * 技师的预约
	 * @param page
	 * @param size
	 * @param date
	 * @param request
	 * @return
	 */
	@GetMapping(value="/mylist.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listCustomerAppoints(@RequestParam int page, @RequestParam int size, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date date, HttpServletRequest request) {
		try {
			User user = getCurrentUser(request);
			Pageable pageable = PageRequest.of(page, size, new Sort(Direction.DESC, "startDate"));
			if (user.getType()==User.USER_TYPE_SUPER) {
				Page<Appoint> resultPage = appointService.listAppointByDate(null,null,date,pageable);
				JsonFilter uFilter = new JsonFilter(User.class,null,"password,roles,group");
				return handleFastJsonPage("获取成功", resultPage, uFilter);
			}
			Page<Appoint> resultPage = appointService.listAppointByDate(user.getId(),null,date,pageable);
			JsonFilter aFilter = new JsonFilter(Appoint.class,null,"attendant");
			JsonFilter uFilter = new JsonFilter(User.class,null,"password,roles,group");
			return handleFastJsonPage("获取成功", resultPage, aFilter, uFilter);
		} catch (Exception e) {
			return handleError("获取失败.",e);
		}
	}

}
