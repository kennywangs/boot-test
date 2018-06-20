package com.xxb.action;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.xxb.base.BaseController;
import com.xxb.module.identity.entity.User;
import com.xxb.module.identity.repository.UserRepository;
import com.xxb.module.identity.service.UserService;

@RestController
@RequestMapping("/system")
public class SystemAction extends BaseController {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private UserService userservice;
	
	@RequestMapping(value="/register_w",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String registerUser(@RequestBody User user) {
		if (StringUtils.isNoneEmpty(user.getId())) {
			return handelError("注册失败,用户id不为空");
		}
		if (repo.queryTopEntityByNameEqualsOrMobileEquals(user.getName(), user.getMobile())!=null) {
			return handelError("注册失败,用户已经存在");
		}
		try {
			user = userservice.saveUser(user);
			return handelResult("注册成功", user);
		} catch (Exception e) {
			return handelError("注册失败",e);
		}
	}
	
	@RequestMapping(value="/login",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String registerUser(@RequestBody JSONObject param) {
		try {
			userservice.login(param);
			return handelResult("登陆成功");
		} catch (Exception e) {
			return handelError("登陆失败",e);
		}
	}
	
}
