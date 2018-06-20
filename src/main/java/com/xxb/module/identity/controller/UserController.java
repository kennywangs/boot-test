package com.xxb.module.identity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.xxb.base.BaseController;
import com.xxb.module.identity.entity.Group;
import com.xxb.module.identity.entity.User;
import com.xxb.module.identity.repository.UserRepository;
import com.xxb.module.identity.service.UserService;
import com.xxb.util.jackson.Djson;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private UserService userservice;
	
	@RequestMapping(value="/search_native",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String searchNativeUser(@RequestBody JSONObject param) {
		Page<User> users = userservice.searchNativeUser(param);
		return handelPageResult("成功", users);
	}
	
	@RequestMapping(value="/search",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String searchUser(@RequestBody JSONObject param,@PageableDefault(value = 10, sort = { "name" }, direction = Sort.Direction.ASC) Pageable pageable) {
		Page<User> users = userservice.searchUser(param, pageable);
		Djson djson = new Djson(User.class,null,"password,roles,group");
		return handelJsonPageResult("成功", users, djson);
	}
	
	@RequestMapping(value="/save",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveUser(@RequestBody User user) {
		user = userservice.saveUser(user);
		Djson ujson = new Djson(User.class,null,"password,roles,group");
		return handelJsonResult("成功", user, ujson);
	}
	
	@RequestMapping(value="/view",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String viewUser(String id) {
		User user = repo.findById(id).get();
		Djson ujson = new Djson(User.class,null,"password,roles");
		Djson gjson = new Djson(Group.class,null,null);
		return handelJsonResult("成功", user, ujson, gjson);
	}
	
	@RequestMapping(value="/list",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listUser(@PageableDefault(value = 10, sort = { "name" }, direction = Sort.Direction.DESC) Pageable pageable) {
		Page<User> users = repo.findAll(pageable);
		Djson djson = new Djson(User.class,null,"password,roles,group");
		return handelJsonResult("成功", users, djson);
	}
	
	@RequestMapping(value="/modifypw",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String modifyPw(@RequestBody JSONObject param) {
		try {
			userservice.modifyPw(param);
			return handelResult("修改成功");
		} catch (Exception e) {
			return handelError("修改失败!",e);
		}
	}

}
