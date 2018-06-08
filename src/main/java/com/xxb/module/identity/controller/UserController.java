package com.xxb.module.identity.controller;

import java.util.List;

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
import com.xxb.module.identity.entity.User;
import com.xxb.module.identity.repository.UserRepository;
import com.xxb.module.identity.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private UserService userservice;
	
	@RequestMapping(value="/search",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String searchUser(@RequestBody JSONObject param) {
		Page<User> users = userservice.searchUser(param);
		return handelPageResult("成功", users);
	}
	
	@RequestMapping(value="/save",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveUser(@RequestBody User user) {
		user = userservice.saveUser(user);
		return handelResult("成功", user);
	}
	
	@RequestMapping(value="/view",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String viewUser(String id) {
		User user = repo.findById(id).get();
		return handelResult("成功", user);
	}
	
	@RequestMapping(value="/list",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listUser(@PageableDefault(value = 10, sort = { "name" }, direction = Sort.Direction.DESC) Pageable pageable) {
		Page<User> users = repo.findAll(pageable);
		return handelResult("成功", users);
	}

}
